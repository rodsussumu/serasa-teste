package com.rodsussumu.experian.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodsussumu.experian.dtos.AuthorResponseList;
import com.rodsussumu.experian.dtos.BookAuthorResponseDTO;
import com.rodsussumu.experian.dtos.BookCreateRequestDTO;
import com.rodsussumu.experian.dtos.BookListResponseDTO;
import com.rodsussumu.experian.models.Author;
import com.rodsussumu.experian.models.Book;
import com.rodsussumu.experian.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddBook() {
        Long authorId = 1L;
        String genre = "Drama";
        String title = "Livro teste";
        String releaseYear = "2024";

        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(genre, releaseYear, title, authorId);

        Author author = new Author(authorId, "José", "Brasil", null);

        Book book = Book.builder()
                .id(1L)
                .author(author)
                .genre(genre)
                .title(title)
                .release(releaseYear)
                .build();

        BookAuthorResponseDTO authorDTO = new BookAuthorResponseDTO(authorId, "José", "Brasil");

        Mockito.when(authorService.listById(authorId)).thenReturn(author);
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);
        Mockito.when(objectMapper.convertValue(author, BookAuthorResponseDTO.class)).thenReturn(authorDTO);

        ResponseEntity<BookListResponseDTO> response = bookService.addBook(bookCreateRequestDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        BookListResponseDTO actualResponse = response.getBody();
        assertNotNull(actualResponse);

        assertEquals(genre, actualResponse.genre());
        assertEquals(releaseYear, actualResponse.release());
        assertEquals(title, actualResponse.title());
        assertEquals(authorDTO.id(), actualResponse.author().id());
        assertEquals(authorDTO.name(), actualResponse.author().name());
    }

    @Test
    void shouldListAllBooks() {
        Author author1 = new Author(1L, "Teste autor", "Brasil", null);
        Author author2 = new Author(2L, "Teste autor2", "Brasil", null);
        Book book1 = Book.builder()
                .id(1L)
                .author(author1)
                .genre("Drama")
                .title("Livro 1")
                .release("2023")
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .author(author2)
                .genre("Terror")
                .title("Livro 2")
                .release("1994")
                .build();

        List<Book> books = List.of(book1, book2);

        BookAuthorResponseDTO authorDTO1 = new BookAuthorResponseDTO(author1.getId(), author1.getName(), author1.getNationality());
        BookAuthorResponseDTO authorDTO2 = new BookAuthorResponseDTO(author2.getId(), author2.getName(), author2.getNationality());

        BookListResponseDTO responseDTO1 = new BookListResponseDTO(
                book1.getId(),
                book1.getGenre(),
                book1.getRelease(),
                book1.getTitle(),
                authorDTO1
        );
        BookListResponseDTO responseDTO2 = new BookListResponseDTO(
                book2.getId(),
                book2.getGenre(),
                book2.getRelease(),
                book2.getTitle(),
                authorDTO2
        );

        Mockito.when(bookRepository.findAll()).thenReturn(books);
        Mockito.when(objectMapper.convertValue(book1.getAuthor(), BookAuthorResponseDTO.class)).thenReturn(authorDTO1);
        Mockito.when(objectMapper.convertValue(book2.getAuthor(), BookAuthorResponseDTO.class)).thenReturn(authorDTO2);

        ResponseEntity<List<BookListResponseDTO>> response = bookService.listAll();


        assertNotNull(response);
        List<BookListResponseDTO> actualResponseList = response.getBody();
        assertNotNull(actualResponseList);
        assertEquals(2, actualResponseList.size());
    }
}