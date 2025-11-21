package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, UUID> {

    // Buscar relatórios por tipo
    List<Relatorio> findByTipo(String tipo);

    // Buscar relatórios por usuário
    List<Relatorio> findByUsuarioId(Long usuarioId);
}
