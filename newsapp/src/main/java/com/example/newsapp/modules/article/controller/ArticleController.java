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
//    @GetMapping("/{slug}")
//    public Object get(@PathVariable String slug) {
//        return repo.findBySlug(slug)
//                .orElseThrow();
//    }
@GetMapping("/{value}")
public Object get(@PathVariable String value) {
    try {
        Long id = Long.parseLong(value);
        return repo.findById(id).orElseThrow();
    } catch (NumberFormatException e) {
        return repo.findBySlug(value).orElseThrow();
    }
}

//  @GetMapping("/{id}") public Object get(@PathVariable Long id){ return repo.findById(id).orElseThrow(); }
//@GetMapping("/{id}")
//public Object get(@PathVariable Long id){
//    return repo.findById(id)
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));
//}
  @GetMapping("/search")
  public Object search(@RequestParam String q,
                       @RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="10") int size) {
    return repo.search(q, PageRequest.of(page, size));
  }
}
