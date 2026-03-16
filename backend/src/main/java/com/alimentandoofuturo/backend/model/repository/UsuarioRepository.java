package com.alimentandoofuturo.backend.model.repository;

import com.alimentandoofuturo.backend.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByIdAndAtivoTrue(Long id);

    Optional<Usuario> findByEmailAndAtivoTrue(String email);

    List<Usuario> findByAtivoTrue();

    @Transactional
    void deleteByEmail(String email);
}