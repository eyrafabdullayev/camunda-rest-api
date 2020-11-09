package com.example.workflow.config;

import com.example.workflow.service.JWTInMemoryUserDetailsService;
import com.example.workflow.filter.JWTTokenAuthenticationOnePerRequestFilter;
import com.example.workflow.exception.JWTUnAuthorizedResponseAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationConfiguration applicationConfiguration;
    private final JWTUnAuthorizedResponseAuthenticationEntryPoint unAuthorizedResponseAuthenticationEntryPoint;
    private final JWTInMemoryUserDetailsService inMemoryUserDetailsService;
    private final JWTTokenAuthenticationOnePerRequestFilter tokenAuthenticationOnePerRequestFilter;

    public WebSecurityConfig(JWTUnAuthorizedResponseAuthenticationEntryPoint unAuthorizedResponseAuthenticationEntryPoint,
                             JWTInMemoryUserDetailsService inMemoryUserDetailsService,
                             JWTTokenAuthenticationOnePerRequestFilter tokenAuthenticationOnePerRequestFilter,
                             ApplicationConfiguration applicationConfiguration) {
        this.unAuthorizedResponseAuthenticationEntryPoint = unAuthorizedResponseAuthenticationEntryPoint;
        this.inMemoryUserDetailsService = inMemoryUserDetailsService;
        this.tokenAuthenticationOnePerRequestFilter = tokenAuthenticationOnePerRequestFilter;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(inMemoryUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unAuthorizedResponseAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .anyRequest().authenticated();

        httpSecurity
                .addFilterBefore(tokenAuthenticationOnePerRequestFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .headers()
                .frameOptions().sameOrigin()  //H2 Console Needs this setting
                .cacheControl(); //disable caching
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers(
                        HttpMethod.POST,
                       applicationConfiguration.getAuthenticationPath()
                )
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .and()
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/" //Other Stuff You want to Ignore
                )
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**"); //Should not be in Production!
    }
}
