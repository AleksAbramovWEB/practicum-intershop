package ru.abramov.practicum.intershop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Product;

public interface CartService {

    Flux<Product> getProductsInCart();

    Mono<Void> minus(Long productId);

    Mono<Void> plus(Long productId);

    Mono<Void> delete(Long productId);
}
