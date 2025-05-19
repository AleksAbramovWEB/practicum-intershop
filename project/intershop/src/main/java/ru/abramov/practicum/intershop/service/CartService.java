package ru.abramov.practicum.intershop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Product;

public interface CartService {

    Flux<Product> getProductsInCart(String userId);

    Mono<Void> minus(Long productId, String userId);

    Mono<Void> plus(Long productId, String userId);

    Mono<Void> delete(Long productId, String userId);
}
