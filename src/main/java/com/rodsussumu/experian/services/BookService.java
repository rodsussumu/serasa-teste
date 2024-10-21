package com.rodsussumu.experian.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodsussumu.experian.dtos.BookCreateRequestDTO;
import com.rodsussumu.experian.dtos.BookListResponseDTO;
import com.rodsussumu.experian.models.Author;
import com.rodsussumu.experian.models.Book;
import com.rodsussumu.experian.repositories.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private BookRepository bookRepository;
    private AuthorService authorService;
    private ObjectMapper objectMapper;

    public BookService(BookRepository bookRepository, AuthorService authorService, ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<BookListResponseDTO> addBook(BookCreateRequestDTO bookCreateRequestDTO) {
        Author author = authorService.listById(bookCreateRequestDTO.author_id());

        Book book = Book.builder()
                .author(author)
                .genre(bookCreateRequestDTO.genre())
                .title(bookCreateRequestDTO.title())
                .releaseYear(bookCreateRequestDTO.release_year())
                .build();

        bookRepository.save(book);

        BookListResponseDTO responseMap = objectMapper.convertValue(book, BookListResponseDTO.class);

        return ResponseEntity.ok(responseMap);
    }
}
