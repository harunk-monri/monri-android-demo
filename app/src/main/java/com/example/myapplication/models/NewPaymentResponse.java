package com.example.myapplication.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewPaymentResponse {
    @JsonProperty("client_secret")
    String clientSecret;

    @JsonProperty("status")
    String status;

    public NewPaymentResponse(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public NewPaymentResponse(String clientSecret, String status) {
        this.clientSecret = clientSecret;
        this.status = status;
    }

    public NewPaymentResponse() {
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getStatus() {
        return status;
    }
}