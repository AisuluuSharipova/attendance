package tilacademy.attendance.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tilacademy.attendance.entities.Teacher;
import tilacademy.attendance.entities.User;
import tilacademy.attendance.repositories.TeacherRepository;
import tilacademy.attendance.services.TeacherService;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }

    @Transactional
    public Teacher createTeacherForUser(User user, String firstName, String lastName, String email, String phone) {
        // Не создаём, если уже существует
        if (teacherRepository.findByUser_Id(user.getId()).isPresent()) {
            throw new IllegalStateException("Teacher profile already exists for user " + user.getId());
        }

        Teacher t = Teacher.builder()
                .user(user)           // MapsId возьмёт user.id в качестве teacher.id
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .build();

        return teacherRepository.save(t);
    }

    @Override
    public Teacher update(Long id, Teacher teacher) {
        return teacherRepository.findById(id).map(existing -> {
            existing.setFirstName(teacher.getFirstName());
            existing.setLastName(teacher.getLastName());
            existing.setEmail(teacher.getEmail());
            existing.setPhone(teacher.getPhone());
            return teacherRepository.save(existing);
        }).orElseGet(() -> {
            teacher.setId(id);
            return teacherRepository.save(teacher);
        });
    }

    @Override
    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }
}
