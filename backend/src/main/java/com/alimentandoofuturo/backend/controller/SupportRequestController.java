package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.dto.ApiResponse;
import com.alimentandoofuturo.backend.model.entity.SupportRequest;
import com.alimentandoofuturo.backend.model.service.SupportRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suporte")
@CrossOrigin(origins = {"http://localhost:5173", "https://alimentando-o-futuro.netlify.app"})
public class SupportRequestController {

    private final SupportRequestService service;

    public SupportRequestController(SupportRequestService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SupportRequest>>> listar() {
        return ResponseEntity.ok(ApiResponse.success("Solicitações listadas com sucesso", service.listarTodos()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SupportRequest>> salvar(@RequestBody SupportRequest supportRequest) {
        return ResponseEntity.ok(ApiResponse.success("Solicitação criada com sucesso", service.salvar(supportRequest)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupportRequest>> buscar(@PathVariable Long id) {
        SupportRequest request = service.buscarPorId(id);
        if (request == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Solicitação não encontrada", "ID: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Solicitação encontrada", request));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<ApiResponse<List<SupportRequest>>> buscarPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Solicitações do usuário listadas", service.buscarPorUsuario(id)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<SupportRequest>>> buscarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.success("Solicitações por status listadas", service.buscarPorStatus(status)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupportRequest>> atualizar(@PathVariable Long id, @RequestBody SupportRequest supportRequest) {
        return ResponseEntity.ok(ApiResponse.success("Solicitação atualizada com sucesso", service.atualizar(id, supportRequest)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SupportRequest>> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        SupportRequest request = service.buscarPorId(id);
        if (request == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Solicitação não encontrada", "ID: " + id));
        }
        request.setStatus(body.get("status"));
        return ResponseEntity.ok(ApiResponse.success("Status atualizado com sucesso", service.atualizar(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> inativar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.success("Solicitação inativada com sucesso", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.success("Solicitação inativada com sucesso", null));
    }
}
