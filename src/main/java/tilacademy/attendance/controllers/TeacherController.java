package tilacademy.attendance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tilacademy.attendance.dto.CreateTeacherDto;
import tilacademy.attendance.dto.TeacherWithCredentialsDto;
import tilacademy.attendance.dto.TeacherDto;
import tilacademy.attendance.entities.Teacher;
import tilacademy.attendance.entities.User;
import tilacademy.attendance.entities.Role;
import tilacademy.attendance.mappers.TeacherMapper;
import tilacademy.attendance.services.TeacherService;
import tilacademy.attendance.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.Valid;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherController(TeacherService teacherService,
                             TeacherMapper teacherMapper,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<TeacherDto> list() {
        return teacherService.findAll().stream().map(teacherMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> get(@PathVariable Long id) {
        return teacherService.findById(id)
                .map(teacherMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create teacher + associated User credentials.
     * - создаём User (username/password)
     * - затем создаём Teacher, связав его с User через teacherService.createTeacherForUser(...)
     *
     * Возвращаем rawPassword один раз в ответе (чтобы менеджер мог передать учителю).
     */
    @PostMapping
    public ResponseEntity<TeacherWithCredentialsDto> create(@Valid @RequestBody CreateTeacherDto dto) {
        // 1) prepare username
        String username = (dto.username() == null || dto.username().isBlank()) ? dto.email() : dto.username().trim();

        // 2) prepare raw password (either provided or generated)
        String rawPassword = (dto.password() == null || dto.password().isBlank()) ? generateSecurePassword(12) : dto.password();

        // 3) basic validation
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(new TeacherWithCredentialsDto(null, null, null, null, null, null, null, "USERNAME_EXISTS"));
        }

        // 4) create User entity and save
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);
        user.setRole(Role.TEACHER);

        User savedUser = userRepository.save(user);

        // 5) create Teacher profile linked to savedUser
        // Предполагается, что teacherService.createTeacherForUser сохраняет Teacher и связывает его с User,
        // и в случае shared PK (MapsId) teacher.id == user.id.
        Teacher created;
        try {
            created = teacherService.createTeacherForUser(
                    savedUser,
                    dto.firstName(),
                    dto.lastName(),
                    dto.email(),
                    dto.phone()
            );
        } catch (Exception ex) {
            // если создание профиля упало — откатим созданного пользователя или вернём ошибку
            // (в идеале оборачивать в @Transactional в сервисе, чтобы и user и teacher создавались атомарно)
            return ResponseEntity.status(500).body(new TeacherWithCredentialsDto(null, null, null, null, null, null, null, "TEACHER_CREATE_FAILED"));
        }

        TeacherWithCredentialsDto out = new TeacherWithCredentialsDto(
                created.getId(),
                created.getFirstName(),
                created.getLastName(),
                created.getFullName(),
                created.getEmail(),
                created.getPhone(),
                username,
                rawPassword
        );

        return ResponseEntity.created(URI.create("/api/v1/teachers/" + created.getId())).body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> update(@PathVariable Long id, @Valid @RequestBody CreateTeacherDto dto) {
        Teacher t = Teacher.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .phone(dto.phone())
                .build();

        Teacher updated = teacherService.update(id, t);
        return ResponseEntity.ok(teacherMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // secure random password generator
    private static String generateSecurePassword(int length) {
        SecureRandom rng = new SecureRandom();
        byte[] buffer = new byte[length];
        rng.nextBytes(buffer);
        String base = Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
        if (base.length() <= length) return base;
        return base.substring(0, length);
    }
}
