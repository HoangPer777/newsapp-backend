package com.example.newsapp.modules.article.controller;

import com.example.newsapp.modules.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
  private final ArticleRepository repo;

  @GetMapping public Object list(@RequestParam(defaultValue="0") int page,
                                 @RequestParam(defaultValue="10") int size){
    return repo.findAll(PageRequest.of(page, size));
  }

  @GetMapping("/{id}") public Object get(@PathVariable Long id){ return repo.findById(id).orElseThrow(); }

  @GetMapping("/search")
  public Object search(@RequestParam String q,
                       @RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="10") int size) {
    return repo.search(q, PageRequest.of(page, size));
  }
}
