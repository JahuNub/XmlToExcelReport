package com.api.report.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Optional;

@Service
public class ReportService {

    private final ExcelService excelService;

    public ReportService(ExcelService excelService) {
        this.excelService = excelService;
    }

    public Optional<byte[]> generateXlsReport(MultipartFile file){
        try {
            parseXmlFileToExcel(file);
            return Optional.of(excelService.exportCreatedExcel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void parseXmlFileToExcel(MultipartFile file) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(file.getInputStream(), new SaxReaderService(excelService));
        System.out.println(file.getOriginalFilename());
    }

}
