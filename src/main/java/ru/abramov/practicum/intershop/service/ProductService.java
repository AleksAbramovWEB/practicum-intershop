package ru.abramov.practicum.intershop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.model.ProductPage;

public interface ProductService {
    Mono<ProductPage> getProductsWithCount(String search, String sort, int page, int size);

    Mono<Void> addProduct(Product product);

    Mono<Product> getProduct(Long id);
}
