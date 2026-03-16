package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.dto.ApiResponse;
import com.alimentandoofuturo.backend.model.entity.Colheita;
import com.alimentandoofuturo.backend.model.service.ColheitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/colheitas")
@CrossOrigin(origins = "*")
public class ColheitaController {

    private final ColheitaService service;

    public ColheitaController(ColheitaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Colheita>>> listar() {
        return ResponseEntity.ok(ApiResponse.success("Colheitas listadas com sucesso", service.listarTodas()));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Colheita>>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ApiResponse.success("Colheitas do usuário listadas", service.listarPorUsuario(usuarioId)));
    }

    @GetMapping("/horta/{hortaId}")
    public ResponseEntity<ApiResponse<List<Colheita>>> listarPorHorta(@PathVariable Long hortaId) {
        return ResponseEntity.ok(ApiResponse.success("Colheitas da horta listadas", service.listarPorHorta(hortaId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Colheita>> salvar(@RequestBody Colheita colheita) {
        return ResponseEntity.ok(ApiResponse.success("Colheita registrada com sucesso", service.salvar(colheita)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Colheita>> buscar(@PathVariable Long id) {
        Colheita colheita = service.buscarPorId(id);
        if (colheita == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Colheita não encontrada", "ID: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Colheita encontrada", colheita));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Colheita>> atualizar(@PathVariable Long id, @RequestBody Colheita colheita) {
        return ResponseEntity.ok(ApiResponse.success("Colheita atualizada com sucesso", service.atualizar(id, colheita)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> inativar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.success("Colheita inativada com sucesso", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponse.success("Colheita inativada com sucesso", null));
    }
}
