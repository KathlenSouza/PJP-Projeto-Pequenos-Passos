package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByCriancaId(Long criancaId);
    List<Agenda> findByCriancaIdAndStatus(Long criancaId, Agenda.StatusAgenda status);

    @Query("SELECT a FROM Agenda a WHERE a.crianca.id = :criancaId AND " +
           "a.dataAgendada BETWEEN :dataInicio AND :dataFim")
    List<Agenda> findByCriancaIdAndPeriodo(
        @Param("criancaId") Long criancaId,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );

    @Query("SELECT a FROM Agenda a WHERE a.crianca.usuario.id = :usuarioId AND " +
           "a.status = :status AND a.dataAgendada = :data")
    List<Agenda> findByUsuarioIdAndStatusAndData(
        @Param("usuarioId") Long usuarioId,
        @Param("status") Agenda.StatusAgenda status,
        @Param("data") LocalDate data
    );

    @Query("SELECT COUNT(a) FROM Agenda a WHERE a.crianca.id = :criancaId AND a.status = 'CONCLUIDA'")
    Long countAtividadesConcluidasByCrianca(@Param("criancaId") Long criancaId);
}
