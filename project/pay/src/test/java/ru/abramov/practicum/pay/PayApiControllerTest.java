package ru.abramov.practicum.pay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.abramov.practicum.pay.model.PaymentRequest;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureWebTestClient
class PayApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

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

