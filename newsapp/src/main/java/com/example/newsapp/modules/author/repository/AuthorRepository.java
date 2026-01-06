package com.example.newsapp.modules.author.repository;

import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByUser(User user);
}
