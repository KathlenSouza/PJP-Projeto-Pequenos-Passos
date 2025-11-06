package com.api.blogAppApi.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.api.blogAppApi.model.BlogAppPostModel;
import com.api.blogAppApi.model.PostComentarioModel;




public interface BlogAppPostServiceComentarios {
	
	//TRATA COMENTÁRIOS DOS POSTS
	
	PostComentarioModel saveComentario(PostComentarioModel postComentarioModel); //vai salvar um Post no Banco
	
	
	
	
	
	
	
//	
//	Iterable<PostComentarioModel> findComentariosByPost(BlogAppPostModel PostModel);// vai retornar os comentários de um post
//	
//	void deleteComentarios( Iterable<PostComentarioModel> postComentarioModel);  // vai excluir um post

}


