package ru.abramov.practicum.intershop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private ReactiveClientRegistrationRepository clientRegistrationRepository;

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
                .oauth2Client(withDefaults())
                .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()))
                .build();
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedServerLogoutSuccessHandler handler =
                new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);

        handler.setPostLogoutRedirectUri("{baseUrl}");

        return handler;
    }
}
