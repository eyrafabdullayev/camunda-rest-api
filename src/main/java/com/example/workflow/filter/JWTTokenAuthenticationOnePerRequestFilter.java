package com.example.workflow.filter;

import com.example.workflow.config.ApplicationConfiguration;
import com.example.workflow.service.JWTInMemoryUserDetailsService;
import com.example.workflow.util.JWTTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTTokenAuthenticationOnePerRequestFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final JWTTokenUtil tokenUtil;
    private final JWTInMemoryUserDetailsService inMemoryUserDetailsService;
    private final ApplicationConfiguration applicationConfiguration;

    public JWTTokenAuthenticationOnePerRequestFilter(JWTTokenUtil tokenUtil,
                                                     JWTInMemoryUserDetailsService inMemoryUserDetailsService,
                                                     ApplicationConfiguration applicationConfiguration) {
        this.tokenUtil = tokenUtil;
        this.inMemoryUserDetailsService = inMemoryUserDetailsService;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("Authentication Request for '{}'", request.getRequestURL());

        final String requestToken = request.getHeader(applicationConfiguration.getRequestHeader());

        String username = null;
        String token = null;
        if(requestToken != null && requestToken.startsWith("Bearer ")) {
            token = requestToken.substring(7);
            try {
                username = tokenUtil.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.error("JWT_TOKEN_UNABLE_TO_GET_USERNAME", e);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT_TOKEN_EXPIRED", e);
            }
        } else {
            logger.warn("JWT_DOES_NOT_START_WITH_BEARER_STRING");
        }

        logger.debug("JWT_TOKEN_USERNAME_VALUE '{}'", username);
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = inMemoryUserDetailsService.loadUserByUsername(username);

            if(tokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
