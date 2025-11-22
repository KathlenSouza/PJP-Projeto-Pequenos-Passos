package com.api.pjpPequenosPassos.dto;

import java.util.List;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;

    private List<CriancaDTO> criancas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<CriancaDTO> getCriancas() {
        return criancas;
    }

    public void setCriancas(List<CriancaDTO> criancas) {
        this.criancas = criancas;
    }
}
