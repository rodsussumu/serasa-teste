package com.rodsussumu.experian.controllers;

import com.rodsussumu.experian.dtos.AuthorAddRequestDTO;
import com.rodsussumu.experian.dtos.AuthorResponseDTO;
import com.rodsussumu.experian.dtos.BookCreateRequestDTO;
import com.rodsussumu.experian.dtos.BookListResponseDTO;
import com.rodsussumu.experian.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookListResponseDTO> addAuthor(@RequestBody BookCreateRequestDTO bookCreateRequestDTO) {
        return bookService.addBook(bookCreateRequestDTO);
    }

    @GetMapping
    public ResponseEntity<List<BookListResponseDTO>> findAll() {
        return bookService.listAll();
    }
}
