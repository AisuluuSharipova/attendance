package tilacademy.attendance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tilacademy.attendance.entities.Pkg;

import java.util.List;
import java.util.Optional;

public interface PkgRepository extends JpaRepository<Pkg, Long> {
    List<Pkg> findByIsActiveTrue();
    Optional<Pkg> findByNameIgnoreCase(String name);
}
