package com.api.report.service;

import com.api.report.customer.Customer;
import com.api.report.user.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    private static final int CUSTOMER_NUMBER = 0;
    private static final int CUSTOMER_NAME = 1;
    private static final int CUSTOMER_GROUP = 2;
    private static final int CUSTOMER_SEGMENTS = 3;
    private static final int USER = 4;
    private static final int LAST_LOGGED = 5;
    private static final int ROLES = 6;

    private static final int NUMBER_OF_COLUMNS = 7;

    private Workbook workbook = new XSSFWorkbook();
    private Sheet sheet = workbook.createSheet("Customers");
    static int rowId = 0;

    public void clearWorkbook(){
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Customers");
        rowId = 0;
    }

    public void addDataToExcel(Customer customer){
        List<User> users = customer.getUsersList();

        if (!users.isEmpty()){
            for (User user : users) {
                rowId++;
                Row row = sheet.createRow(rowId);
                createCommonData(customer, row);
                row.createCell(USER).setCellValue(user.getEmail());
                row.createCell(LAST_LOGGED).setCellValue(user.getLastLogin());
                row.createCell(ROLES).setCellValue(user.getRoles());
            }
        }else {
            rowId++;
            Row row = sheet.createRow(rowId);
            createCommonData(customer, row);
        }
    }

    private void createCommonData(Customer customer, Row row) {
        row.createCell(CUSTOMER_NUMBER).setCellValue(customer.getCompanyId());
        row.createCell(CUSTOMER_NAME).setCellValue(customer.getCompanyName());
        row.createCell(CUSTOMER_GROUP).setCellValue(customer.getCustomerGroup());
        row.createCell(CUSTOMER_SEGMENTS).setCellValue(customer.getCustomerSegments().toString().replaceAll("[\\[\\]]",""));
    }

    public void createHeaderRow() {
        Row headerRow = sheet.createRow(rowId);
        headerRow.createCell(CUSTOMER_NUMBER).setCellValue("Customer Number");
        headerRow.createCell(CUSTOMER_NAME).setCellValue("Customer name");
        headerRow.createCell(CUSTOMER_GROUP).setCellValue("Customer group");
        headerRow.createCell(CUSTOMER_SEGMENTS).setCellValue("Customer Segments");
        headerRow.createCell(USER).setCellValue("User");
        headerRow.createCell(LAST_LOGGED).setCellValue("Last Logged In");
        headerRow.createCell(ROLES).setCellValue("Roles");
    }

    public void autoSizeColumns(){
        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            sheet.autoSizeColumn(i);
        }
    }


    public byte[] exportCreatedExcel(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            autoSizeColumns();
            workbook.write(outputStream);
            outputStream.close();
            clearWorkbook(); //clear workbook for next xmls
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

}
