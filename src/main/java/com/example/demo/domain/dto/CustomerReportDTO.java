package com.example.demo.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CustomerReportDTO {

    @Schema(description = "Customer ID")
    private Long id;
    @Schema(description = "Customer full name")
    private String fullName;
    @Schema(description = "Customer email")
    private String email;
    @Schema(description = "Phone number")
    private String phone;
    @Schema(description = "Creation date")
    private String createdAt;

    public CustomerReportDTO() {}

    public CustomerReportDTO(Long id, String fullName, String email, String phone, String createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}