package com.api.report.customer;

import com.api.report.user.User;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String companyId;
    private String companyName;
    private String customerGroup;
    private List<String> customerSegments = new ArrayList<>();
    private List<User> usersList = new ArrayList<>();

    public List<String> getCustomerSegments() {
        return customerSegments;
    }

    public void setCustomerSegments(List<String> customerSegments) {
        this.customerSegments = customerSegments;
    }

    public void addSegment(String segment){
        customerSegments.add(segment);
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public void addUser(User user){
        usersList.add(user);
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
