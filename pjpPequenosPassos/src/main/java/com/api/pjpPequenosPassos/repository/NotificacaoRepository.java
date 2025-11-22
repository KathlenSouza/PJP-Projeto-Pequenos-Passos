package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {

    // 🔔 Lista todas as notificações de um usuário específico
    List<Notificacao> findByUsuarioId(Long usuarioId);

    // 🔔 Lista apenas as notificações não lidas de um usuário
    List<Notificacao> findByUsuarioIdAndLidaFalse(Long usuarioId);

    // 🧹 Lista apenas as notificações lidas (usado para limpeza)
    List<Notificacao> findByUsuarioIdAndLidaTrue(Long usuarioId);
}
