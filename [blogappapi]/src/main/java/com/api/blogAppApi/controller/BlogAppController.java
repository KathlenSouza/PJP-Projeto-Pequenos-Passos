package com.api.blogAppApi.controller;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.blogAppApi.dtos.BlogAppRecordDto;
import com.api.blogAppApi.dtos.BlogAppRecordPostComentarioDto;
import com.api.blogAppApi.model.BlogAppPostModel;
import com.api.blogAppApi.model.PostComentarioModel;
import com.api.blogAppApi.service.BlogAppPostService;
import com.api.blogAppApi.service.BlogAppPostServiceComentarios;

import jakarta.validation.Valid;


@RestController
@CrossOrigin(origins = "http://localhost:3000") // libera acesso do front
@RequestMapping("/api")
public class BlogAppController {

	@Autowired
	BlogAppPostService blogapppostservice;
	
	@Autowired
	private BlogAppPostServiceComentarios blogAppServicePostComentarios; 
	
	
	// LISTA TODOS OS POSTS
	@GetMapping(value = "/posts")
	public ResponseEntity<List<BlogAppPostModel>> getPosts() {
		List<BlogAppPostModel> posts = blogapppostservice.findAll();
		
		  if (posts.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(posts);
		  } 
		 return ResponseEntity.status(HttpStatus.OK).body(posts);
		
	}
	
	//LISTA DETALHES DE UM POST
	@GetMapping(value = "/posts/{id}")
	public ResponseEntity<Object> getPostDetails(@PathVariable("id") UUID id ) {
		  Optional<BlogAppPostModel> blogappModelOptional = blogapppostservice.findById(id);
		  //BlogAppPostModel post = blogappModelOptional.get(); // ERRO AQUI
		
		  if (!blogappModelOptional.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("blog not found.");
	      }
		  
			 // return ResponseEntity.status(HttpStatus.OK).body(post);  
			  return ResponseEntity.status(HttpStatus.OK).body(blogappModelOptional.get());  
		
	}
	
	// ADICIONA NOVO POST
		@PostMapping(value = "/newpost") // retorna 201
		public ResponseEntity<Object> savePost(@RequestBody @Valid BlogAppRecordDto blogAppRecordDto) {
		
			// Java 10 introduziu o recurso de inferência de tipos para variáveis locais com a palavra
			// reservada VAR
		    var postModel = new BlogAppPostModel();
		    BeanUtils.copyProperties(blogAppRecordDto, postModel);
		    postModel.setData(LocalDate.now(ZoneId.of("UTC")));
		    return ResponseEntity.status(HttpStatus.CREATED).body(blogapppostservice.save(postModel));

		}
		
		// EDITA UM POST	
		@PutMapping("/editapost/{id}")
		    public ResponseEntity<Object> updatePost(@PathVariable(value = "id") UUID id,
		    										@RequestBody @Valid BlogAppRecordDto blogAppRecordDto){
			  
		    	 Optional<BlogAppPostModel> blogappModelOptional = blogapppostservice.findById(id);
			        if (!blogappModelOptional.isPresent()) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("blog not found.");
		        }
			    var postModel = new BlogAppPostModel();
			    BeanUtils.copyProperties(blogAppRecordDto, postModel);
			    postModel.setId(blogappModelOptional.get().getId());
			    postModel.setData(blogappModelOptional.get().getData());
		        return ResponseEntity.status(HttpStatus.OK).body(blogapppostservice.save(postModel));
		    }
		

		// DELETA UM POST
		 @DeleteMapping("/deletapost/{id}")
		    public ResponseEntity<Object> deletarPosts(@PathVariable(value = "id") UUID id){
			//Optional classe implementada no java 8 para evitar erros de nullPointer
		        Optional<BlogAppPostModel> blogappModelOptional = blogapppostservice.findById(id);
		        if (!blogappModelOptional.isPresent()) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("blog not found.");
		        } 
		        blogapppostservice.delete(blogappModelOptional.get());
		        return ResponseEntity.status(HttpStatus.OK).body("POST DELETADO COM SUCESSO.");
		        
		    }
		 
		// ADICIONA COMENTARIO EM UM POST
			@PostMapping(value = "/posts/{id}") // RETORNA 201
		     public ResponseEntity<Object> saveComentarioPost(@PathVariable("id") UUID id, 
					@RequestBody BlogAppRecordPostComentarioDto blogAppRecordPostComentarioDto ) {
				 
				var postComentario = new PostComentarioModel(); 
				
				 Optional<BlogAppPostModel> blogappModelOptional = blogapppostservice.findById(id);
				 BlogAppPostModel post = blogappModelOptional.get();
				 BeanUtils.copyProperties(blogAppRecordPostComentarioDto, postComentario);
				postComentario.setPostModel(post);
				postComentario.setData(LocalDate.now());
					
				return ResponseEntity.status(HttpStatus.CREATED).body(blogAppServicePostComentarios.saveComentario(postComentario));
					
		     }
				
		 
}
