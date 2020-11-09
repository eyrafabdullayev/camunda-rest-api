package com.example.workflow.controller;

import com.example.workflow.config.ApplicationConfiguration;
import com.example.workflow.exception.AuthenticationException;
import com.example.workflow.service.JWTInMemoryUserDetailsService;
import com.example.workflow.dto.request.JWTTokenRequest;
import com.example.workflow.dto.response.JWTTokenResponse;
import com.example.workflow.util.JWTTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
public class TokenRestController {

    private final ApplicationConfiguration applicationConfiguration;
    private final AuthenticationManager authenticationManager;
    private final JWTInMemoryUserDetailsService inMemoryUserDetailsService;
    private final JWTTokenUtil tokenUtil;

    public TokenRestController(AuthenticationManager authenticationManager,
                               JWTInMemoryUserDetailsService inMemoryUserDetailsService,
                               JWTTokenUtil tokenUtil,
                               ApplicationConfiguration applicationConfiguration) {
        this.authenticationManager = authenticationManager;
        this.inMemoryUserDetailsService = inMemoryUserDetailsService;
        this.tokenUtil = tokenUtil;
        this.applicationConfiguration = applicationConfiguration;
    }

    @RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JWTTokenRequest request) throws AuthenticationException {
        authenticate(request.getUsername(), request.getPassword());
        final UserDetails userDetails = inMemoryUserDetailsService.loadUserByUsername(request.getUsername());
        final String token = tokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JWTTokenResponse(token));
    }

    @RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(applicationConfiguration.getRequestHeader());
        final String token = authToken.substring(7);
        if(!tokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = tokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JWTTokenResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getLocalizedMessage());
    }

    public void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException ex) {
            throw new AuthenticationException("USER_DISABLED", ex);
        } catch (BadCredentialsException ex) {
            throw new AuthenticationException("INVALID_CREDENTIALS", ex);
        }
    }
}
