package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoAtividadeRepository extends JpaRepository<HistoricoAtividade, Long> {
    List<HistoricoAtividade> findByCriancaId(Long criancaId);
    Optional<HistoricoAtividade> findByCriancaIdAndAreaDesenvolvimento(Long criancaId, Tarefa.AreaDesenvolvimento area);

    @Query("SELECT h FROM HistoricoAtividade h WHERE h.crianca.id = :criancaId ORDER BY h.percentualProgresso DESC")
    List<HistoricoAtividade> findByCriancaIdOrderByProgresso(@Param("criancaId") Long criancaId);
}
