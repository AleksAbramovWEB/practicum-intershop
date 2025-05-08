package ru.abramov.practicum.pay;

import org.junit.jupiter.api.Test;

class BalanceApiTest extends AbstractApiTest {

    @Test
    void balanceGet_shouldReturnBalance() {

        webTestClient.get()
                .uri("/balance")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.balance");
    }
}
