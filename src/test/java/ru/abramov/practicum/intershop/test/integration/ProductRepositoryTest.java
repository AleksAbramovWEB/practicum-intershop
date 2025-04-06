package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.ProductRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class ProductRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveProduct_shouldSaveProduct() {
        Product product = new Product();
        product.setTitle("Пряник");
        product.setDescription("Вкусный пряник");
        product.setPrice(new BigDecimal("100.0"));
        product.setImgPath("/image/path/to/pryanik.jpg");

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getTitle()).isEqualTo("Пряник");
    }

    @Test
    void findByTitle_shouldReturnProduct() {
        Product product = new Product();
        product.setTitle("Пряник");
        product.setDescription("Вкусный пряник");
        product.setPrice(new BigDecimal("100.0"));
        product.setImgPath("/image/path/to/pryanik.jpg");
        productRepository.save(product);

        Product foundProduct = productRepository.findByTitle("Пряник");

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getTitle()).isEqualTo("Пряник");
    }

    @Test
    void findByTitleContainingIgnoreCase_shouldReturnPageOfProducts() {
        Product product1 = new Product();
        product1.setTitle("Пряник");
        product1.setDescription("Вкусный пряник");
        product1.setPrice(new BigDecimal("100.0"));
        product1.setImgPath("/image/path/to/pryanik.jpg");
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setTitle("Пирог");
        product2.setDescription("Вкусный пирог");
        product2.setPrice(new BigDecimal("120.0"));
        product2.setImgPath("/image/path/to/pirog.jpg");
        productRepository.save(product2);

        Page<Product> productsPage = productRepository.findByTitleContainingIgnoreCase("пря", Pageable.unpaged());

        assertThat(productsPage).isNotNull();
        assertThat(productsPage.getTotalElements()).isEqualTo(1);
        assertThat(productsPage.getContent()).hasSize(1);
        assertThat(productsPage.getContent().getFirst().getTitle()).isEqualTo("Пряник");
    }
}
