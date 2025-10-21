package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRiscoRepository extends JpaRepository<AvaliacaoRisco, Long> {
    List<AvaliacaoRisco> findByCriancaId(Long criancaId);
    List<AvaliacaoRisco> findByCriancaIdOrderByDataAvaliacaoDesc(Long criancaId);
    List<AvaliacaoRisco> findByNivelRiscoIn(List<AvaliacaoRisco.NivelRisco> niveis);

    @Query("SELECT a FROM AvaliacaoRisco a WHERE a.crianca.id = :criancaId AND " +
           "a.nivelRisco IN ('ALTO', 'CRITICO') AND a.alertaEnviado = false")
    List<AvaliacaoRisco> findAvaliacoesComAlertaPendente(@Param("criancaId") Long criancaId);

    Optional<AvaliacaoRisco> findFirstByCriancaIdAndAreaAvaliadaOrderByDataAvaliacaoDesc(
        Long criancaId,
        Tarefa.AreaDesenvolvimento area
    );
}
