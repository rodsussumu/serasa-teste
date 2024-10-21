package com.rodsussumu.experian.dtos;

import com.rodsussumu.experian.models.Author;

public record BookListResponseDTO(Long id, String genre, String release, String title, BookAuthorResponseDTO author) {
}
