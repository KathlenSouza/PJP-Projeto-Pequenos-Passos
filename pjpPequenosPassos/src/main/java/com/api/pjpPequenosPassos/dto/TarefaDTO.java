package com.api.pjpPequenosPassos.dto;

import com.api.pjpPequenosPassos.model.Tarefa;

public class TarefaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Tarefa.AreaDesenvolvimento areaDesenvolvimento;
    private String categoria;
    private int faixaEtariaMin;
    private int faixaEtariaMax;
    private String nivelDificuldade;
    private int duracaoEstimadaMinutos;
    private String materiaisNecessarios;
    private String beneficios;
    private boolean ativo;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Tarefa.AreaDesenvolvimento getAreaDesenvolvimento() { return areaDesenvolvimento; }
    public void setAreaDesenvolvimento(Tarefa.AreaDesenvolvimento areaDesenvolvimento) { this.areaDesenvolvimento = areaDesenvolvimento; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getFaixaEtariaMin() { return faixaEtariaMin; }
    public void setFaixaEtariaMin(int faixaEtariaMin) { this.faixaEtariaMin = faixaEtariaMin; }

    public int getFaixaEtariaMax() { return faixaEtariaMax; }
    public void setFaixaEtariaMax(int faixaEtariaMax) { this.faixaEtariaMax = faixaEtariaMax; }

    public String getNivelDificuldade() { return nivelDificuldade; }
    public void setNivelDificuldade(String nivelDificuldade) { this.nivelDificuldade = nivelDificuldade; }

    public int getDuracaoEstimadaMinutos() { return duracaoEstimadaMinutos; }
    public void setDuracaoEstimadaMinutos(int duracaoEstimadaMinutos) { this.duracaoEstimadaMinutos = duracaoEstimadaMinutos; }

    public String getMateriaisNecessarios() { return materiaisNecessarios; }
    public void setMateriaisNecessarios(String materiaisNecessarios) { this.materiaisNecessarios = materiaisNecessarios; }

    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}

