package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.dto.ApiResponse;
import com.alimentandoofuturo.backend.model.dto.PerfilUpdateRequest;
import com.alimentandoofuturo.backend.model.entity.Usuario;
import com.alimentandoofuturo.backend.model.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService){ 
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<ApiResponse<List<Usuario>>> findAll(){
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success("Usuários listados com sucesso", usuarios));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> listar() {
        List<Usuario> l = usuarioService.listarTodos();
        l.forEach(u -> u.setSenha(null));
        return ResponseEntity.ok(ApiResponse.success("Usuários listados com sucesso", l));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> buscar(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Usuário não encontrado", "ID: " + id));
        }
        usuario.setSenha(null);
        return ResponseEntity.ok(ApiResponse.success("Usuário encontrado", usuario));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Usuario>> salvar(@RequestBody Usuario usuario) {
        Usuario usuarioSalvo = usuarioService.salvar(usuario);
        usuarioSalvo.setSenha(null);
        return ResponseEntity.ok(ApiResponse.success("Usuário criado com sucesso", usuarioSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> atualizar(@PathVariable Long id, @RequestBody Usuario u) {
        Usuario updated = usuarioService.atualizar(id, u);
        updated.setSenha(null);
        return ResponseEntity.ok(ApiResponse.success("Usuário atualizado com sucesso", updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> inativar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.ok(ApiResponse.success("Usuário inativado com sucesso", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.ok(ApiResponse.success("Usuário inativado com sucesso", null));
    }

    @PutMapping("/perfil")
    public ResponseEntity<ApiResponse<Usuario>> atualizarPerfil(
            @RequestHeader("Authorization") String token,
            @RequestBody PerfilUpdateRequest request) {
        String jwtToken = token.replace("Bearer ", "");
        Long userId = Long.parseLong(jwtService.extractClaims(jwtToken).getSubject());
        
        Usuario usuario = usuarioService.buscarPorId(userId);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Usuário não encontrado", "ID: " + userId));
        }
        
        if (request.getNome() != null) usuario.setNome(request.getNome());
        if (request.getEmail() != null) usuario.setEmail(request.getEmail());
        if (request.getSenha() != null) usuario.setSenha(request.getSenha());
        
        Usuario updated = usuarioService.salvar(usuario);
        updated.setSenha(null);
        return ResponseEntity.ok(ApiResponse.success("Perfil atualizado com sucesso", updated));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        System.out.println("=== CADASTRO RECEBIDO ===");
        System.out.println("Email: " + (usuario.getEmail() != null ? usuario.getEmail() : "NULL"));
        System.out.println("Senha: " + (usuario.getSenha() != null ? "[PRESENTE]" : "NULL"));
        System.out.println("Nome: " + (usuario.getNome() != null ? usuario.getNome() : "NULL"));
        
        try {
            if (usuario.getEmail() == null || usuario.getSenha() == null) {
                System.out.println("Erro: Email ou senha nulos");
                return ResponseEntity.badRequest().body(Map.of("erro", "Email e senha são obrigatórios"));
            }

            if (usuarioService.existsByEmail(usuario.getEmail())) {
                System.out.println("Erro: Email já existe");
                return ResponseEntity.badRequest().body(Map.of("erro", "Email já cadastrado"));
            }

            usuarioService.save(usuario);
            System.out.println("Usuário salvo com sucesso");
            return ResponseEntity.ok(Map.of("mensagem", "Usuário cadastrado com sucesso"));
        } catch (Exception e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("erro", "Erro interno: " + e.getMessage()));
        }
    }

    @PostMapping("/admin/cadastro")
    public ResponseEntity<?> cadastrarAdmin(@RequestBody Usuario usuario) {
        try {
            if (usuario.getEmail() == null || usuario.getSenha() == null) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Email e senha são obrigatórios"));
            }

            if (usuarioService.existsByEmail(usuario.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Email já cadastrado"));
            }

            usuario.setTipoPerfil("ADMIN");
            usuarioService.save(usuario);
            return ResponseEntity.ok(Map.of("mensagem", "Administrador cadastrado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Erro interno: " + e.getMessage()));
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Usuario usuario) {
        System.out.println("=== LOGIN ADMIN RECEBIDO ===");
        System.out.println("Email: " + usuario.getEmail());
        try {
            if (usuario.getEmail() == null || usuario.getSenha() == null) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Email e senha são obrigatórios"));
            }

            Usuario userEncontrado = usuarioService.findByEmail(usuario.getEmail());
            if (userEncontrado == null) {
                System.out.println("Usuário não encontrado: " + usuario.getEmail());
                return ResponseEntity.status(404).body(Map.of("erro", "Usuário não encontrado"));
            }

            System.out.println("Usuário encontrado - Tipo: " + userEncontrado.getTipoPerfil());
            if (!"ADMIN".equals(userEncontrado.getTipoPerfil())) {
                System.out.println("Acesso negado - Não é admin");
                return ResponseEntity.status(403).body(Map.of("erro", "Acesso negado. Apenas administradores"));
            }

            if (!usuarioService.verificarSenha(usuario.getSenha(), userEncontrado.getSenha())) {
                return ResponseEntity.status(401).body(Map.of("erro", "Senha incorreta"));
            }

            if (!userEncontrado.getAtivo()) {
                return ResponseEntity.status(401).body(Map.of("erro", "Usuário inativo"));
            }

            String token = jwtService.generateToken(userEncontrado.getId(), userEncontrado.getEmail(), userEncontrado.getTipoPerfil());

            Map<String, Object> adminDTO = Map.of(
                "id", userEncontrado.getId(),
                "nome", userEncontrado.getNome(),
                "email", userEncontrado.getEmail(),
                "tipoPerfil", userEncontrado.getTipoPerfil(),
                "ativo", userEncontrado.getAtivo()
            );

            return ResponseEntity.ok(Map.of(
                "usuario", adminDTO,
                "token", token
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro interno no servidor"));
        }
    }
}
