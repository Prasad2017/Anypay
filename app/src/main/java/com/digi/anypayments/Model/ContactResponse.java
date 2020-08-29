package com.digi.anypayments.Model;

import com.google.gson.annotations.SerializedName;

public class ContactResponse {

    private String customerName;

    private String customerNumber;


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }
}
