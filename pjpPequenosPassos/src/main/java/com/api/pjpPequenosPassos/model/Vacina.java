package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacinas")
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long criancaId;

    private String nome;

    private LocalDate dataAplicacao;

    public Vacina() {}

    public Vacina(Long criancaId, String nome, LocalDate dataAplicacao) {
        this.criancaId = criancaId;
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
    }

    public Long getId() { return id; }
    public Long getCriancaId() { return criancaId; }
    public String getNome() { return nome; }
    public LocalDate getDataAplicacao() { return dataAplicacao; }

    public void setId(Long id) { this.id = id; }
    public void setCriancaId(Long criancaId) { this.criancaId = criancaId; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }
}
