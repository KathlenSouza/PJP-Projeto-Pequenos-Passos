package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.model.Notificacao;
import com.api.pjpPequenosPassos.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/notificacoes")
@CrossOrigin(origins = "*")
public class NotificacaoController {

    private static final Logger log = Logger.getLogger(NotificacaoController.class.getName());
    private final NotificacaoService notificacaoService;

    // Construtor manual para injeção
    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    // ==================== CRIAR NOTIFICAÇÃO ====================
    @PostMapping
    public ResponseEntity<Notificacao> criarNotificacao(@RequestBody Notificacao notificacao) {
        try {
            Notificacao salva = notificacaoService.criarNotificacao(notificacao);
            return ResponseEntity.ok(salva);
        } catch (Exception e) {
            log.severe("❌ Erro ao criar notificação: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== LISTAR TODAS ====================
    @GetMapping
    public ResponseEntity<List<Notificacao>> listarTodas() {
        List<Notificacao> notificacoes = notificacaoService.listarTodas();
        return ResponseEntity.ok(notificacoes);
    }

    // ==================== LISTAR POR USUÁRIO ====================
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacao>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<Notificacao> notificacoes = notificacaoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(notificacoes);
    }

    // ==================== LISTAR NÃO LIDAS ====================
    @GetMapping("/usuario/{usuarioId}/nao-lidas")
    public ResponseEntity<List<Notificacao>> listarNaoLidas(@PathVariable Long usuarioId) {
        List<Notificacao> notificacoes = notificacaoService.listarNaoLidas(usuarioId);
        return ResponseEntity.ok(notificacoes);
    }

    // ==================== MARCAR COMO LIDA ====================
    @PatchMapping("/{id}/ler")
    public ResponseEntity<Void> marcarComoLida(@PathVariable UUID id) {
        try {
            notificacaoService.marcarComoLida(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.warning("⚠️ Erro ao marcar notificação como lida: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== DELETAR NOTIFICAÇÃO ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        try {
            notificacaoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.severe("❌ Erro ao deletar notificação: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== LIMPAR TODAS AS LIDAS ====================
    @DeleteMapping("/usuario/{usuarioId}/limpar-lidas")
    public ResponseEntity<Void> limparLidas(@PathVariable Long usuarioId) {
        try {
            notificacaoService.limparLidas(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.warning("⚠️ Erro ao limpar notificações lidas: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
