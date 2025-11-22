package com.api.pjpPequenosPassos.dto;

import java.time.LocalDate;

public class VacinaDTO {

    private Long id;
    private Long criancaId;
    private String nome;
    private LocalDate dataAplicacao;

    public Long getId() { return id; }
    public Long getCriancaId() { return criancaId; }
    public String getNome() { return nome; }
    public LocalDate getDataAplicacao() { return dataAplicacao; }

    public void setId(Long id) { this.id = id; }
    public void setCriancaId(Long criancaId) { this.criancaId = criancaId; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }
}
