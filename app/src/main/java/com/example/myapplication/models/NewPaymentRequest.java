package com.example.myapplication.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewPaymentRequest {
    @JsonProperty("add_payment_method")
    boolean addPaymentMethod;

    public NewPaymentRequest(boolean addPaymentMethod) {
        this.addPaymentMethod = addPaymentMethod;
    }

    public NewPaymentRequest() {

    }
}
