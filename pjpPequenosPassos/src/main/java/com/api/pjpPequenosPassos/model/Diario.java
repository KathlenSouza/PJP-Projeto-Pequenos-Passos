package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "diario")
public class Diario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emocao;

    @Column(length = 1000)
    private String descricao;

    @Column(name = "data_registro")
    private LocalDate dataRegistro = LocalDate.now();

    private Boolean ativo = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "diario_fotos", joinColumns = @JoinColumn(name = "diario_id"))
    @Column(name = "fotos", columnDefinition = "LONGTEXT")
    private List<String> fotos = new ArrayList<>();

    // 🔵 ADICIONAR ISSO
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "crianca_id", nullable = false)
    private Crianca crianca;
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmocao() {
		return emocao;
	}

	public void setEmocao(String emocao) {
		this.emocao = emocao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(LocalDate dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public List<String> getFotos() {
		return fotos;
	}

	public void setFotos(List<String> fotos) {
		this.fotos = fotos;
	}

	public Crianca getCrianca() {
		return crianca;
	}

	public void setCrianca(Crianca crianca) {
		this.crianca = crianca;
	}

}
