package tilacademy.attendance.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tilacademy.attendance.entities.Teacher;
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

    @Override
    public Teacher create(Teacher teacher) {
        return teacherRepository.save(teacher);
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
