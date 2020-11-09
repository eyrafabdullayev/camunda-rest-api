package com.example.workflow.util;

import org.springframework.http.HttpHeaders;

public final class CommonUtil {

    public static HttpHeaders getHeaderWithAuthToken(String authToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + authToken);
        return httpHeaders;
    }
}
