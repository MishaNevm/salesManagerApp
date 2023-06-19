package com.example.Frontend.security;

import com.example.Frontend.dto.UserLogin;
import com.example.Frontend.util.CurrentUser;
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
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final RestTemplate restTemplate;
    private final CurrentUser currentUser;

    private final String LOGIN_URL = "http://localhost:8484/auth/login";

    private final HttpServletResponse response;

    @Autowired
    public AuthProviderImpl(RestTemplate restTemplate, CurrentUser currentUser, HttpServletResponse response) {
        this.restTemplate = restTemplate;
        this.currentUser = currentUser;
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
        HashMap<String, String> authMap;
        try {
            authMap = restTemplate.postForObject(LOGIN_URL, userLogin, HashMap.class);
        } catch (HttpClientErrorException.BadRequest e) {
            throw new BadCredentialsException("Ошибка аутентификации");
        }
        if (authMap == null) {
            throw new BadCredentialsException("Ошибка аутентификации");
        }
        currentUser.setEmail(userLogin.getEmail());
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + authMap.get("token"));
            return execution.execute(request, body);
        });
        userLogin.setUserRole(authMap.get("role"));
        currentUser.setRole(userLogin.getUserRole());
        UserLoginDetails userLoginDetails = new UserLoginDetails(userLogin);
        return new UsernamePasswordAuthenticationToken(userLoginDetails, userLoginDetails.getPassword(), userLoginDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

