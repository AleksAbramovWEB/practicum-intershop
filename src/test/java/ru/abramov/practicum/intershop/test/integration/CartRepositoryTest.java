package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class CartRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findAllByProduct_shouldReturnCartsForProduct() {
        Product product = new Product();
        product.setTitle("Пряник");
        product.setPrice(new BigDecimal("100.0"));
        product.setImgPath("image/path/to/pryanik.jpg");
        product.setDescription("Вкусный пряник");
        productRepository.save(product);

        // Создаем корзины для продукта
        Cart cart1 = new Cart();
        cart1.setProduct(product);
        cartRepository.save(cart1);

        Cart cart2 = new Cart();
        cart2.setProduct(product);
        cartRepository.save(cart2);

        List<Cart> carts = cartRepository.findAllByProduct(product);

        assertThat(carts).isNotEmpty();
        assertThat(carts).hasSize(2);
        assertThat(carts.get(0).getProduct().getTitle()).isEqualTo("Пряник");
        assertThat(carts.get(1).getProduct().getTitle()).isEqualTo("Пряник");
    }

    @Test
    void deleteAllByProduct_shouldDeleteAllCartsForProduct() {
        Product product = new Product();
        product.setTitle("Пирог");
        product.setPrice(new BigDecimal("150.0"));
        product.setImgPath("image/path/to/pirog.jpg");
        product.setDescription("Вкусный пирог");
        productRepository.save(product);

        Cart cart1 = new Cart();
        cart1.setProduct(product);
        cartRepository.save(cart1);

        Cart cart2 = new Cart();
        cart2.setProduct(product);
        cartRepository.save(cart2);

        cartRepository.deleteAllByProduct(product);

        List<Cart> cartsAfterDelete = cartRepository.findAllByProduct(product);

        assertThat(cartsAfterDelete).isEmpty();
    }
}

