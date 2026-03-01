package com.example.demo.service.report;

import java.util.List;

import com.example.demo.search.SearchCriteria;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for generating reports")
public record ReportRequest(

    @Schema(description = "Dynamic filters to apply",
            example = "[{\"key\":\"email\",\"operation\":\"LIKE\",\"value\":\"gmail\"}]")
    List<SearchCriteria> filters,

    @Schema(description = "User generating the report",
            example = "admin")
    String generatedBy,

    @Schema(description = "Output format: PDF or XLSX",
            example = "PDF")
    String format
) {}