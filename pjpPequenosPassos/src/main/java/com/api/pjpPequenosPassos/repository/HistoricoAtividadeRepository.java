package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.HistoricoAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HistoricoAtividadeRepository extends JpaRepository<HistoricoAtividade, UUID> {

    // List<HistoricoAtividade> findByCriancaId(Long criancaId);
    // Optional<HistoricoAtividade> findByCriancaIdAndAreaDesenvolvimento(Long criancaId, Tarefa.AreaDesenvolvimento area);
    // @Query("SELECT h FROM HistoricoAtividade h WHERE h.crianca.id = :criancaId ORDER BY h.percentualProgresso DESC")
    // List<HistoricoAtividade> findByCriancaIdOrderByProgresso(@Param("criancaId") Long criancaId);

    List<HistoricoAtividade> findByDataConclusaoBetween(LocalDateTime inicio, LocalDateTime fim);
    List<HistoricoAtividade> findAllByOrderByDataConclusaoDesc();
    List<HistoricoAtividade> findByCriancaIdOrderByDataConclusaoDesc(Long criancaId);
    List<HistoricoAtividade> findByCriancaId(Long criancaId);

}
