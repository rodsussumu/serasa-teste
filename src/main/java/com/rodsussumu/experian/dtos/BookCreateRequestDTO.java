package com.rodsussumu.experian.dtos;

public record BookCreateRequestDTO(String genre, String release_year, String title, int quantity, Long author_id) {
}
