package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.dto.CustomerRequestDTO;
import com.example.demo.domain.dto.CustomerResponseDTO;
import com.example.demo.search.SearchCriteria;
import com.example.demo.service.customer.CustomerService;
import com.example.demo.service.report.CustomerReportService;
import com.example.demo.service.report.ReportRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer API", description = "Endpoints for managing customers")
public class CustomerController {
    
    private final CustomerService service;
    private final CustomerReportService reportService;

    public CustomerController(CustomerService service, CustomerReportService reportService) {
        this.service = service;
        this.reportService = reportService;
    }

    @Operation(summary = "Get paginated customers")
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> getAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @Operation(summary = "Get customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(
            @Valid @RequestBody CustomerRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @Operation(summary = "Search customers with dynamic filters")
    @PostMapping("/search")
    public ResponseEntity<Page<CustomerResponseDTO>> search(
            @RequestBody List<SearchCriteria> filters,
            Pageable pageable) {
    
        return ResponseEntity.ok(service.search(filters, pageable));
    }

    @Operation(
        summary = "Generate customer report",
        description = """
            Generates a customer report based on dynamic filters.
            Supported formats: PDF, XLSX.
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report generated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid report request"),
        @ApiResponse(responseCode = "500", description = "Report generation error")
    })
    @PostMapping("/report")
    public ResponseEntity<byte[]> generateReport(
            @RequestBody ReportRequest request) {

        byte[] file = reportService.generate(request);
    
        String filename = "customers_report." +
                request.format().toLowerCase();
    
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + filename)
                .contentType(resolveContentType(request.format()))
                .body(file);
    }
    
    private MediaType resolveContentType(String format) {
        if ("XLSX".equalsIgnoreCase(format)) {
            return MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }
        return MediaType.APPLICATION_PDF;
    }

    @Operation(summary = "Update an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(
            @Parameter(description = "Customer ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Soft delete a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
