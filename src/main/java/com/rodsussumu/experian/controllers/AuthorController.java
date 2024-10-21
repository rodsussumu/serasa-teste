package com.rodsussumu.experian.controllers;

import com.rodsussumu.experian.dtos.AuthorAddRequestDTO;
import com.rodsussumu.experian.dtos.AuthorResponseDTO;
import com.rodsussumu.experian.dtos.AuthorResponseList;
import com.rodsussumu.experian.models.Author;
import com.rodsussumu.experian.services.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDTO> addAuthor(@RequestBody AuthorAddRequestDTO authorAddRequestDTO) {
        return authorService.addAuthor(authorAddRequestDTO);
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseList>> getAllAuthor() {
        return authorService.listAllAuthor();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorAddRequestDTO authorAddRequestDTO) {
        return authorService.updateAuthor(id, authorAddRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id) {
        return authorService.deleteAuthor(id);
    }
}
