package ru.abramov.practicum.pay;

import org.junit.jupiter.api.Test;
import ru.abramov.practicum.pay.model.PaymentRequest;

import java.math.BigDecimal;

class PayApiTest extends AbstractApiTest {

    @Test
    void payPost_shouldSucceed() {

        webTestClient.post()
                .uri("/pay")
                .bodyValue(new PaymentRequest().amount(BigDecimal.valueOf(50)))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true);
    }

    @Test
    void payPost_shouldReturnError_ifInsufficientFunds() {

        webTestClient.post()
                .uri("/pay")
                .bodyValue(new PaymentRequest().amount(BigDecimal.valueOf(5000000000L)))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }
}

