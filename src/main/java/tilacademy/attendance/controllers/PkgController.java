package tilacademy.attendance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tilacademy.attendance.dto.PkgDto;
import tilacademy.attendance.dto.CreatePkgDto;
import tilacademy.attendance.dto.UpdatePkgDto;
import tilacademy.attendance.entities.Pkg;
import tilacademy.attendance.mappers.PkgMapper;
import tilacademy.attendance.services.PkgService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/packages")
public class PkgController {

    private final PkgService pkgService;
    private final PkgMapper pkgMapper;

    public PkgController(PkgService pkgService, PkgMapper pkgMapper) {
        this.pkgService = pkgService;
        this.pkgMapper = pkgMapper;
    }

    @GetMapping
    public List<PkgDto> list() {
        return pkgService.findAll().stream().map(pkgMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/active")
    public List<PkgDto> listActive() {
        return pkgService.findActive().stream().map(pkgMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PkgDto> get(@PathVariable Long id) {
        return pkgService.findById(id)
                .map(pkgMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PkgDto> create(@Valid @RequestBody CreatePkgDto dto) {
        Pkg p = pkgMapper.toEntity(dto);
        if (p.getIsActive() == null) p.setIsActive(true);
        Pkg created = pkgService.create(p);
        PkgDto out = pkgMapper.toDto(created);
        return ResponseEntity.created(URI.create("/api/v1/packages/" + created.getId())).body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PkgDto> update(@PathVariable Long id, @Valid @RequestBody UpdatePkgDto dto) {
        Pkg p = pkgMapper.toEntity(dto);
        Pkg updated = pkgService.update(id, p);
        return ResponseEntity.ok(pkgMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pkgService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
