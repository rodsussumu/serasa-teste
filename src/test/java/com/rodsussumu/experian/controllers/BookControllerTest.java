package com.rodsussumu.experian.controllers;

import com.rodsussumu.experian.dtos.BookCreateRequestDTO;
import com.rodsussumu.experian.dtos.BookListResponseDTO;
import com.rodsussumu.experian.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddBook() {
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO("Drama", "2024", "Livro teste", 1L);
        BookListResponseDTO bookResponseDTO = new BookListResponseDTO(1L, "Drama", "2024", "Livro teste", null);

        Mockito.when(bookService.addBook(bookCreateRequestDTO)).thenReturn(ResponseEntity.ok(bookResponseDTO));

        ResponseEntity<BookListResponseDTO> response = bookController.addAuthor(bookCreateRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookListResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.id());
        assertEquals("Drama", responseBody.genre());
        assertEquals("2024", responseBody.release());
        assertEquals("Livro teste", responseBody.title());
    }

    @Test
    void shouldFindAllBooks() {
        BookListResponseDTO book1 = new BookListResponseDTO(1L, "Drama", "2024", "Livro teste", null);
        BookListResponseDTO book2 = new BookListResponseDTO(2L, "Fiction", "2023", "Outro livro", null);
        List<BookListResponseDTO> books = List.of(book1, book2);

        Mockito.when(bookService.listAll()).thenReturn(ResponseEntity.ok(books));

        ResponseEntity<List<BookListResponseDTO>> response = bookController.findAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BookListResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        assertEquals(1L, responseBody.get(0).id());
        assertEquals("Drama", responseBody.get(0).genre());
        assertEquals("2024", responseBody.get(0).release());
        assertEquals("Livro teste", responseBody.get(0).title());

        assertEquals(2L, responseBody.get(1).id());
        assertEquals("Fiction", responseBody.get(1).genre());
        assertEquals("2023", responseBody.get(1).release());
        assertEquals("Outro livro", responseBody.get(1).title());
    }
}