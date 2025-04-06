package ru.abramov.practicum.intershop.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.ProductRepository;
import ru.abramov.practicum.intershop.service.ImageService;
import ru.abramov.practicum.intershop.service.ProductService;
import ru.abramov.practicum.intershop.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = ProductServiceUnitTest.Config.class)
class ProductServiceUnitTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setImgPath("path/to/image");

        reset(productRepository, imageService);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(imageService.save(any())).thenReturn(Optional.of("path/to/image"));
    }

    @Test
    void getProduct_ShouldReturnProduct_WhenProductExists() {
        Product result = productService.getProduct(1L);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getTitle(), result.getTitle());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProduct(1L);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void addProduct_ShouldSaveProduct_WhenImageIsValid() {
        Product newProduct = new Product();
        newProduct.setTitle("New Product");
        newProduct.setPrice(BigDecimal.valueOf(200.00));
        newProduct.setImgPath("new/image/path");

        when(imageService.save(any())).thenReturn(Optional.of("new/image/path"));

        productService.addProduct(newProduct);

        assertNotNull(newProduct.getImgPath());
        verify(imageService, times(1)).save(any());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void addProduct_ShouldThrowException_WhenImageIsNull() {
        Product newProduct = new Product();
        newProduct.setTitle("New Product Without Image");
        newProduct.setPrice(BigDecimal.valueOf(200.00));

        when(imageService.save(any())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(newProduct);
        });

        assertEquals("Image is required", exception.getMessage());
        verify(imageService, times(1)).save(any());
        verify(productRepository, never()).save(newProduct);
    }

    @Configuration
    static class Config {

        @Bean
        public ProductService productService(ProductRepository productRepository, ImageService imageService) {
            return new ProductServiceImpl(productRepository, imageService);
        }

        @Bean
        public ProductRepository productRepository() {
            return mock(ProductRepository.class);
        }

        @Bean
        public ImageService imageService() {
            return mock(ImageService.class);
        }
    }
}
