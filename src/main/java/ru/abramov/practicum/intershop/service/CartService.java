package ru.abramov.practicum.intershop.service;

import reactor.core.publisher.Flux;
import ru.abramov.practicum.intershop.model.Product;

public interface CartService {

    Flux<Product> getProductsInCart();

    void minus(Long productId);

    void plus(Long productId);

    void delete(Long productId);
}
