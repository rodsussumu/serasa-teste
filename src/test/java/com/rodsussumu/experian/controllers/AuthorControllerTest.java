package com.rodsussumu.experian.controllers;

import com.rodsussumu.experian.dtos.AuthorAddRequestDTO;
import com.rodsussumu.experian.dtos.AuthorResponseDTO;
import com.rodsussumu.experian.dtos.AuthorResponseList;
import com.rodsussumu.experian.services.AuthorService;
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

class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddAuthorSuccessfully() {
        AuthorAddRequestDTO authorAddRequestDTO = new AuthorAddRequestDTO("José", "Brasil");
        AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO("José", "Brasil", null);

        Mockito.when(authorService.addAuthor(authorAddRequestDTO)).thenReturn(ResponseEntity.ok(authorResponseDTO));

        ResponseEntity<AuthorResponseDTO> response = authorController.addAuthor(authorAddRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthorResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("José", responseBody.name());
        assertEquals("Brasil", responseBody.nationality());
    }

    @Test
    void shouldGetAllAuthors() {
        AuthorResponseList author1 = new AuthorResponseList(1L, "José", "Brasil", null);
        AuthorResponseList author2 = new AuthorResponseList(2L, "Maria", "Portugal", null);
        List<AuthorResponseList> authors = List.of(author1, author2);

        Mockito.when(authorService.listAllAuthor()).thenReturn(ResponseEntity.ok(authors));

        ResponseEntity<List<AuthorResponseList>> response = authorController.getAllAuthor();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<AuthorResponseList> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        assertEquals(1L, responseBody.get(0).id());
        assertEquals("José", responseBody.get(0).name());
        assertEquals("Brasil", responseBody.get(0).nationality());

        assertEquals(2L, responseBody.get(1).id());
        assertEquals("Maria", responseBody.get(1).name());
        assertEquals("Portugal", responseBody.get(1).nationality());

    }

    @Test
    void shouldUpdateAuthor() {
        Long authorId = 1L;
        AuthorAddRequestDTO authorAddRequestDTO = new AuthorAddRequestDTO("José Atualizado", "Brasil");
        AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO("José Atualizado", "Brasil", null);

        Mockito.when(authorService.updateAuthor(authorId, authorAddRequestDTO)).thenReturn(ResponseEntity.ok(authorResponseDTO));

        ResponseEntity<AuthorResponseDTO> response = authorController.updateAuthor(authorId, authorAddRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthorResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("José Atualizado", responseBody.name());
        assertEquals("Brasil", responseBody.nationality());
    }

    @Test
    void shouldDeleteAuthor() {
        Long authorId = 1L;

        Mockito.when(authorService.deleteAuthor(authorId)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<?> response = authorController.deleteAuthor(authorId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}