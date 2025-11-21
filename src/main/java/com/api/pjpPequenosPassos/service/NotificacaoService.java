package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.Notificacao;
import com.api.pjpPequenosPassos.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class NotificacaoService {

    private static final Logger log = Logger.getLogger(NotificacaoService.class.getName());
    private final NotificacaoRepository notificacaoRepository;

    // Injeção manual (sem Lombok)
    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    // ==================== CRIAR ====================
    public Notificacao criarNotificacao(Notificacao notificacao) {
        try {
            notificacao.setDataEnvio(LocalDateTime.now());
            notificacao.setLida(false);
            Notificacao salva = notificacaoRepository.save(notificacao);
            log.info("✅ Notificação criada: " + salva.getMensagem());
            return salva;
        } catch (Exception e) {
            log.severe("❌ Erro ao criar notificação: " + e.getMessage());
            throw new RuntimeException("Erro ao criar notificação", e);
        }
    }

    // ==================== LISTAR TODAS ====================
    public List<Notificacao> listarTodas() {
        log.info("📋 Listando todas as notificações");
        return notificacaoRepository.findAll();
    }

    // ==================== LISTAR POR USUÁRIO ====================
    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        log.info("📋 Buscando notificações do usuário ID: " + usuarioId);
        return notificacaoRepository.findByUsuarioId(usuarioId);
    }

    // ==================== LISTAR NÃO LIDAS ====================
    public List<Notificacao> listarNaoLidas(Long usuarioId) {
        log.info("🔔 Buscando notificações não lidas para usuário ID: " + usuarioId);
        return notificacaoRepository.findByUsuarioIdAndLidaFalse(usuarioId);
    }

    // ==================== MARCAR COMO LIDA ====================
    @Transactional
    public void marcarComoLida(UUID id) {
        Optional<Notificacao> notificacaoOpt = notificacaoRepository.findById(id);
        if (notificacaoOpt.isPresent()) {
            Notificacao notificacao = notificacaoOpt.get();
            notificacao.setLida(true);
            notificacaoRepository.save(notificacao);
            log.info("📨 Notificação marcada como lida: " + notificacao.getMensagem());
        } else {
            log.warning("⚠️ Notificação não encontrada para ID: " + id);
            throw new RuntimeException("Notificação não encontrada");
        }
    }

    // ==================== DELETAR ====================
    public void deletar(UUID id) {
        try {
            notificacaoRepository.deleteById(id);
            log.info("🗑️ Notificação deletada ID: " + id);
        } catch (Exception e) {
            log.severe("❌ Erro ao deletar notificação ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Erro ao deletar notificação", e);
        }
    }

    // ==================== LIMPAR TODAS AS LIDAS ====================
    @Transactional
    public void limparLidas(Long usuarioId) {
        List<Notificacao> lidas = notificacaoRepository.findByUsuarioIdAndLidaTrue(usuarioId);
        notificacaoRepository.deleteAll(lidas);
        log.info("🧹 Notificações lidas removidas do usuário ID: " + usuarioId);
    }
}

