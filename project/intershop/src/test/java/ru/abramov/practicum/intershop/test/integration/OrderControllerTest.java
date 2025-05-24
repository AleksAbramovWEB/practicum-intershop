package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import reactor.test.StepVerifier;
import ru.abramov.practicum.intershop.repository.OrderRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderControllerTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        databaseClient.sql("DELETE FROM order_item").then()
                .then(databaseClient.sql("DELETE FROM orders").then())
                .then(databaseClient.sql("DELETE FROM cart").then())
                .then(databaseClient.sql("DELETE FROM product").then())
                .then(databaseClient.sql("""
                INSERT INTO product (id, title, img_path, price, description)
                VALUES (200, 'Продукт для заказа', '/images/test.png', 500.00, 'Описание товара')
            """).then())
                .then(databaseClient.sql("""
                INSERT INTO cart (id, product_id, user_id)
                VALUES (1000, 200, 'user-42')
            """).then())
                .then(databaseClient.sql("""
                INSERT INTO orders (id, total_sum, user_id) 
                VALUES (300, 1000.00, 'user-42')
            """).then())
                .then(databaseClient.sql("""
                INSERT INTO order_item (id, order_id, product_id, count, total_sum)
                VALUES (400, 300, 200, 2, 1000.00)
            """).then())
                .block();
    }

    @AfterEach
    public void cleanup() {
        databaseClient.sql("DELETE FROM order_item").then()
                .then(databaseClient.sql("DELETE FROM orders").then())
                .then(databaseClient.sql("DELETE FROM cart").then())
                .then(databaseClient.sql("DELETE FROM product").then())
                .block();
    }

    @Test
    void getOrders_shouldReturnOrdersPageWithOrderList() {
        webTestClient.mutateWith(getMockJwt())
                .get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class);
    }

    @Test
    void getOrder_shouldReturnOrderPageById() {
        webTestClient.mutateWith(getMockJwt())
                .get()
                .uri("/order/300")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class);
    }

    @Test
    void getOrder_withCreatedParam_shouldIncludeNewOrderAttribute() {
        webTestClient.mutateWith(getMockJwt())
                .get()
                .uri("/order/300?created=300")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class);
    }

    @Test
    void postOrder_shouldCreateNewOrderAndRedirectToIt() {
        webTestClient.mutateWith(getMockJwt())
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/order")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", "/order/\\d+\\?created=\\d+");

        StepVerifier.create(orderRepository.findTopByOrderByIdDesc())
                .assertNext(order -> {
                    assertThat(order).isNotNull();
                    assertThat(order.getId()).isNotNull();
                })
                .verifyComplete();
    }
}
