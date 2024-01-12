package com.example.myapplication;


import com.example.myapplication.models.NewPaymentRequest;
import com.example.myapplication.models.NewPaymentResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface ExampleApi {
    @POST("example/create-payment-session")
    Single<NewPaymentResponse> createPaymentSession(@Body NewPaymentRequest request);
}
