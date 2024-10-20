package com.rodsussumu.experian.dtos;

import lombok.Builder;

@Builder
public record LoginResponseDTO(String username, String token) {
}
