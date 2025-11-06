package com.api.blogAppApi.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name="TB_POSTCOMENTARIO")
public class PostComentarioModel {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate data;

	@Lob
	@Column(columnDefinition="text")
	private String comentario;
	
	@ManyToOne
	private BlogAppPostModel postModel;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public BlogAppPostModel getPostModel() {
		return postModel;
	}

	public void setPostModel(BlogAppPostModel postModel) {
		this.postModel = postModel;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	

}
