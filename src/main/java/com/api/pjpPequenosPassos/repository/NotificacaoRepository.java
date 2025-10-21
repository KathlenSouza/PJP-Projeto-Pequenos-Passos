package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByUsuarioId(Long usuarioId);
    List<Notificacao> findByUsuarioIdAndLidaFalse(Long usuarioId);
    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);

    @Query("SELECT COUNT(n) FROM Notificacao n WHERE n.usuario.id = :usuarioId AND n.lida = false")
    Long countNaoLidasByUsuario(@Param("usuarioId") Long usuarioId);

    List<Notificacao> findByEnviadaFalse();
}
