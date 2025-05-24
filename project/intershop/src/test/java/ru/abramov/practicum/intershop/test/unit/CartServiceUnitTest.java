package ru.abramov.practicum.intershop.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.repository.ProductRepository;
import ru.abramov.practicum.intershop.service.CartService;
import ru.abramov.practicum.intershop.service.ProductService;
import ru.abramov.practicum.intershop.service.impl.CartServiceImpl;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = CartServiceUnitTest.Config.class)
class CartServiceUnitTest {

    private final Long productId = 1L;

    private final String userId = "user-42";

    private Product product;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(productId);
        product.setTitle("Test Product");
        product.setPrice(new BigDecimal("100.00"));
        product.setImgPath("img.jpg");

        when(productService.getProduct(productId, userId)).thenReturn(Mono.just(product));
    }

    @Test
    void getProductsInCart_ShouldReturnFluxOfProducts() {
        when(productRepository.findAllInCart(userId)).thenReturn(Flux.just(product));

        StepVerifier.create(cartService.getProductsInCart(userId))
                .expectNext(product)
                .verifyComplete();

        verify(productRepository, times(1)).findAllInCart(userId);
    }

    @Test
    void minus_ShouldDeleteOneCartItem() {
        Cart cart = new Cart();
        cart.setId(42L);
        cart.setProductId(productId);

        when(cartRepository.findAllByProductIdAndUserId(productId, userId)).thenReturn(Flux.just(cart));
        when(cartRepository.delete(cart)).thenReturn(Mono.empty());

        StepVerifier.create(cartService.minus(productId, userId))
                .verifyComplete();

        verify(cartRepository).delete(cart);
    }

    @Test
    void plus_ShouldSaveCartItem() {
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.empty());

        StepVerifier.create(cartService.plus(productId, userId))
                .verifyComplete();

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void delete_ShouldRemoveAllCartItemsForProduct() {
        Cart cart1 = new Cart();
        cart1.setProductId(productId);

        Cart cart2 = new Cart();
        cart2.setProductId(productId);

        when(cartRepository.findAllByProductIdAndUserId(productId, userId)).thenReturn(Flux.just(cart1, cart2));
        when(cartRepository.delete(any())).thenReturn(Mono.empty());

        StepVerifier.create(cartService.delete(productId, userId))
                .verifyComplete();

        verify(cartRepository, times(2)).delete(any());
    }

    @Configuration
    static class Config {
        @Bean
        public CartService cartService(ProductService productService, CartRepository cartRepository, ProductRepository productRepository) {
            return new CartServiceImpl(productService, cartRepository, productRepository);
        }

        @Bean
        public CartRepository cartRepository() {
            return mock(CartRepository.class);
        }

        @Bean
        public ProductService productService() {
            return mock(ProductService.class);
        }

        @Bean
        public ProductRepository productRepository() {
            return mock(ProductRepository.class);
        }
    }
}
