package com.daniel.authenticationapi.objects.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResponseDTO(
        UUID id,
        String username,
        LocalDateTime created
) {
}
