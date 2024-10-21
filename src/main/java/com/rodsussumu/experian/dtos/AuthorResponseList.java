package com.rodsussumu.experian.dtos;

import com.rodsussumu.experian.models.Book;

import java.util.List;

public record AuthorResponseList(Long id, String name, String nationality, List<Book> books) {
}
