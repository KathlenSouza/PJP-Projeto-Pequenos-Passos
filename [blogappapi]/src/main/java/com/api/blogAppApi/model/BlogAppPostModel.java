package com.api.blogAppApi.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import com.api.blogAppApi.model.PostComentarioModel;

@Entity
@Table(name="TB_POST")
public class BlogAppPostModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	//anotação para geração do id no banco automático
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;
	@Column(nullable = false, length = 70)
	private String autor;
	@Column(nullable = false)
	private LocalDate data;
	@Column(nullable = false, length = 70)
	private String titulo;
	@Lob // para o banco aceitar texto grande e ter boa performace
	@Column(columnDefinition="text")
	private String texto;
	@OneToMany
	private List<PostComentarioModel> postComentarioModel;
	
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public List<PostComentarioModel> getPostComentarioModel() {
		return postComentarioModel;
	}
	public void setPostComentarioModel(List<PostComentarioModel> postComentarioModel) {
		this.postComentarioModel = postComentarioModel;
	}

}
