package com.rodsussumu.experian.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodsussumu.experian.dtos.AuthorAddRequestDTO;
import com.rodsussumu.experian.dtos.AuthorResponseDTO;
import com.rodsussumu.experian.dtos.AuthorResponseList;
import com.rodsussumu.experian.models.Author;
import com.rodsussumu.experian.repositories.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddAuthor() {
        AuthorAddRequestDTO authorAddRequestDTO = new AuthorAddRequestDTO("José", "Brasil");
        Author newAuthor = new Author(1L, "José", "Brasil", null);

        Mockito.when(authorRepository.findByName(authorAddRequestDTO.name())).thenReturn(Optional.empty());
        Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenReturn(newAuthor);

        ResponseEntity<AuthorResponseDTO> response = authorService.addAuthor(authorAddRequestDTO);

        assertNotNull(response);

        AuthorResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("José", responseBody.name());
        assertEquals("Brasil", responseBody.nationality());
    }

    @Test
    void shouldNotAddWhenAddingSameAuthor() {
        AuthorAddRequestDTO authorAddRequestDTO = new AuthorAddRequestDTO("José", "Brasil");
        Author existingAuthor = new Author(1L, "José", "Brasil", null);

        Mockito.when(authorRepository.findByName(authorAddRequestDTO.name())).thenReturn(Optional.of(existingAuthor));

        assertThrows(DuplicateKeyException.class, () -> authorService.addAuthor(authorAddRequestDTO));
    }

    @Test
    void shouldListAllAuthors() {
        Author author1 = new Author(1L, "José", "Brasil", null);
        Author author2 = new Author(2L, "Maria", "Portugal", null);
        List<Author> authors = List.of(author1, author2);

        AuthorResponseList responseList1 = new AuthorResponseList(1L, "José", "Brasil", null);
        AuthorResponseList responseList2 = new AuthorResponseList(2L, "Maria", "Portugal", null);

        Mockito.when(authorRepository.findAll()).thenReturn(authors);
        Mockito.when(objectMapper.convertValue(author1, AuthorResponseList.class)).thenReturn(responseList1);
        Mockito.when(objectMapper.convertValue(author2, AuthorResponseList.class)).thenReturn(responseList2);

        ResponseEntity<List<AuthorResponseList>> response = authorService.listAllAuthor();

        assertNotNull(response);
        List<AuthorResponseList> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        Mockito.verify(authorRepository, Mockito.times(1)).findAll();
        Mockito.verify(objectMapper, Mockito.times(1)).convertValue(author1, AuthorResponseList.class);
        Mockito.verify(objectMapper, Mockito.times(1)).convertValue(author2, AuthorResponseList.class);
    }

    @Test
    void shouldUpdateAuthor() {
        Long authorId = 1L;
        AuthorAddRequestDTO authorDTO = new AuthorAddRequestDTO("José Atualizado", "Brasil");
        Author existingAuthor = new Author(authorId, "José", "Brasil", null);
        Author updatedAuthor = new Author(authorId, "José Atualizado", "Brasil", null);
        AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO("José Atualizado", "Brasil", null);

        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        Mockito.when(authorRepository.save(existingAuthor)).thenReturn(updatedAuthor);
        Mockito.when(objectMapper.convertValue(updatedAuthor, AuthorResponseDTO.class)).thenReturn(authorResponseDTO);

        ResponseEntity<AuthorResponseDTO> response = authorService.updateAuthor(authorId, authorDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldDeleteAuthor() {
        Long authorId = 1L;
        Author author = new Author(authorId, "José", "Brasil", null);

        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        ResponseEntity<?> response = authorService.deleteAuthor(authorId);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());

        Mockito.verify(authorRepository, Mockito.times(1)).findById(authorId);
        Mockito.verify(authorRepository, Mockito.times(1)).delete(author);
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFoundForDelete() {
        Long authorId = 1L;

        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authorService.deleteAuthor(authorId));

        Mockito.verify(authorRepository, Mockito.times(1)).findById(authorId);
        Mockito.verify(authorRepository, Mockito.never()).delete(Mockito.any(Author.class));
    }

    @Test
    void shouldReturnAuthorById() {
        Long authorId = 1L;
        Author author = new Author(authorId, "José", "Brasil", null);

        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        Author response = authorService.listById(authorId);

        assertNotNull(response);
        assertEquals(authorId, response.getId());
        assertEquals("José", response.getName());
        assertEquals("Brasil", response.getNationality());

        Mockito.verify(authorRepository, Mockito.times(1)).findById(authorId);
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFoundById() {
        // Arrange
        Long authorId = 1L;

        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authorService.listById(authorId));

        Mockito.verify(authorRepository, Mockito.times(1)).findById(authorId);
    }
}