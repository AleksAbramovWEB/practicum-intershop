package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ProductControllerTest extends AbstractIntegrationTest {


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
                            INSERT INTO product (id, title, img_path, price, description)
                            VALUES (300, 'Пряник', '/images/test.png', 100.00, 'Описание товара')
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
    void getProducts_shouldReturnMainPageWithProducts() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/")
                        .queryParam("pageNumber", "0")
                        .queryParam("pageSize", "10")
                        .queryParam("sort", "NO")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class);
    }

    @Test
    void getProduct_shouldReturnProductPage() {
        webTestClient.get()
                .uri("/product/200")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class);
    }

    @Test
    void addProductForm_shouldReturnProductFormPage() {
        webTestClient.get()
                .uri("/product/new")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class);
    }
}
