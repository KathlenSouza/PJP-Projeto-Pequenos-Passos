package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.Diario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiarioRepository extends JpaRepository<Diario, Long> {

    List<Diario> findByAtivoTrueOrderByDataRegistroDesc();
    // Buscar por um único dia
    List<Diario> findByDataRegistro(LocalDate data);

    // Buscar por intervalo de dias
    List<Diario> findByDataRegistroBetween(LocalDate inicio, LocalDate fim);
    
    List<Diario> findByCriancaId(Long criancaId);

}
