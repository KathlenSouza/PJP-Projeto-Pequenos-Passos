package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "agendas")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(length = 255)
    private String descricao;

    @Column(length = 500)
    private String notas;

    @Column(name = "data_agendada", nullable = false)
    private LocalDate dataAgendada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgenda status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crianca_id", nullable = false)
    private Crianca crianca;

    public Agenda() {}

    public Agenda(Long id, String titulo, String descricao, String notas,
                  LocalDate dataAgendada, StatusAgenda status, Crianca crianca) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.notas = notas;
        this.dataAgendada = dataAgendada;
        this.status = status;
        this.crianca = crianca;
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public LocalDate getDataAgendada() { return dataAgendada; }
    public void setDataAgendada(LocalDate dataAgendada) { this.dataAgendada = dataAgendada; }

    public StatusAgenda getStatus() { return status; }
    public void setStatus(StatusAgenda status) { this.status = status; }

    public Crianca getCrianca() { return crianca; }
    public void setCrianca(Crianca crianca) { this.crianca = crianca; }

    public enum StatusAgenda {
        PENDENTE, CONCLUIDA, CANCELADA
    }
}