package com.example.Frontend.controllers;

import com.example.Frontend.dto.UserLogin;
import com.example.Frontend.security.JWTUtil;
import com.example.Frontend.service.AuthService;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm(@ModelAttribute("userLogin") UserLogin userLogin) {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userLogin") UserLogin userLogin,
                        BindingResult bindingResult, RedirectAttributes redirectAttributes,
                        HttpServletResponse httpServletResponse) {

        // Валидация полей формы
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        // Аутентификация пользователя
        String token = null;
        try {
            token = authService.authenticateAndGetToken(userLogin.getEmail(), userLogin.getPassword());
        } catch (InvalidCredentialsException e) {
            bindingResult.rejectValue("email", "0", "Пользователь по данному email не найден");
            return "auth/login";
        }

        if (token != null) {
            // Успешная аутентификация

            // Создание куки с токеном
            Cookie tokenCookie = new Cookie("token", token);
            tokenCookie.setPath("/");
            tokenCookie.setHttpOnly(true);// Недоступен из JavaScript
            // Добавление куки в ответ
            httpServletResponse.addCookie(tokenCookie);

            redirectAttributes.addFlashAttribute("successMessage", "You have been successfully logged in.");
            return "redirect:/users";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password.");
            return "redirect:/login";
        }
    }


}
