package ru.abramov.practicum.pay;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class BalanceApiTest extends AbstractApiTest {

    @Test
    void balanceGet_shouldReturnBalance() {

        webTestClient.mutateWith(getMockJwt())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/balance")
                        .queryParam("user_id", "user-42")
                        .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.balance");
    }
}
