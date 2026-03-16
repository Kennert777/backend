package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.service.ColheitaService;
import com.alimentandoofuturo.backend.model.service.HortaService;
import com.alimentandoofuturo.backend.model.service.UsuarioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = {"http://localhost:5173", "https://alimentando-o-futuro.netlify.app"})
public class RelatoriosController {

    private final ColheitaService colheitaService;
    private final HortaService hortaService;
    private final UsuarioService usuarioService;

    public RelatoriosController(ColheitaService colheitaService, HortaService hortaService, UsuarioService usuarioService) {
        this.colheitaService = colheitaService;
        this.hortaService = hortaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/csv/{userId}")
    public ResponseEntity<String> exportarCSV(@PathVariable Long userId) {
        var colheitas = colheitaService.listarPorUsuario(userId);
        
        StringBuilder csv = new StringBuilder();
        csv.append("Data,Produto,Quantidade(kg),Qualidade,Horta,Observacoes\n");
        
        for (var colheita : colheitas) {
            String nomeHorta = "";
            if (colheita.getHortaId() != null) {
                var horta = hortaService.buscarPorId(colheita.getHortaId());
                nomeHorta = horta != null ? horta.getNome() : "";
            }
            
            csv.append(colheita.getDataColheita()).append(",")
               .append(colheita.getTipoPlanta()).append(",")
               .append(colheita.getQuantidadeKg()).append(",")
               .append(colheita.getQualidade() != null ? colheita.getQualidade() : "").append(",")
               .append(nomeHorta).append(",")
               .append(colheita.getObservacoes() != null ? colheita.getObservacoes().replace(",", ";") : "").append("\n");
        }
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=colheitas_usuario_" + userId + ".csv")
            .contentType(MediaType.TEXT_PLAIN)
            .body(csv.toString());
    }

    @GetMapping("/graficos/{userId}")
    public Map<String, Object> dadosGraficos(@PathVariable Long userId) {
        var colheitas = colheitaService.listarPorUsuario(userId);
        
        Map<String, BigDecimal> producaoPorMes = colheitas.stream()
            .collect(Collectors.groupingBy(
                c -> c.getDataColheita().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                Collectors.reducing(BigDecimal.ZERO, c -> c.getQuantidadeKg(), BigDecimal::add)
            ));
            
        Map<String, Long> producaoPorTipo = colheitas.stream()
            .collect(Collectors.groupingBy(
                c -> c.getTipoPlanta(),
                Collectors.counting()
            ));
            
        return Map.of(
            "producaoMensal", producaoPorMes,
            "producaoPorTipo", producaoPorTipo
        );
    }

    @GetMapping("/producao-mensal/{userId}")
    public List<Map<String, Object>> producaoMensal(@PathVariable Long userId) {
        var colheitas = colheitaService.listarPorUsuario(userId);
        
        Map<String, BigDecimal> producaoPorMes = colheitas.stream()
            .collect(Collectors.groupingBy(
                c -> c.getDataColheita().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                Collectors.reducing(BigDecimal.ZERO, c -> c.getQuantidadeKg(), BigDecimal::add)
            ));
            
        return producaoPorMes.entrySet().stream()
            .map(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("mes", entry.getKey());
                map.put("quantidade", entry.getValue());
                return map;
            })
            .collect(Collectors.toList());
    }

    @GetMapping("/admin/geral")
    public Map<String, Object> relatorioGeral() {
        var usuarios = usuarioService.listarTodos();
        var hortas = hortaService.listarTodas();
        var colheitas = colheitaService.listarTodas();
        
        BigDecimal producaoTotal = colheitas.stream()
            .map(c -> c.getQuantidadeKg())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        long usuariosAtivos = usuarios.stream().filter(u -> u.getAtivo()).count();
        long hortasAprovadas = hortas.stream().filter(h -> h.getAprovada()).count();
        long hortasPendentes = hortas.stream().filter(h -> !h.getAprovada()).count();
        
        return Map.of(
            "totalUsuarios", usuarios.size(),
            "usuariosAtivos", usuariosAtivos,
            "totalHortas", hortas.size(),
            "hortasAprovadas", hortasAprovadas,
            "hortasPendentes", hortasPendentes,
            "totalColheitas", colheitas.size(),
            "producaoTotal", producaoTotal
        );
    }

    @GetMapping("/admin/usuarios")
    public Map<String, Object> relatorioUsuarios() {
        var usuarios = usuarioService.listarTodos();
        
        long admins = usuarios.stream().filter(u -> "ADMIN".equals(u.getTipoPerfil())).count();
        long users = usuarios.stream().filter(u -> "USER".equals(u.getTipoPerfil())).count();
        long ativos = usuarios.stream().filter(u -> u.getAtivo()).count();
        long inativos = usuarios.stream().filter(u -> !u.getAtivo()).count();
        
        return Map.of(
            "totalUsuarios", usuarios.size(),
            "admins", admins,
            "users", users,
            "ativos", ativos,
            "inativos", inativos
        );
    }
}