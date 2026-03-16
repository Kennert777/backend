package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.entity.PasswordResetToken;
import com.alimentandoofuturo.backend.model.entity.Usuario;
import com.alimentandoofuturo.backend.model.service.EmailService;
import com.alimentandoofuturo.backend.model.service.PasswordResetTokenService;
import com.alimentandoofuturo.backend.model.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/password-reset")
@CrossOrigin(origins = "*")
public class PasswordResetTokenController {

    private final PasswordResetTokenService service;
    private final UsuarioService usuarioService;
    private final EmailService emailService;

    public PasswordResetTokenController(PasswordResetTokenService service, UsuarioService usuarioService, EmailService emailService) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarRecuperacao(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Usuario usuario = usuarioService.findByEmail(email);
        
        if (usuario == null) {
            return ResponseEntity.status(404).body(Map.of("erro", "Email não encontrado"));
        }

        service.deletarPorEmail(email);
        
        String token = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        PasswordResetToken resetToken = PasswordResetToken.builder()
            .token(token)
            .email(email)
            .expiryDate(LocalDateTime.now().plusHours(1))
            .used(false)
            .build();
        
        service.salvar(resetToken);
        
        try {
            emailService.enviarEmailRecuperacaoSenha(email, token);
        } catch (Exception e) {
            // Email não configurado, mas token foi gerado
        }
        
        return ResponseEntity.ok(Map.of("mensagem", "Código de recuperação gerado", "token", token));
    }

    @PostMapping("/redefinir")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String novaSenha = body.get("novaSenha");
        
        PasswordResetToken resetToken = service.buscarPorToken(token);
        
        if (resetToken == null) {
            return ResponseEntity.status(404).body(Map.of("erro", "Token inválido"));
        }
        
        if (resetToken.getUsed()) {
            return ResponseEntity.status(400).body(Map.of("erro", "Token já utilizado"));
        }
        
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body(Map.of("erro", "Token expirado"));
        }
        
        Usuario usuario = usuarioService.findByEmail(resetToken.getEmail());
        usuario.setSenha(novaSenha);
        usuarioService.save(usuario);
        
        resetToken.setUsed(true);
        service.salvar(resetToken);
        
        return ResponseEntity.ok(Map.of("mensagem", "Senha redefinida com sucesso"));
    }

    @GetMapping
    public List<PasswordResetToken> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public PasswordResetToken salvar(@RequestBody PasswordResetToken token) {
        return service.salvar(token);
    }

    @GetMapping("/{id}")
    public PasswordResetToken buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<?> buscarPorToken(@PathVariable String token) {
        PasswordResetToken resetToken = service.buscarPorToken(token);
        if (resetToken == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resetToken);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        PasswordResetToken token = service.buscarPorEmail(email);
        if (token == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(token);
    }

    @PutMapping("/{id}")
    public PasswordResetToken atualizar(@PathVariable Long id, @RequestBody PasswordResetToken token) {
        return service.atualizar(id, token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(Map.of("msg", "Token deletado"));
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> deletarPorEmail(@PathVariable String email) {
        service.deletarPorEmail(email);
        return ResponseEntity.ok(Map.of("msg", "Tokens deletados para o email"));
    }
}