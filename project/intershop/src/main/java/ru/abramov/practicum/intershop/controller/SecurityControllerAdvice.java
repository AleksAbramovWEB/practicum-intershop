package ru.abramov.practicum.intershop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ControllerAdvice
public class SecurityControllerAdvice {

    @ModelAttribute
    Mono<CsrfToken> csrfToken(ServerWebExchange exchange) {
        Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            return csrfToken.doOnSuccess(token -> exchange.getAttributes()
                    .put(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME, token));
        }

        return Mono.empty();
    }

    @ModelAttribute("user_id")
    public Mono<String> userId(ServerWebExchange exchange) {
        return exchange.getPrincipal()
                .filter(Authentication.class::isInstance)
                .cast(Authentication.class)
                .map(Authentication::getPrincipal)
                .filter(OAuth2User.class::isInstance)
                .cast(OAuth2User.class)
                .map(user -> (String) user.getAttribute("sub"))
                .switchIfEmpty(Mono.justOrEmpty(null));
    }

    @ModelAttribute("keycloak_uri")
    public Mono<String> keycloakUri() {
        return Mono.just("/oauth2/authorization/keycloak");
    }
}
