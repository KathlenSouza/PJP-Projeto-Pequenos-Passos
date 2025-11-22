package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    List<Vacina> findByCriancaId(Long criancaId);
    
}
