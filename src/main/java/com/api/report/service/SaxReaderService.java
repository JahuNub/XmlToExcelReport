package com.api.report.service;

import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.api.report.customer.Customer;
import com.api.report.user.User;

//SaxReaderService contains logic what data is read from xml. If any new field required - here is to be added
@Service
public class SaxReaderService extends DefaultHandler {
    private int customerCounter = 0;
    private final StringBuilder textFromNodeBuilder = new StringBuilder();
    private Customer customer = new Customer();
    private User user = new User();
    private final Map<String, Boolean> attributesMap = new HashMap<>();
    private final ExcelService excelService;

    public SaxReaderService(ExcelService excelService) {
        this.excelService = excelService;
    }

    @Override
    public void startDocument() {
        excelService.createHeaderRow();
    }

    //Node start ex. <custom-attribute>, searching for attributes
    @Override
    public void startElement(String uri, String localName,String qName, Attributes attributes) {
        //Reset StringBuilder and segmentsList
        textFromNodeBuilder.setLength(0);
        //checking for new customer <customer>
        if (qName.equalsIgnoreCase("customer")){
            customer.setCompanyId(attributes.getValue("id"));
            customerCounter++;
        }
        if (qName.equalsIgnoreCase("user-group")){
            customer.addSegment(attributes.getValue("id"));
        }
        //search for specific att-names in custom-attributes nodes
        addSearchedCustomAttributesToMap(qName, attributes);
    }

    //Node end ex. </custom-attribute>, getting text
    @Override
    public void endElement(String uri, String localName,String qName) {
        switch (qName.toLowerCase()){

            case "company-name":
                customer.setUsersList(new ArrayList<>());
                customer.setCustomerSegments(new ArrayList<>());
                customer.setCompanyName(textFromNodeBuilder.toString());
                break;

            case "custom-attribute":
                searchForSpecificAttributeName();
                break;

            case "reminder-email":
                user = new User(textFromNodeBuilder.toString());
                break;

            case "last-logged-in":
                user.setLastLogin(textFromNodeBuilder.toString().replaceAll("T", " ").substring(0,19));
                break;

            case "customer":
                excelService.addDataToExcel(customer);
                customer = new Customer(); //clear for next customer in xml
                break;
        }

    }

    //get text from searched node(attribute name)
    private void searchForSpecificAttributeName() {
        if (isAttributeNameExist("CustomerGroup2")) {
            customer.setCustomerGroup(textFromNodeBuilder.toString());
            attributesMap.replace("CustomerGroup2", false);
        }
        if (isAttributeNameExist("RoleID")) {
            user.setRoles(textFromNodeBuilder.toString());
            customer.addUser(user);
            attributesMap.replace("RoleID", false);
        }
    }

    //Add searched data to map from custom-attribute named nodes
    private void addSearchedCustomAttributesToMap(String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("custom-attribute")){
            String attributeValue = attributes.getValue("name");

            switch (attributeValue){
                case "CustomerGroup2":
                    attributesMap.put("CustomerGroup2", true);
                    break;

                case "RoleID":
                    attributesMap.put("RoleID", true);
                    break;
            }
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
        textFromNodeBuilder.append(ch, start, length);
    }

    public boolean isAttributeNameExist(String attributeName){
        return attributesMap.containsKey(attributeName) &&
                Optional.ofNullable(attributesMap.get(attributeName)).orElse(false);
    }

    @Override
    public void endDocument() {
        excelService.addDataToExcel(customer); //adding last customer found
        if (customerCounter == 0){
            throw new RuntimeException("No customers in this file");
        }
        System.out.println("customers processed: " + customerCounter);
    }
}

