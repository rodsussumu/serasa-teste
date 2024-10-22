package com.rodsussumu.experian.controllers;

import com.rodsussumu.experian.dtos.AuthorAddRequestDTO;
import com.rodsussumu.experian.dtos.AuthorResponseDTO;
import com.rodsussumu.experian.dtos.BookCreateRequestDTO;
import com.rodsussumu.experian.dtos.BookListResponseDTO;
import com.rodsussumu.experian.services.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Tag(name = "Book")
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

    @PatchMapping("/{id}")
    public ResponseEntity<BookListResponseDTO> updateQuantity(@RequestParam Long id, @RequestBody int quantity){
        return bookService.updateQuantity(id, quantity);
    }
}
