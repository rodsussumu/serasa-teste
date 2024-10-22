package com.rodsussumu.experian.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodsussumu.experian.dtos.*;
import com.rodsussumu.experian.models.Author;
import com.rodsussumu.experian.models.Book;
import com.rodsussumu.experian.repositories.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .quantity(bookCreateRequestDTO.quantity())
                .release(bookCreateRequestDTO.release_year())
                .build();

        bookRepository.save(book);

        BookAuthorResponseDTO responseMap = objectMapper.convertValue(author, BookAuthorResponseDTO.class);


        BookListResponseDTO responseDTO = new BookListResponseDTO(
                book.getId(),
                book.getGenre(),
                book.getRelease(),
                book.getTitle(),
                book.getQuantity(),
                responseMap
        );

        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<List<BookListResponseDTO>> listAll() {
        List<Book> books = bookRepository.findAll();

        List<BookListResponseDTO> postDtoList = books.stream().map(book -> {
            BookAuthorResponseDTO authorDTO = objectMapper.convertValue(book.getAuthor(), BookAuthorResponseDTO.class);
            return new BookListResponseDTO(
                    book.getId(),
                    book.getGenre(),
                    book.getRelease(),
                    book.getTitle(),
                    book.getQuantity(),
                    authorDTO
            );
        }).toList();

        return ResponseEntity.ok(postDtoList);
    }

    public ResponseEntity<BookListResponseDTO> updateQuantity(Long id, int quantity) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
        book.setQuantity(quantity);
        BookListResponseDTO bookListResponseDTO = objectMapper.convertValue(book, BookListResponseDTO.class);
        return ResponseEntity.ok(bookListResponseDTO);
    }
}
