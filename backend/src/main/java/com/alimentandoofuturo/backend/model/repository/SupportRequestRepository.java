package com.alimentandoofuturo.backend.model.repository;

import com.alimentandoofuturo.backend.model.entity.SupportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {
    List<SupportRequest> findByEmail(String email);
    List<SupportRequest> findByStatus(String status);
    List<SupportRequest> findByUsuarioId(Long usuarioId);

    List<SupportRequest> findByAtivoTrue();

    List<SupportRequest> findByEmailAndAtivoTrue(String email);

    List<SupportRequest> findByStatusAndAtivoTrue(String status);

    List<SupportRequest> findByUsuarioIdAndAtivoTrue(Long usuarioId);

    Optional<SupportRequest> findByIdAndAtivoTrue(Long id);
}