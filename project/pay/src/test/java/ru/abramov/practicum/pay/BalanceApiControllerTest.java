package ru.abramov.practicum.pay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.abramov.practicum.pay.api.BalanceApiController;
import ru.abramov.practicum.pay.handler.impl.AccountHandlerImpl;

@WebFluxTest(controllers = BalanceApiController.class)
@Import({AccountHandlerImpl.class})
class BalanceApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

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
