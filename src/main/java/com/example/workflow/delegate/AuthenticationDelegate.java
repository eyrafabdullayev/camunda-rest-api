package com.example.workflow.delegate;

import com.example.workflow.config.ApplicationConfiguration;
import com.example.workflow.dto.response.JWTTokenResponse;
import com.example.workflow.util.CommonUtil;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component("authentication")
public class AuthenticationDelegate implements JavaDelegate {

    private final ApplicationConfiguration applicationConfiguration;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public AuthenticationDelegate(ApplicationConfiguration applicationConfiguration,
                                  RestTemplate restTemplate) {
        this.applicationConfiguration = applicationConfiguration;
        this.restTemplate = restTemplate;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("LOGGER - AuthenticationDelegate - START");

        String URL = applicationConfiguration.getBaseUrl() + applicationConfiguration.getRefreshPath();
        String authToken = (String) execution.getVariable("auth_token");

        JWTTokenResponse body = null;
        try {
            body = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<>(CommonUtil.getHeaderWithAuthToken(authToken)), JWTTokenResponse.class).getBody();

            execution.setVariable("auth_token", body.getToken());
        } catch (HttpClientErrorException ex) {
            execution.setVariable("auth_token", body);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "TOKEN_NOT_FOUND");
        } catch (HttpServerErrorException ex) {
            execution.setVariable("auth_token", body);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "TOKEN_NOT_FOUND");
        }

        logger.info("LOGGER - AuthenticationDelegate - END");
    }
}
