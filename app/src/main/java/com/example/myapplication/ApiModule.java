package com.example.myapplication;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

class ApiModule {
    //singleton
    private static ExampleApi exampleApi = null;
    public static final String BASE_URL = "https://mobile.webteh.hr/";

    private ApiModule() {
    }

    public static ExampleApi getExampleApi() {
        if (exampleApi == null) {
            exampleApi = getRetrofit().create(ExampleApi.class);
        }
        return exampleApi;
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(converterFactory(objectMapper(new SimpleModule())))
                .addCallAdapterFactory(callAdapterFactory())
                .client(provideOkHttpClient())
                .build();
    }

    private static Converter.Factory converterFactory(ObjectMapper objectMapper) {
        return JacksonConverterFactory.create(objectMapper);
    }

    private static ObjectMapper objectMapper(SimpleModule simpleModule) {
        return new ObjectMapper()
                .registerModule(simpleModule)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    private static CallAdapter.Factory callAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    private static OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }
}
