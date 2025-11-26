package tilacademy.attendance.services.impl;

import org.springframework.stereotype.Service;
import tilacademy.attendance.entities.Pkg;
import tilacademy.attendance.repositories.PkgRepository;
import tilacademy.attendance.services.PkgService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PkgServiceImpl implements PkgService {

    private final PkgRepository pkgRepository;

    public PkgServiceImpl(PkgRepository pkgRepository) {
        this.pkgRepository = pkgRepository;
    }

    @Override
    public List<Pkg> findAll() {
        return pkgRepository.findAll();
    }

    @Override
    public Optional<Pkg> findById(Long id) {
        return pkgRepository.findById(id);
    }

    @Override
    public Pkg create(Pkg pkg) {
        return pkgRepository.save(pkg);
    }

    @Override
    public Pkg update(Long id, Pkg pkg) {
        return pkgRepository.findById(id).map(existing -> {
            existing.setName(pkg.getName());
            existing.setDescription(pkg.getDescription());
            existing.setLessonsCount(pkg.getLessonsCount());
            existing.setDurationDays(pkg.getDurationDays());
            existing.setPrice(pkg.getPrice());
            existing.setIsActive(pkg.getIsActive());
            return pkgRepository.save(existing);
        }).orElseGet(() -> {
            pkg.setId(id);
            return pkgRepository.save(pkg);
        });
    }

    @Override
    public void delete(Long id) {
        pkgRepository.deleteById(id);
    }

    @Override
    public List<Pkg> findActive() {
        return pkgRepository.findByIsActiveTrue();
    }
}
