package com.example.workflow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@ConfigurationProperties(prefix = "camunda-rest-api")
public class ApplicationConfiguration {
    @Value("${signingKey}")
    private String signingKey;
    @Value("${authenticationPath}")
    private String authenticationPath;
    @Value("${refreshPath}")
    private String refreshPath;
    @Value("${requestHeader}")
    private String requestHeader;
    @Value("${expirationSeconds}")
    private Long expirationSeconds;
    @Value("${baseUrl}")
    private String baseUrl;

    public ApplicationConfiguration() {
    }

    public ApplicationConfiguration(String signingKey, String authenticationPath, String refreshPath, String requestHeader, Long expirationSeconds, String baseUrl) {
        this.signingKey = signingKey;
        this.authenticationPath = authenticationPath;
        this.refreshPath = refreshPath;
        this.requestHeader = requestHeader;
        this.expirationSeconds = expirationSeconds;
        this.baseUrl = baseUrl;
    }

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }

    public String getAuthenticationPath() {
        return authenticationPath;
    }

    public void setAuthenticationPath(String authenticationPath) {
        this.authenticationPath = authenticationPath;
    }

    public String getRefreshPath() {
        return refreshPath;
    }

    public void setRefreshPath(String refreshPath) {
        this.refreshPath = refreshPath;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public Long getExpirationSeconds() {
        return expirationSeconds;
    }

    public void setExpirationSeconds(Long expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationConfiguration that = (ApplicationConfiguration) o;
        return Objects.equals(signingKey, that.signingKey) &&
                Objects.equals(authenticationPath, that.authenticationPath) &&
                Objects.equals(refreshPath, that.refreshPath) &&
                Objects.equals(requestHeader, that.requestHeader) &&
                Objects.equals(expirationSeconds, that.expirationSeconds) &&
                Objects.equals(baseUrl, that.baseUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signingKey, authenticationPath, refreshPath, requestHeader, expirationSeconds, baseUrl);
    }

    @Override
    public String toString() {
        return "ApplicationConfiguration{" +
                "signingKey='" + signingKey + '\'' +
                ", authenticationPath='" + authenticationPath + '\'' +
                ", refreshPath='" + refreshPath + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", expirationSeconds=" + expirationSeconds +
                ", baseUrl='" + baseUrl + '\'' +
                '}';
    }
}
