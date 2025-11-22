package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.model.HistoricoAtividade;
import com.api.pjpPequenosPassos.service.HistoricoAtividadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/historico")
@CrossOrigin(origins = "*")
public class HistoricoController {

    private static final Logger log = LoggerFactory.getLogger(HistoricoController.class);

    @Autowired
    private HistoricoAtividadeService historicoService;

    // ==================== BUSCAR POR ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoAtividade> buscarPorId(@PathVariable UUID id) {
        log.info("Requisição recebida: buscar histórico por ID {}", id);
        try {
            HistoricoAtividade historico = historicoService.buscarPorId(id);
            return ResponseEntity.ok(historico);
        } catch (Exception e) {
            log.error("Erro ao buscar histórico: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== LISTAR TODOS ====================
    @GetMapping
    public ResponseEntity<List<HistoricoAtividade>> listarTodos() {
        log.info("Listando todos os históricos");
        List<HistoricoAtividade> historicos = historicoService.listarTodos();
        return ResponseEntity.ok(historicos);
    }

    // ==================== (DESATIVADO) LISTAR POR CRIANÇA ====================
    // Mantido comentado pois não há relacionamento com Crianca
    // @GetMapping("/crianca/{criancaId}")
    // public ResponseEntity<List<HistoricoAtividade>> listarPorCrianca(@PathVariable Long criancaId) {
    //     log.info("Listando históricos da criança ID {}", criancaId);
    //     List<HistoricoAtividade> historicos = historicoService.listarPorCrianca(criancaId);
    //     return ResponseEntity.ok(historicos);
    // }

    // ==================== FILTRAR POR PERÍODO ====================
    @GetMapping("/periodo")
    public ResponseEntity<List<HistoricoAtividade>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        log.info("Filtrando históricos entre {} e {}", inicio, fim);
        List<HistoricoAtividade> historicos = historicoService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(historicos);
    }

    // ==================== SALVAR HISTÓRICO ====================
    @PostMapping
    public ResponseEntity<HistoricoAtividade> salvar(@RequestBody HistoricoAtividade historico) {
        log.info("Salvando novo histórico");
        HistoricoAtividade salvo = historicoService.salvar(historico);
        return ResponseEntity.ok(salvo);
    }

    // ==================== DELETAR ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        log.warn("Deletando histórico ID {}", id);
        historicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
