package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_historico_tarefa")
public class HistoricoAtividade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime dataConclusao;

    @Column(columnDefinition = "text")
    private String observacao;

    // ID da criança que concluiu a atividade
    @Column(name = "crianca_id", nullable = false)
    private Long criancaId;

    @ManyToOne
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    public HistoricoAtividade() {}

    public HistoricoAtividade(UUID id, LocalDateTime dataConclusao, String observacao,
                              Long criancaId, Tarefa tarefa) {
        this.id = id;
        this.dataConclusao = dataConclusao;
        this.observacao = observacao;
        this.criancaId = criancaId;
        this.tarefa = tarefa;
    }

    // =============== GETTERS E SETTERS ===============

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Long getCriancaId() { return criancaId; }
    public void setCriancaId(Long criancaId) { this.criancaId = criancaId; }

    public Tarefa getTarefa() { return tarefa; }
    public void setTarefa(Tarefa tarefa) { this.tarefa = tarefa; }

    @Override
    public String toString() {
        return "HistoricoAtividade{" +
                "id=" + id +
                ", dataConclusao=" + dataConclusao +
                ", observacao='" + observacao + '\'' +
                ", criancaId=" + criancaId +
                ", tarefa=" + (tarefa != null ? tarefa.getTitulo() : "null") +
                '}';
    }
}
