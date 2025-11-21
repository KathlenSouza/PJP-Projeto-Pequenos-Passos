package com.api.pjpPequenosPassos.dto;

public class CadastroCompletoDTO {

    private UsuarioDTO usuario;
    private CriancaDTO crianca;

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public CriancaDTO getCrianca() {
        return crianca;
    }

    public void setCrianca(CriancaDTO crianca) {
        this.crianca = crianca;
    }
}
