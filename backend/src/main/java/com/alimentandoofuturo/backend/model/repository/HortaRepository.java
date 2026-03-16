package com.alimentandoofuturo.backend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alimentandoofuturo.backend.model.entity.Horta;
import java.util.List;
import java.util.Optional;

public interface HortaRepository extends JpaRepository<Horta, Long> {
    List<Horta> findByUsuarioResponsavelId(Long usuarioId);

    List<Horta> findByAtivoTrue();

    List<Horta> findByUsuarioResponsavelIdAndAtivoTrue(Long usuarioId);

    Optional<Horta> findByIdAndAtivoTrue(Long id);
}
