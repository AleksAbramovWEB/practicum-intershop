package ru.abramov.practicum.intershop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        var csrfHandler = new XorServerCsrfTokenRequestAttributeHandler();

        csrfHandler.setTokenFromMultipartDataEnabled(true);

        return http
                .csrf( csrf -> csrf.csrfTokenRequestHandler(csrfHandler))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/",
                                "/product/**"
                        ).permitAll()
                        .pathMatchers(
                                "/product/new",
                                "/orders",
                                "/order",
                                "/order/**",
                                "/cart"
                        ).authenticated()
                )
                .oauth2Login(withDefaults())
                .build();
    }
}
