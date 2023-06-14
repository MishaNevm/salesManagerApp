package com.example.Frontend.security;

import com.example.Frontend.dto.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final RestTemplate restTemplate;

    private String LOGIN_URL = "http://localhost:8484/auth/login";

    private final HttpServletResponse response;

    @Autowired
    public AuthProviderImpl(RestTemplate restTemplate, HttpServletResponse response) {
        this.restTemplate = restTemplate;
        this.response = response;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!restTemplate.getInterceptors().isEmpty()) {
            restTemplate.getInterceptors().clear();
        }
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(authentication.getName());
        userLogin.setPassword((String) authentication.getCredentials());
        String token;
        try {
            token = restTemplate.postForObject(LOGIN_URL, userLogin, String.class);
        } catch (HttpClientErrorException.BadRequest e) {
            throw new BadCredentialsException("Ошибка аутентификации");
        }
        if (token == null) {
            throw new BadCredentialsException("Ошибка аутентификации");
        }
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + token);
            return execution.execute(request, body);
        });
        UserLoginDetails userLoginDetails = new UserLoginDetails(userLogin);
        return new UsernamePasswordAuthenticationToken(userLoginDetails, userLoginDetails.getPassword(), userLoginDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

