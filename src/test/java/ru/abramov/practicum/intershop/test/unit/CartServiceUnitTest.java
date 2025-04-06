package ru.abramov.practicum.intershop.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.service.CartService;
import ru.abramov.practicum.intershop.service.ProductService;
import ru.abramov.practicum.intershop.service.impl.CartServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = CartServiceUnitTest.Config.class)
class CartServiceUnitTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(new BigDecimal("100.00"));
        product.setImgPath("path/to/image");

        when(productService.getProduct(1L)).thenReturn(product);
    }

    @Test
    void getProductsInCart_ShouldReturnListOfDistinctProducts() {
        Cart cart1 = new Cart();
        cart1.setProduct(product);

        Cart cart2 = new Cart();
        cart2.setProduct(product);

        when(cartRepository.findAll()).thenReturn(List.of(cart1, cart2));

        List<Product> result = cartService.getProductsInCart();

        assertEquals(1, result.size());  // Ожидаем 1 уникальный продукт
        verify(cartRepository, times(1)).findAll();
    }

    @Test
    void minus_ShouldDeleteProductFromCart() {
        Cart cart = new Cart();
        cart.setProduct(product);
        when(cartRepository.findAllByProduct(product)).thenReturn(List.of(cart));

        cartService.minus(1L);

        verify(cartRepository, times(1)).delete(cart);
    }

    @Test
    void plus_ShouldSaveProductToCart() {
        Cart cart = new Cart();
        cart.setProduct(product);

        cartService.plus(1L);

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void delete_ShouldDeleteAllCartsWithProduct() {
        Cart cart = new Cart();
        cart.setProduct(product);
        when(cartRepository.findAllByProduct(product)).thenReturn(List.of(cart));

        cartService.delete(1L);

        verify(cartRepository, times(1)).deleteAllByProduct(product);
    }

    @Configuration
    static class Config {

        @Bean
        public CartService cartService(CartRepository cartRepository, ProductService productService) {
            return new CartServiceImpl(productService, cartRepository);
        }

        @Bean
        public CartRepository cartRepository() {
            return mock(CartRepository.class);
        }

        @Bean
        public ProductService productService() {
            return mock(ProductService.class);
        }
    }
}
