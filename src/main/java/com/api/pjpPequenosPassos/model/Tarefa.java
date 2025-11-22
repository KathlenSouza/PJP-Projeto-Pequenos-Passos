package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AreaDesenvolvimento areaDesenvolvimento;

    @Column(nullable = false, length = 60)
    private String categoria;

    @Column(nullable = false)
    private int faixaEtariaMin;

    @Column(nullable = false)
    private int faixaEtariaMax;

    @Column(length = 20)
    private String nivelDificuldade;

    @Column(nullable = false)
    private int duracaoEstimadaMinutos;

    @Column(length = 255)
    private String materiaisNecessarios;

    @Column(columnDefinition = "TEXT")
    private String beneficios;

    @Column(nullable = false)
    private boolean ativo = true;

    // 🔵 ADICIONAR RELACIONAMENTO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crianca_id")
    private Crianca crianca;

    
    // ==================== CONSTRUTORES ====================
    public Tarefa() {}

    public Tarefa(Long id, String titulo, String descricao, AreaDesenvolvimento areaDesenvolvimento,
                  String categoria, int faixaEtariaMin, int faixaEtariaMax,
                  String nivelDificuldade, int duracaoEstimadaMinutos,
                  String materiaisNecessarios, String beneficios, boolean ativo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.areaDesenvolvimento = areaDesenvolvimento;
        this.categoria = categoria;
        this.faixaEtariaMin = faixaEtariaMin;
        this.faixaEtariaMax = faixaEtariaMax;
        this.nivelDificuldade = nivelDificuldade;
        this.duracaoEstimadaMinutos = duracaoEstimadaMinutos;
        this.materiaisNecessarios = materiaisNecessarios;
        this.beneficios = beneficios;
        this.ativo = ativo;
    }

    // ==================== BUILDER MANUAL ====================
    public static class Builder {
        private Long id;
        private String titulo;
        private String descricao;
        private AreaDesenvolvimento areaDesenvolvimento;
        private String categoria;
        private int faixaEtariaMin;
        private int faixaEtariaMax;
        private String nivelDificuldade;
        private int duracaoEstimadaMinutos;
        private String materiaisNecessarios;
        private String beneficios;
        private boolean ativo = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder titulo(String titulo) { this.titulo = titulo; return this; }
        public Builder descricao(String descricao) { this.descricao = descricao; return this; }
        public Builder areaDesenvolvimento(AreaDesenvolvimento area) { this.areaDesenvolvimento = area; return this; }
        public Builder categoria(String categoria) { this.categoria = categoria; return this; }
        public Builder faixaEtariaMin(int faixaEtariaMin) { this.faixaEtariaMin = faixaEtariaMin; return this; }
        public Builder faixaEtariaMax(int faixaEtariaMax) { this.faixaEtariaMax = faixaEtariaMax; return this; }
        public Builder nivelDificuldade(String nivel) { this.nivelDificuldade = nivel; return this; }
        public Builder duracaoEstimadaMinutos(int minutos) { this.duracaoEstimadaMinutos = minutos; return this; }
        public Builder materiaisNecessarios(String materiais) { this.materiaisNecessarios = materiais; return this; }
        public Builder beneficios(String beneficios) { this.beneficios = beneficios; return this; }
        public Builder ativo(boolean ativo) { this.ativo = ativo; return this; }

        public Tarefa build() {
            return new Tarefa(id, titulo, descricao, areaDesenvolvimento, categoria,
                    faixaEtariaMin, faixaEtariaMax, nivelDificuldade,
                    duracaoEstimadaMinutos, materiaisNecessarios, beneficios, ativo);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // ==================== GETTERS E SETTERS ====================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public AreaDesenvolvimento getAreaDesenvolvimento() { return areaDesenvolvimento; }
    public void setAreaDesenvolvimento(AreaDesenvolvimento areaDesenvolvimento) { this.areaDesenvolvimento = areaDesenvolvimento; }

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
    

    // ==================== ENUM DE ÁREAS DE DESENVOLVIMENTO ====================
    public enum AreaDesenvolvimento {
        MOTOR_GROSSO,
        MOTOR_FINO,
        LINGUAGEM,
        COGNITIVO,
        SOCIAL
    }
}
