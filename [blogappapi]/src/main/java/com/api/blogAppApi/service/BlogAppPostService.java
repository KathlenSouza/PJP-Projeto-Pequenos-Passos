package com.api.blogAppApi.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.api.blogAppApi.model.BlogAppPostModel;


public interface BlogAppPostService {
	
	List<BlogAppPostModel> findAll(); // vai retornar uma lista de Posts 
	
	Optional<BlogAppPostModel> findById(UUID id); // vai retornar um unico Post passando o id
	
	BlogAppPostModel save(BlogAppPostModel post); // vai salvar um Post no Banco
	
	void delete(BlogAppPostModel post); // vai excluir um post
	
}
