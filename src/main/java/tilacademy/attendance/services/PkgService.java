package tilacademy.attendance.services;

import tilacademy.attendance.entities.Pkg;

import java.util.List;
import java.util.Optional;

public interface PkgService {
    List<Pkg> findAll();
    Optional<Pkg> findById(Long id);
    Pkg create(Pkg pkg);
    Pkg update(Long id, Pkg pkg);
    void delete(Long id);
    List<Pkg> findActive();
}
