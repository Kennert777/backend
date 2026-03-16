package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.dto.LoginRequest;
import com.alimentandoofuturo.backend.model.dto.LoginResponse;
import com.alimentandoofuturo.backend.model.entity.Usuario;
import com.alimentandoofuturo.backend.model.service.JwtService;
import com.alimentandoofuturo.backend.model.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:5173", "https://alimentando-o-futuro.netlify.app"})
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Usuario user = usuarioService.findByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("erro", "Usuário não encontrado"));
            }

            if (!usuarioService.verificarSenha(request.getSenha(), user.getSenha())) {
                return ResponseEntity.status(401).body(Map.of("erro", "Senha incorreta"));
            }

            if (!user.getAtivo()) {
                return ResponseEntity.status(401).body(Map.of("erro", "Usuário inativo"));
            }

            String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getTipoPerfil());
            
            LoginResponse.UsuarioDto usuarioDto = new LoginResponse.UsuarioDto();
            usuarioDto.setId(user.getId());
            usuarioDto.setNome(user.getNome());
            usuarioDto.setEmail(user.getEmail());
            usuarioDto.setTipoPerfil(user.getTipoPerfil());
            usuarioDto.setAtivo(user.getAtivo());

            LoginResponse response = new LoginResponse();
            response.setUsuario(usuarioDto);
            response.setToken(token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro interno no servidor", "status", 500));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            if (usuarioService.existsByEmail(usuario.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Email já cadastrado"));
            }

            usuario.setTipoPerfil("USUARIO");
            usuarioService.save(usuario);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário cadastrado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Erro interno: " + e.getMessage()));
        }
    }
}