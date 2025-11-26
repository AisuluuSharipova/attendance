package tilacademy.attendance.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tilacademy.attendance.entities.Attendance;
import tilacademy.attendance.repositories.AttendanceRepository;
import tilacademy.attendance.services.AttendanceService;
import tilacademy.attendance.services.StudentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentService studentService;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 StudentService studentService) {
        this.attendanceRepository = attendanceRepository;
        this.studentService = studentService;
    }

    @Override
    @Transactional
    public Attendance create(Attendance attendance) {
        Attendance saved = attendanceRepository.save(attendance);
        studentService.consumeLesson(attendance.getStudent().getId());
        return saved;
    }

    @Override
    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Override
    public List<Attendance> findByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date);
    }

    @Override
    public List<Attendance> findByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    @Override
    public List<Attendance> findByTeacher(Long teacherId) {
        return attendanceRepository.findByTeacherId(teacherId);
    }
}
