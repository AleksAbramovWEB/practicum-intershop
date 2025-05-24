package ru.abramov.practicum.intershop.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.ProductRepository;
import ru.abramov.practicum.intershop.service.ImageService;
import ru.abramov.practicum.intershop.service.ProductService;
import ru.abramov.practicum.intershop.service.impl.ProductServiceImpl;

import java.math.BigDecimal;

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

    private final String userId = "user-42";

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setImgPath("path/to/image");

        reset(productRepository, imageService);

        when(productRepository.findByIdWithCountCart(1L, userId)).thenReturn(Mono.just(product));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));
        when(imageService.save(any())).thenReturn(Mono.just("path/to/image"));
    }

    @Test
    void getProduct_ShouldReturnProduct_WhenProductExists() {
        StepVerifier.create(productService.getProduct(1L, userId))
                .expectNextMatches(p ->
                        p.getId().equals(product.getId()) &&
                                p.getTitle().equals(product.getTitle()))
                .verifyComplete();

        verify(productRepository, times(1)).findByIdWithCountCart(1L, userId);
    }

    @Test
    void getProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findByIdWithCountCart(1L, userId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.getProduct(1L, userId))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Product not found"))
                .verify();

        verify(productRepository, times(1)).findByIdWithCountCart(1L, userId);
    }

    @Test
    void addProduct_ShouldSaveProduct_WhenImageIsValid() {
        Product newProduct = new Product();
        newProduct.setTitle("New Product");
        newProduct.setPrice(BigDecimal.valueOf(200.00));

        when(imageService.save(any())).thenReturn(Mono.just("new/image/path"));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(newProduct));

        StepVerifier.create(productService.addProduct(newProduct))
                .verifyComplete();

        verify(imageService, times(1)).save(any());
        verify(productRepository, times(1)).save(newProduct);
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

        @Bean
        public Environment environment() {
            return mock(Environment.class);
        }

        @Bean
        public FilePart filePart() {
            return mock(FilePart.class);
        }
    }
}
