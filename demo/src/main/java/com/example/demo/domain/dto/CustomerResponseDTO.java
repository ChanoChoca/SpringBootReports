package com.example.demo.domain.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerResponseDTO(

    @Schema(description = "Customer ID", example = "1")
    Long id,

    @Schema(description = "First name")
    String firstName,

    @Schema(description = "Last name")
    String lastName,

    @Schema(description = "Email address")
    String email,

    @Schema(description = "Phone number")
    String phone,

    @Schema(description = "Active status")
    Boolean active,

    @Schema(description = "Creation date")
    LocalDate createdAt,

    @Schema(description = "Update date")
    LocalDate updatedAt
) {}
