package com.example.gatewayService.config;


import com.example.gatewayService.security.JWTUtil;
import com.example.gatewayService.security.UserLoginDetails;
import com.example.gatewayService.services.UserLoginDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if (jwt.isBlank()) response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token not valid");
            else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    UserLoginDetails userLoginDetails = userLoginDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken token =
                            new UsernamePasswordAuthenticationToken
                                    (userLoginDetails, userLoginDetails.getPassword(), userLoginDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                } catch (Exception e) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("JWT token expired");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

