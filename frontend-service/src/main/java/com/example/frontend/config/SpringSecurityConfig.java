package com.example.frontend.config;

import com.example.frontend.security.AuthProviderImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProviderImpl authProvider;

    public SpringSecurityConfig(AuthProviderImpl authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                      .authorizeRequests()
                      .antMatchers("/auth/login", "/error")
                      .permitAll()
                      .anyRequest()
                      .authenticated()
                .and()
                      .formLogin()
                      .loginPage("/auth/login")
                      .loginProcessingUrl("/process_login")
                      .defaultSuccessUrl("/users")
                      .failureUrl("/auth/login?error")
                .and()
                      .logout()
                      .logoutUrl("/logout")
                      .logoutSuccessUrl("/auth/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }
}


