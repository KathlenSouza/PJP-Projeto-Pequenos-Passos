package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.RecursoPedagogico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecursoPedagogicoRepository extends JpaRepository<RecursoPedagogico, UUID> {}
