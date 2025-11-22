package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.model.Relatorio;
import com.api.pjpPequenosPassos.model.Usuario;
import com.api.pjpPequenosPassos.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    // ==================== GERAR RELATÓRIO ====================
    @PostMapping
    public ResponseEntity<Relatorio> gerarRelatorio(
            @RequestParam String tipo,
            @RequestParam String conteudo) {

        // ⚠️ Como ainda não temos login, simulamos um usuário fixo
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        

        Relatorio relatorio = relatorioService.gerarRelatorio(tipo, conteudo, usuario);
        return ResponseEntity.ok(relatorio);
    }

    // ==================== LISTAR TODOS ====================
    @GetMapping
    public ResponseEntity<List<Relatorio>> listarTodos() {
        return ResponseEntity.ok(relatorioService.listarTodos());
    }

    // ==================== BUSCAR POR TIPO ====================
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Relatorio>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(relatorioService.buscarPorTipo(tipo));
    }

    // ==================== DELETAR ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        relatorioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
