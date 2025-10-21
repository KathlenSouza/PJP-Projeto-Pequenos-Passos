package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiarioRepository extends JpaRepository<Diario, Long> {
    List<Diario> findByCriancaId(Long criancaId);
    List<Diario> findByCriancaIdOrderByDataRegistroDesc(Long criancaId);
    Optional<Diario> findByCriancaIdAndDataRegistro(Long criancaId, LocalDate data);
    List<Diario> findByCriancaIdAndImportanteTrue(Long criancaId);

    @Query("SELECT d FROM Diario d WHERE d.crianca.id = :criancaId AND " +
           "d.dataRegistro BETWEEN :dataInicio AND :dataFim ORDER BY d.dataRegistro DESC")
    List<Diario> findByCriancaIdAndPeriodo(
        @Param("criancaId") Long criancaId,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );
}
