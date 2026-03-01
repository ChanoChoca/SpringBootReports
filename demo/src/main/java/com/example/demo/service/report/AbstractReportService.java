package com.example.demo.service.report;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ReportGenerationException;
import com.example.demo.search.SearchCriteria;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public abstract class AbstractReportService<T, R> {

    private static final Logger log = LoggerFactory.getLogger(AbstractReportService.class);

    protected abstract List<T> fetchData(List<SearchCriteria> filters);
    protected abstract List<R> mapToReportDTO(List<T> data);
    
    @Value("${app.reports.pdf-template}")
    private String pdfTemplate;
    
    @Value("${app.reports.excel-template}")
    private String excelTemplate;

    protected Map<String, Object> buildParameters(
            String generatedBy,
            int totalRecords) {

        Map<String, Object> params = new HashMap<>();
        params.put("generatedBy", generatedBy);
        params.put("generatedAt", LocalDateTime.now());
        params.put("totalRecords", totalRecords);
        return params;
    }

    public byte[] generate(ReportRequest request) {

        if (!List.of("PDF","XLSX")
                .contains(request.format().toUpperCase())) {
        
            throw new BusinessException("Unsupported report format. Allowed: PDF, XLSX");
        }

        long start = System.currentTimeMillis();
    
        try {
    
            log.debug("Starting report generation: format={}, filters={}",
                    request.format(),
                    request.filters() != null ? request.filters().size() : 0);
    
            List<T> data = fetchData(request.filters());
            List<R> reportData = mapToReportDTO(data);
    
            log.debug("Fetched {} records for report", reportData.size());
    
            InputStream template = resolveTemplate(request.format());
    
            JRBeanCollectionDataSource ds =
                new JRBeanCollectionDataSource(reportData);
    
            JasperPrint print = JasperFillManager.fillReport(
                    template,
                    buildParameters(request.generatedBy(), reportData.size()),
                    ds
            );
    
            byte[] result = export(print, request.format());
    
            long duration = System.currentTimeMillis() - start;
    
            log.info("Report generated successfully in {} ms", duration);
    
            return result;
    
        } catch (Exception e) {
    
            log.error("Error generating report", e);
            throw new ReportGenerationException("Report error", e);
        }
    }

    private byte[] export(JasperPrint print, String format)
            throws JRException {

        if ("XLSX".equalsIgnoreCase(format)) {
            return exportXlsx(print);
        }

        return JasperExportManager.exportReportToPdf(print);
    }

    private byte[] exportXlsx(JasperPrint print) throws JRException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
    
        JRXlsxExporter exporter = new JRXlsxExporter();
    
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(out));
    
        SimpleXlsxReportConfiguration config =
                new SimpleXlsxReportConfiguration();
    
        config.setDetectCellType(true);
        config.setCollapseRowSpan(false);
        config.setRemoveEmptySpaceBetweenRows(true);
        config.setRemoveEmptySpaceBetweenColumns(true);
        config.setWhitePageBackground(false);
        config.setIgnoreCellBorder(true);
    
        exporter.setConfiguration(config);
    
        exporter.exportReport();
    
        return out.toByteArray();
    }

    private InputStream resolveTemplate(String format) {

        String templatePath;
    
        if ("XLSX".equalsIgnoreCase(format)) {
            templatePath = excelTemplate;
        } else {
            templatePath = pdfTemplate;
        }
    
        return getClass().getResourceAsStream(templatePath);
    }
}