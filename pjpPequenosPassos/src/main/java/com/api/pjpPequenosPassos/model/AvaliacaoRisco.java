package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_avaliacao_risco")
public class AvaliacaoRisco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String tipoRisco;

    @Column(columnDefinition = "text")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "crianca_id", nullable = false)
    private Crianca crianca;

    public AvaliacaoRisco() {}

    // ---------- Getters e Setters ----------

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTipoRisco() { return tipoRisco; }
    public void setTipoRisco(String tipoRisco) { this.tipoRisco = tipoRisco; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Crianca getCrianca() { return crianca; }
    public void setCrianca(Crianca crianca) { this.crianca = crianca; }
}
