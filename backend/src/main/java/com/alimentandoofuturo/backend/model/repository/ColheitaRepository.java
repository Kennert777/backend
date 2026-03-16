package com.alimentandoofuturo.backend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alimentandoofuturo.backend.model.entity.Colheita;
import java.util.List;
import java.util.Optional;

public interface ColheitaRepository extends JpaRepository<Colheita, Long> {
    List<Colheita> findByUsuarioId(Long usuarioId);
    List<Colheita> findByHortaId(Long hortaId);

    List<Colheita> findByAtivoTrue();

    List<Colheita> findByUsuarioIdAndAtivoTrue(Long usuarioId);

    List<Colheita> findByHortaIdAndAtivoTrue(Long hortaId);

    Optional<Colheita> findByIdAndAtivoTrue(Long id);
}
