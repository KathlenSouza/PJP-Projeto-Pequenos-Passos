package com.api.pjpPequenosPassos.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class HistoricoAtividadeDTO {

    private UUID id;
    private Long criancaId;
    private LocalDateTime dataConclusao;

    private String tarefaTitulo;
    private String descricao;
    private String categoria;
    private String areaDesenvolvimento;

    private String observacao;

    private String status;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Long getCriancaId() {
		return criancaId;
	}

	public void setCriancaId(Long criancaId) {
		this.criancaId = criancaId;
	}

	public LocalDateTime getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(LocalDateTime dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public String getTarefaTitulo() {
		return tarefaTitulo;
	}

	public void setTarefaTitulo(String tarefaTitulo) {
		this.tarefaTitulo = tarefaTitulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getAreaDesenvolvimento() {
		return areaDesenvolvimento;
	}

	public void setAreaDesenvolvimento(String areaDesenvolvimento) {
		this.areaDesenvolvimento = areaDesenvolvimento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    // Getters e Setters...
    
}

