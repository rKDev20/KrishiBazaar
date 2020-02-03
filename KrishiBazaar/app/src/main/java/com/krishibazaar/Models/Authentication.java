package com.krishibazaar.Models;

public class Authentication {
    private int status;
    private String token;

    public Authentication(int status, String token) {
        this.status = status;
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }
}
