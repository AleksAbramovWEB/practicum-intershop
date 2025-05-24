package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.abramov.practicum.intershop.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class CartControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        databaseClient.sql("DELETE FROM order_item").then()
                .then(databaseClient.sql("DELETE FROM orders").then())
                .then(databaseClient.sql("DELETE FROM cart").then())
                .then(databaseClient.sql("DELETE FROM product").then())
                .then(databaseClient.sql("""
            INSERT INTO product (id, title, img_path, price, description)
            VALUES (100, 'Тестовый товар', '/images/test.png', 150.00, 'Описание')
        """).then())
                .then(databaseClient.sql("""
            INSERT INTO cart (id, product_id, user_id)
            VALUES (200, 100, 'user-42'),
                   (201, 100, 'user-42')
        """).then())
                .block();
    }

    @AfterEach
    public void cleanup() {
        Mono.when(
                databaseClient.sql("DELETE FROM cart").then(),
                databaseClient.sql("DELETE FROM product").then(),
                databaseClient.sql("DELETE FROM order_item").then(),
                databaseClient.sql("DELETE FROM orders").then()
        ).block();
    }

    @Test
    void getCart_shouldReturnCartPageWithItemsAndTotal() {
        webTestClient.mutateWith(getMockJwt())
                .get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class);
    }

    @Test
    void postPlusAction_shouldIncrementProductCount() {
        webTestClient.mutateWith(getMockJwt())
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/product/100/cart/plus")
                .header("Referer", "/cart")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/cart");

        StepVerifier.create(productRepository.findByIdWithCountCart(100L, USER_ID))
                .assertNext(product -> assertThat(product.getCount()).isEqualTo(3))
                .verifyComplete();
    }

    @Test
    void postMinusAction_shouldDecrementProductCount() {
        webTestClient.mutateWith(getMockJwt())
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/product/100/cart/minus")
                .header("Referer", "/cart")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/cart");

        StepVerifier.create(productRepository.findByIdWithCountCart(100L, USER_ID))
                .assertNext(product -> assertThat(product.getCount()).isEqualTo(1))
                .verifyComplete();
    }

    @Test
    void postDeleteAction_shouldClearProductFromCart() {
        webTestClient.mutateWith(getMockJwt())
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/product/100/cart/delete")
                .header("Referer", "/cart")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/cart");

        StepVerifier.create(productRepository.findByIdWithCountCart(100L, USER_ID))
                .assertNext(product -> assertThat(product.getCount()).isZero())
                .verifyComplete();
    }
}
