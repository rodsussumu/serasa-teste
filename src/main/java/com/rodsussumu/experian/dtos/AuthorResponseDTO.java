package com.rodsussumu.experian.dtos;

import com.rodsussumu.experian.models.Book;

import java.util.List;

public record AuthorResponseDTO(String name, String nationality, List<Book> books) {
}
