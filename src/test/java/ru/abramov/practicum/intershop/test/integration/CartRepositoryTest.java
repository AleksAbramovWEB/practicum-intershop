package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.repository.ProductRepository;

import java.math.BigDecimal;

public class CartRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findAllByProductId_shouldReturnCartsForProduct() {
        Product product = new Product();
        product.setTitle("Пряник");
        product.setPrice(new BigDecimal("100.0"));
        product.setImgPath("image/path/to/pryanik.jpg");
        product.setDescription("Вкусный пряник");

        productRepository.save(product)
                .flatMapMany(savedProduct -> {
                    Long productId = savedProduct.getId();
                    Cart cart1 = new Cart();
                    cart1.setProductId(productId);
                    Cart cart2 = new Cart();
                    cart2.setProductId(productId);

                    return cartRepository.saveAll(Flux.just(cart1, cart2))
                            .thenMany(cartRepository.findAllByProductId(productId));
                })
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void deleteAllByProductId_shouldDeleteAllCartsForProduct() {
        Product product = new Product();
        product.setTitle("Пирог");
        product.setPrice(new BigDecimal("150.0"));
        product.setImgPath("image/path/to/pirog.jpg");
        product.setDescription("Вкусный пирог");

        productRepository.save(product)
                .flatMapMany(savedProduct -> {
                    Long productId = savedProduct.getId();
                    Cart cart1 = new Cart();
                    cart1.setProductId(productId);
                    Cart cart2 = new Cart();
                    cart2.setProductId(productId);

                    return cartRepository.saveAll(Flux.just(cart1, cart2))
                            .thenMany(cartRepository.findAllByProductId(productId)
                                    .flatMap(cartRepository::delete)
                                    .thenMany(cartRepository.findAllByProductId(productId)));
                })
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
