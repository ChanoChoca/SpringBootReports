package com.example.demo.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequestDTO(

    @Schema(description = "Customer first name", example = "John")
    @NotBlank
    @Size(max = 100)
    String firstName,

    @Schema(description = "Customer last name", example = "Doe")
    @NotBlank
    @Size(max = 100)
    String lastName,

    @Schema(description = "Customer email", example = "john@gmail.com")
    @Email
    @NotBlank
    String email,

    @Schema(description = "Phone number", example = "+54 341 1234567")
    String phone,

    @Schema(description = "Customer active status", example = "true")
    Boolean active
) {}