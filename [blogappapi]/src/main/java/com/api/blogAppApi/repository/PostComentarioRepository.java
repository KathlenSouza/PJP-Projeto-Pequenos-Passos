package com.api.blogAppApi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.blogAppApi.model.BlogAppPostModel;
import com.api.blogAppApi.model.PostComentarioModel;


public interface PostComentarioRepository extends JpaRepository<PostComentarioModel, UUID> {
	
	Iterable<PostComentarioModel> findByPostModel(BlogAppPostModel PostModel);

}

