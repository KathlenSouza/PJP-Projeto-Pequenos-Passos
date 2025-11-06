package com.api.blogAppApi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.blogAppApi.model.BlogAppPostModel;

public interface BlogAppPostRepository extends JpaRepository<BlogAppPostModel, UUID> {

}
