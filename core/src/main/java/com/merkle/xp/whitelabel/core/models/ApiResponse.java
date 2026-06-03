package com.merkle.xp.whitelabel.core.models;

public class ApiResponse {
    private int statusCode;

    private Object response;
    
    public ApiResponse() {}

    public ApiResponse(int statusCode, String response) {
        this.statusCode = statusCode;
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        if(response != null)
          return response.toString();
        return null;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
