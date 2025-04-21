package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.ProductRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        databaseClient.sql("DELETE FROM order_item").then()
                .then(databaseClient.sql("DELETE FROM orders").then())
                .then(databaseClient.sql("DELETE FROM cart").then())
                .then(databaseClient.sql("DELETE FROM product").then())
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
    void saveProduct_shouldSaveProduct() {
        Product product = new Product();
        product.setTitle("Пряник");
        product.setDescription("Вкусный пряник");
        product.setPrice(new BigDecimal("100.0"));
        product.setImgPath("/image/path/to/pryanik.jpg");

        Mono<Product> savedProductMono = productRepository.save(product);

        StepVerifier.create(savedProductMono)
                .assertNext(savedProduct -> {
                    assertThat(savedProduct).isNotNull();
                    assertThat(savedProduct.getId()).isNotNull();
                    assertThat(savedProduct.getTitle()).isEqualTo("Пряник");
                })
                .verifyComplete();
    }

    @Test
    void findByTitle_shouldReturnProduct() {
        Product product = new Product();
        product.setTitle("Пряник");
        product.setDescription("Вкусный пряник");
        product.setPrice(new BigDecimal("100.0"));
        product.setImgPath("/image/path/to/pryanik.jpg");

        productRepository.save(product).block();

        Mono<Product> foundProductMono = productRepository.findByTitle("Пряник");

        StepVerifier.create(foundProductMono)
                .assertNext(foundProduct -> {
                    assertThat(foundProduct).isNotNull();
                    assertThat(foundProduct.getTitle()).isEqualTo("Пряник");
                })
                .verifyComplete();
    }

    @Test
    void findByTitleContainingIgnoreCase_shouldReturnPageOfProducts() {
        Product product1 = new Product();
        product1.setTitle("Пряник");
        product1.setDescription("Вкусный пряник");
        product1.setPrice(new BigDecimal("100.0"));
        product1.setImgPath("/image/path/to/pryanik.jpg");

        Product product2 = new Product();
        product2.setTitle("Пирог");
        product2.setDescription("Вкусный пирог");
        product2.setPrice(new BigDecimal("120.0"));
        product2.setImgPath("/image/path/to/pirog.jpg");

        productRepository.save(product1).block();
        productRepository.save(product2).block();

        Mono<Long> countMono = productRepository.countByTitleContainingIgnoreCase("пря");

        StepVerifier.create(countMono)
                .assertNext(count -> assertThat(count).isEqualTo(1L))
                .verifyComplete();
    }
}
