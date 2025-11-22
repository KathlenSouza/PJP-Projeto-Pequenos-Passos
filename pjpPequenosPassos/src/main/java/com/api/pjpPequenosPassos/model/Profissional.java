package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_profissional")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profissional implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nome;

    private String area;

    private String cidade;

    private String contato;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    private Integer avaliacao;

    private boolean indicadoPorPais; // 👈 NOVO

    // Getters e Setters manuais (Lombok manual)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Integer getAvaliacao() { return avaliacao; }
    public void setAvaliacao(Integer avaliacao) { this.avaliacao = avaliacao; }

    public boolean isIndicadoPorPais() { return indicadoPorPais; }
    public void setIndicadoPorPais(boolean indicadoPorPais) { this.indicadoPorPais = indicadoPorPais; }
}
