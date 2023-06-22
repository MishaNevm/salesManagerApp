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

import java.util.HashMap;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final RestTemplate restTemplate;
    private final CurrentUser currentUser;

    private static final String LOGIN_URL = "http://localhost:8484/auth/login";

    @Autowired
    public AuthProviderImpl(RestTemplate restTemplate, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.currentUser = currentUser;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        clearRestTemplateInterceptors();

        UserLogin userLogin = createUserLogin(authentication);
        HashMap<String, String> authMap = performLoginRequest(userLogin);

        validateAuthMap(authMap, userLogin);
        updateCurrentUser(userLogin, authMap);

        UserLoginDetails userLoginDetails = new UserLoginDetails(userLogin);
        return new UsernamePasswordAuthenticationToken(userLoginDetails, userLoginDetails.getPassword(), userLoginDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private void clearRestTemplateInterceptors() {
        restTemplate.getInterceptors().clear();
    }

    private UserLogin createUserLogin(Authentication authentication) {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();
        return new UserLogin(email, password);
    }

    private HashMap<String, String> performLoginRequest(UserLogin userLogin) {
        try {
            return restTemplate.postForObject(LOGIN_URL, userLogin, HashMap.class);
        } catch (HttpClientErrorException.BadRequest e) {
            throw new BadCredentialsException("Ошибка аутентификации");
        }
    }

    private void validateAuthMap(HashMap<String, String> authMap, UserLogin userLogin) {
        if (authMap == null) {
            throw new BadCredentialsException("Ошибка аутентификации");
        }
        userLogin.setUserRole(authMap.get("role"));
    }

    private void updateCurrentUser(UserLogin userLogin, HashMap<String, String> authMap) {
        String token = authMap.get("token");
        String userRole = authMap.get("role");

        currentUser.setEmail(userLogin.getEmail());
        currentUser.setRole(userRole);

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + token);
            return execution.execute(request, body);
        });
    }
}


