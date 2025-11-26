package tilacademy.attendance.services;

import tilacademy.attendance.entities.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    Attendance create(Attendance attendance);
    Optional<Attendance> findById(Long id);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByStudent(Long studentId);
    List<Attendance> findByTeacher(Long teacherId);
}
