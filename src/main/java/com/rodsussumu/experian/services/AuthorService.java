package com.rodsussumu.experian.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodsussumu.experian.dtos.AuthorAddRequestDTO;
import com.rodsussumu.experian.dtos.AuthorResponseDTO;
import com.rodsussumu.experian.dtos.AuthorResponseList;
import com.rodsussumu.experian.models.Author;
import com.rodsussumu.experian.repositories.AuthorRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;

    private ObjectMapper objectMapper;

    public AuthorService(AuthorRepository authorRepository, ObjectMapper objectMapper) {
        this.authorRepository = authorRepository;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<AuthorResponseDTO> addAuthor(AuthorAddRequestDTO authorAddRequestDTO) {
        Optional<Author> author = authorRepository.findByName(authorAddRequestDTO.name());

        if(author.isPresent()) {
            throw new DuplicateKeyException("Author already registered!");
        }

        Author newAuthor = Author.builder().name(authorAddRequestDTO.name()).nationality(authorAddRequestDTO.nationality()).build();

        authorRepository.save(newAuthor);

        return ResponseEntity.ok(new AuthorResponseDTO(newAuthor.getName(), newAuthor.getNationality()));
    }

    public ResponseEntity<List<AuthorResponseList>> listAllAuthor() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorResponseList> postDtoList = authors.stream().map(author -> objectMapper.convertValue(author, AuthorResponseList.class)).toList();

        return ResponseEntity.ok(postDtoList);
    }

    public ResponseEntity<AuthorResponseDTO> updateAuthor(Long id, AuthorAddRequestDTO authorDTO) {
        Author author = listById(id);

        author.setName(authorDTO.name());
        author.setNationality(authorDTO.nationality());

        authorRepository.save(author);

        AuthorResponseDTO responseMap = objectMapper.convertValue(author, AuthorResponseDTO.class);

        return ResponseEntity.ok(responseMap);
    }

    public ResponseEntity<?> deleteAuthor(Long id) {
        Author author = listById(id);

        authorRepository.delete(author);

        return ResponseEntity.noContent().build();
    }

    public Author listById(Long id) {
       return authorRepository.findById(id)
               .orElseThrow(() -> new IllegalArgumentException("Author not found with id: " + id));

    }
}
