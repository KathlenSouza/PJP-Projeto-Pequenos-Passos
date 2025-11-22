package com.api.pjpPequenosPassos.dto;

public class DiarioRequest {
    private Long criancaId;
    private String descricao;
    private String emocao;

    
    public Long getCriancaId() { return criancaId; }
    public void setCriancaId(Long criancaId) { this.criancaId = criancaId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getEmocao() { return emocao; }
    public void setEmocao(String emocao) { this.emocao = emocao; }
}
