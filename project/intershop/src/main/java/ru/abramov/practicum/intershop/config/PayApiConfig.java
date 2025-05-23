package ru.abramov.practicum.intershop.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.abramov.practicum.intershop.client.pay.api.PayApi;
import ru.abramov.practicum.intershop.client.pay.invoker.ApiClient;

@Configuration
public class PayApiConfig {

    @Value("${service.pay.host}")
    private String host;

    @Value("${service.pay.port}")
    private String port;

    @Bean
    public PayApi payApi(ReactiveOAuth2AuthorizedClientManager authorizedClientManager, ObjectMapper objectMapper) {

        String baseUrl = host + ":" + port;

        ExchangeStrategies strategies = ExchangeStrategies
                .builder()
                .codecs(clientDefaultCodecsConfigurer -> {
                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                }).build();

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .filter(authorizationFilter(authorizedClientManager))
                .build();

        ApiClient apiClient = new ApiClient(webClient);

        apiClient.setBasePath(baseUrl);

        return new PayApi(apiClient);
    }

    private ExchangeFilterFunction authorizationFilter(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> authorizedClientManager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId(("keycloak-pay-client"))
                        .principal("system")
                        .build())
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2AccessToken::getTokenValue)
                .map(token -> ClientRequest.from(clientRequest)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build()));
    }
}
