package ru.abramov.practicum.intershop.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.abramov.practicum.intershop.model.Cart;

@Repository
public interface CartRepository extends R2dbcRepository<Cart, Long> {

    Flux<Cart> findAllByProductIdAndUserId(Long productId, String userId);
    Flux<Cart> findAllByUserId(String userId);
}
