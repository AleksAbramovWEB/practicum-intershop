package ru.abramov.practicum.intershop.repository;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Order;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, Long> {

    Mono<Order> findTopByOrderByIdDesc();
}
