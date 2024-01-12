package com.example.myapplication;

class Config {
    private Config() {
    }

    public static String authenticityToken() {
        return "merchant's authenticity token";
    }

    public static boolean isDevelopmentMode() {
        return true;
    }
}
