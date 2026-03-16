package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.dto.ApiResponse;
import com.alimentandoofuturo.backend.model.entity.Horta;
import com.alimentandoofuturo.backend.model.service.HortaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hortas")
@CrossOrigin(origins = "*")
public class HortaController {

    private final HortaService service;

    public HortaController(HortaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Horta>>> listar() {
        return ResponseEntity.ok(ApiResponse.success("Hortas listadas com sucesso", service.listarTodas()));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Horta>>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ApiResponse.success("Hortas do usuário listadas", service.listarPorUsuario(usuarioId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Horta>> salvar(@RequestBody Horta horta) {
        return ResponseEntity.ok(ApiResponse.success("Horta criada com sucesso", service.salvar(horta)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Horta>> buscar(@PathVariable Long id) {
        Horta horta = service.buscarPorId(id);
        if (horta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Horta não encontrada", "ID: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Horta encontrada", horta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Horta>> atualizar(@PathVariable Long id, @RequestBody Horta horta) {
        return ResponseEntity.ok(ApiResponse.success("Horta atualizada com sucesso", service.atualizar(id, horta)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> inativar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.success("Horta inativada com sucesso", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.success("Horta inativada com sucesso", null));
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<ApiResponse<Horta>> aprovar(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Object adminIdObj = request.get("adminId");
        Long adminId = adminIdObj instanceof Integer ? ((Integer) adminIdObj).longValue() : (Long) adminIdObj;
        return ResponseEntity.ok(ApiResponse.success("Horta aprovada com sucesso", service.aprovar(id, adminId)));
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<ApiResponse<Horta>> rejeitar(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Object adminIdObj = request.get("adminId");
        Long adminId = adminIdObj instanceof Integer ? ((Integer) adminIdObj).longValue() : (Long) adminIdObj;
        String motivo = (String) request.get("motivo");
        return ResponseEntity.ok(ApiResponse.success("Horta rejeitada", service.rejeitar(id, adminId, motivo)));
    }
}
