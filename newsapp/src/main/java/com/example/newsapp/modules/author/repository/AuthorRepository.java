package com.example.newsapp.modules.author.repository;

import com.example.newsapp.modules.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
