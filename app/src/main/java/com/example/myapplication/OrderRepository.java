package com.example.myapplication;

import com.example.myapplication.models.NewPaymentRequest;
import com.example.myapplication.models.NewPaymentResponse;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class OrderRepository {
    public static Single<NewPaymentResponse> createPaymentSession(boolean addPaymentMethod) {

        return ApiModule.getExampleApi().createPaymentSession(new NewPaymentRequest(addPaymentMethod))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
