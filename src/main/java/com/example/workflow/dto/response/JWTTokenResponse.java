package com.example.workflow.dto.response;

import java.io.Serializable;

public class JWTTokenResponse implements Serializable {

    private static final long serialVersionUID = 8317676219297719109L;

    private String token;

    public JWTTokenResponse() {
    }

    public JWTTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
