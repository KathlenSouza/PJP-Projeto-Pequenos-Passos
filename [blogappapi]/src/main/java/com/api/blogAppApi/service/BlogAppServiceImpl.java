package com.api.blogAppApi.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.blogAppApi.model.BlogAppPostModel;
import com.api.blogAppApi.repository.BlogAppPostRepository;

import jakarta.transaction.Transactional;

//regras de negocio podem serem colocadas nessa classe de serviço
//ela serve como uma camada intermediaria entre o controler e repository e
//diminuir o acoplamento

@Service
public class BlogAppServiceImpl implements BlogAppPostService {
	
	@Autowired
	BlogAppPostRepository blogapprepository;
	

	@Override
	public List<BlogAppPostModel> findAll() {
		// TODO Auto-generated method stub
		return blogapprepository.findAll();
	}
	
	@Override
	public Optional<BlogAppPostModel> findById(UUID id) {
		// TODO Auto-generated method stub
		return blogapprepository.findById(id);
	}

	@Override
	@Transactional
	public BlogAppPostModel save(BlogAppPostModel post) {
		// TODO Auto-generated method stub
		return blogapprepository.save(post);
	}

	@Override
	@Transactional
	public void delete(BlogAppPostModel post) {
		// TODO Auto-generated method stub
		blogapprepository.delete(post);	
	}
	}

