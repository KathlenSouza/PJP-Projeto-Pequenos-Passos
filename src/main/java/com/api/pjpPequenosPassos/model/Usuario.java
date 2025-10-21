package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nomeCompleto;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(length = 20)
    private String telefone;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    // Relacionamentos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Crianca> criancas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Notificacao> notificacoes = new ArrayList<>();

    // Métodos auxiliares
    public void adicionarCrianca(Crianca crianca) {
        criancas.add(crianca);
        crianca.setUsuario(this);
    }

    public void removerCrianca(Crianca crianca) {
        criancas.remove(crianca);
        crianca.setUsuario(null);
    }

	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	public CharSequence getSenha() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getNomeCompleto() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDataNascimento() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNomeCompleto(Object nomeCompleto2) {
		// TODO Auto-generated method stub
		
	}

	public void setDataNascimento(Object dataNascimento2) {
		// TODO Auto-generated method stub
		
	}

	public Object getTelefone() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTelefone(Object telefone2) {
		// TODO Auto-generated method stub
		
	}

	public Object getCidade() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCidade(Object cidade2) {
		// TODO Auto-generated method stub
		
	}

	public Object getEstado() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setEstado(Object estado2) {
		// TODO Auto-generated method stub
		
	}

	public void setSenha(String encode) {
		// TODO Auto-generated method stub
		
	}

	public void setAtivo(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public boolean getAtivo() {
		// TODO Auto-generated method stub
		return false;
	}
}