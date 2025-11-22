package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByAtivoTrue();
    List<Tarefa> findByAreaDesenvolvimento(Tarefa.AreaDesenvolvimento area);

    @Query("SELECT t FROM Tarefa t WHERE t.ativo = true AND " +
           "t.faixaEtariaMin <= :idade AND t.faixaEtariaMax >= :idade")
    List<Tarefa> findByFaixaEtaria(@Param("idade") int idade);

    @Query("SELECT t FROM Tarefa t WHERE t.ativo = true AND " +
           "t.areaDesenvolvimento = :area AND " +
           "t.faixaEtariaMin <= :idade AND t.faixaEtariaMax >= :idade")
    List<Tarefa> findByAreaAndFaixaEtaria(
        @Param("area") Tarefa.AreaDesenvolvimento area,
        @Param("idade") int idade
    );
    
    // 🟢 Lista tarefas pendentes por criança
    List<Tarefa> findByCriancaIdAndAtivoTrue(Long criancaId);
    
}
