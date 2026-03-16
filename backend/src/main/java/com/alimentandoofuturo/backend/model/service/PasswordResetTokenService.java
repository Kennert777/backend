package com.alimentandoofuturo.backend.model.service;

import com.alimentandoofuturo.backend.model.entity.PasswordResetToken;
import com.alimentandoofuturo.backend.model.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository repository;

    public PasswordResetTokenService(PasswordResetTokenRepository repository) {
        this.repository = repository;
    }

    public List<PasswordResetToken> listarTodos() {
        return repository.findAll();
    }

    public PasswordResetToken buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PasswordResetToken salvar(PasswordResetToken token) {
        return repository.save(token);
    }

    public PasswordResetToken atualizar(Long id, PasswordResetToken token) {
        PasswordResetToken tokenExistente = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Token não encontrado")
        );
        token.setId(tokenExistente.getId());
        return repository.save(token);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public PasswordResetToken buscarPorToken(String token) {
        return repository.findByToken(token).orElse(null);
    }

    public PasswordResetToken buscarPorEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void deletarPorEmail(String email) {
        repository.deleteByEmail(email);
    }
}