package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacoes_risco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoRisco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data da avaliação é obrigatória")
    @Column(nullable = false)
    private LocalDate dataAvaliacao;

    @NotNull(message = "Área avaliada é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Tarefa.AreaDesenvolvimento areaAvaliada;

    @NotNull(message = "Nível de risco é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelRisco nivelRisco;

    @Column(nullable = false)
    @Builder.Default
    private Integer pontuacao = 0;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(columnDefinition = "TEXT")
    private String recomendacoes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean alertaEnviado = false;

    @Column
    private LocalDateTime dataAlerta;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    // Relacionamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crianca_id", nullable = false)
    private Crianca crianca;

    // Enum para nível de risco
    public enum NivelRisco {
        BAIXO("Baixo", "Desenvolvimento adequado"),
        MODERADO("Moderado", "Atenção necessária"),
        ALTO("Alto", "Intervenção recomendada"),
        CRITICO("Crítico", "Buscar profissional urgentemente");

        private final String descricao;
        private final String acao;

        NivelRisco(String descricao, String acao) {
            this.descricao = descricao;
            this.acao = acao;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getAcao() {
            return acao;
        }
    }

    // Métodos auxiliares
    public void enviarAlerta() {
        this.alertaEnviado = true;
        this.dataAlerta = LocalDateTime.now();
    }

    public boolean precisaBuscarProfissional() {
        return this.nivelRisco == NivelRisco.ALTO || this.nivelRisco == NivelRisco.CRITICO;
    }
}