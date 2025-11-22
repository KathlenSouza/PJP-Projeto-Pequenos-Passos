package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.AvaliacaoRisco;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AvaliacaoRiscoRepository extends JpaRepository<AvaliacaoRisco, UUID> {

    List<AvaliacaoRisco> findByCriancaId(Long criancaId);

    List<AvaliacaoRisco> findByCriancaIdOrderByIdDesc(Long criancaId);
}
