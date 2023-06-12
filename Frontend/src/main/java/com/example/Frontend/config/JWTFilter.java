package com.example.Frontend.config;

import com.example.Frontend.security.JWTUtil;
import com.example.Frontend.security.UserLoginDetails;
import com.example.Frontend.service.UserLoginDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserLoginDetailsService userLoginDetailsService;

    public JWTFilter(JWTUtil jwtUtil, UserLoginDetailsService userLoginDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userLoginDetailsService = userLoginDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;

        // Получение токена из куки
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        if (jwt == null || jwt.isBlank()) {
            // Если токен отсутствует или пустой, отправляем ошибку
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token not valid");
            return;
        }

        try {
            String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
            UserLoginDetails userLoginDetails = userLoginDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userLoginDetails, userLoginDetails.getPassword(), userLoginDetails.getAuthorities());

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }

}

