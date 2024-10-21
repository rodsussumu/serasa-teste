package com.rodsussumu.experian.dtos;

public record BookCreateRequestDTO(String genre, String release_year, String title, Long author_id) {
}
