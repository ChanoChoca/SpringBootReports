package com.example.demo.search;

import java.time.LocalDateTime;

public record CustomerSearchDTO(
        String firstName,
        String lastName,
        String email,
        Boolean active,
        LocalDateTime createdFrom,
        LocalDateTime createdTo
) {}
