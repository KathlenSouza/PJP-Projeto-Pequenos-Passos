package com.api.blogAppApi.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.blogAppApi.model.PostComentarioModel;
import com.api.blogAppApi.repository.PostComentarioRepository;

import jakarta.transaction.Transactional;

//regras de negocio podem serem colocadas nessa classe de serviço
//ela serve como uma camada intermediaria entre o controler e repository e
//diminuir o acoplamento

@Service
public class BlogAppServiceImplPostComentario implements BlogAppPostServiceComentarios {
	

	@Autowired
	PostComentarioRepository postComentarioRepository;


	//  TRATA COMENTÁRIOS DOS POSTS
	@Override
	@Transactional
	public PostComentarioModel saveComentario(PostComentarioModel postComentarioModel) {
		// TODO Auto-generated method stub
		return postComentarioRepository.save(postComentarioModel);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Override
//	@Transactional
//	public void deleteComentarios(Iterable<PostComentarioModel> postComentarioModel) {
//		// TODO Auto-generated method stub
//		postComentarioRepository.deleteAll(postComentarioModel);
//	}
//
//	@Override
//	public Iterable<PostComentarioModel> findComentariosByPost(BlogAppPostModel PostModel) { // busca os comentarios de um post
//		// TODO Auto-generated method stub
//		return postComentarioRepository.findByPostModel(PostModel);
//	}

}

//
//@Override
//public Optional<PostComentarioModel> findIdComentario(UUID id) {
//	// TODO Auto-generated method stub
//	return postComentarioRepository.findById(id);
//}


//@Override
//public List<PostComentarioModel> findAllComentarios() {
//	// TODO Auto-generated method stub
//	 return postComentarioRepository.findAll();	
//}

