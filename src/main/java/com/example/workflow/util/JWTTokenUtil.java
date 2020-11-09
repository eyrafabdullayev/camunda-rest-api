package com.example.workflow.util;

import com.example.workflow.config.ApplicationConfiguration;
import com.example.workflow.entity.JWTUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private final ApplicationConfiguration applicationConfiguration;
    private Clock clock = DefaultClock.INSTANCE;

    public JWTTokenUtil(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(applicationConfiguration.getSigningKey()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(clock.now());
    }

    public Boolean canTokenBeRefreshed(String token) {
        return isTokenExpired(token);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerate(claims, userDetails);
    }

    private String doGenerate(Map<String, Object> claims, UserDetails userDetails) {
        Date issuedAtDate = clock.now();
        Date expirationDate = calculateExpirationDate(issuedAtDate);

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).signWith(SignatureAlgorithm.HS512, applicationConfiguration.getSigningKey())
                .setIssuedAt(issuedAtDate).setExpiration(expirationDate).compact();
    }

    public String refreshToken(String token) {
        final Date issuedAtDate = clock.now();
        final Date expirationDate = calculateExpirationDate(issuedAtDate);

        Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(issuedAtDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, applicationConfiguration.getSigningKey()).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        JWTUserDetails user = (JWTUserDetails) userDetails;
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private Date calculateExpirationDate(Date issuedAtDate) {
        return new Date(issuedAtDate.getTime() + applicationConfiguration.getExpirationSeconds() * 1000);
    }
}
