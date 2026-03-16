package com.alimentandoofuturo.backend.model.service;

import com.alimentandoofuturo.backend.model.entity.Usuario;
import com.alimentandoofuturo.backend.model.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return repository.findByAtivoTrue();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findByIdAndAtivoTrue(id).orElse(null);
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getSenha() != null && !isBCryptEncoded(usuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuario) {
        Usuario usuarioExistente = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado")
        );
        usuario.setId(usuarioExistente.getId());
        
        // Preservar senha existente se não fornecida
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            usuario.setSenha(usuarioExistente.getSenha());
        } else if (!isBCryptEncoded(usuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        
        return repository.save(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado")
        );
        usuario.setAtivo(false);
        repository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmailAndAtivoTrue(email).orElse(null);
    }

    public boolean existsByEmail(String email) {
        return repository.findByEmailAndAtivoTrue(email).isPresent();
    }

    public Usuario findByEmail(String email) {
        return repository.findByEmailAndAtivoTrue(email).orElse(null);
    }

    public void save(Usuario usuario) {
        // Sempre criptografar senha se não estiver criptografada
        if (usuario.getSenha() != null && !isBCryptEncoded(usuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        repository.save(usuario);
    }

    private boolean isBCryptEncoded(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }

    public void saveWithEncryptedPassword(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        repository.save(usuario);
    }

    public boolean verificarSenha(String senhaRaw, String senhaEncriptada) {
        // Se a senha no banco não está criptografada, compara diretamente
        if (!isBCryptEncoded(senhaEncriptada)) {
            return senhaRaw.equals(senhaEncriptada);
        }
        // Se está criptografada, usa BCrypt
        return passwordEncoder.matches(senhaRaw, senhaEncriptada);
    }
}