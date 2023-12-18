package com.api.report.controller;

import com.api.report.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> getExcelReport(@RequestParam("file") MultipartFile file) {
        byte[] report = reportService.generateXlsReport(file).orElse(new byte[0]);

        if (report.length > 0){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "report.xls");
            return new ResponseEntity<>(report, headers, HttpStatus.OK);
        }else {
            return ResponseEntity.badRequest().body("File could not be processed");
        }
    }
}
