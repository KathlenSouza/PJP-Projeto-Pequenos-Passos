package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnexoRepository extends JpaRepository<Anexo, Long> {
    List<Anexo> findByDiarioId(Long diarioId);
    List<Anexo> findByTipoAnexo(Anexo.TipoAnexo tipo);
}
