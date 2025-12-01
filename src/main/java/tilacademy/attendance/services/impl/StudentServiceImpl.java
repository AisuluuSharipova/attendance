package tilacademy.attendance.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tilacademy.attendance.entities.Student;
import tilacademy.attendance.repositories.StudentRepository;
import tilacademy.attendance.services.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student update(Long id, Student student) {
        return studentRepository.findById(id).map(existing -> {
            existing.setFirstName(student.getFirstName());
            existing.setLastName(student.getLastName());
            existing.setEmail(student.getEmail());
            existing.setPhone(student.getPhone());
            existing.setNotes(student.getNotes());
            existing.setPkg(student.getPkg());
            existing.setLessonsRemaining(student.getLessonsRemaining());
            return studentRepository.save(existing);
        }).orElseGet(() -> {
            student.setId(id);
            return studentRepository.save(student);
        });
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findByTeacherId(Long teacherId) {
        return studentRepository.findByTeacherId(teacherId);
    }


    @Override
    @Transactional
    public Student consumeLesson(Long studentId) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        Integer lr = s.getLessonsRemaining();
        if (lr == null) lr = 0;
        lr = lr - 1;
        s.setLessonsRemaining(lr);
        return studentRepository.save(s);
    }

    @Override
    @Transactional
    public void bulkConsume(List<Long> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) return;
        for (Long id : studentIds) {
            studentRepository.findById(id).ifPresent(s -> {
                Integer lr = s.getLessonsRemaining();
                if (lr == null) lr = 0;
                s.setLessonsRemaining(lr - 1);
                studentRepository.save(s);
            });
        }
    }
}
