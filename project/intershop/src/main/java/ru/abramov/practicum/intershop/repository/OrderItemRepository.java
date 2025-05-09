package ru.abramov.practicum.intershop.repository;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Order;
import ru.abramov.practicum.intershop.model.OrderItem;

@Repository
public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {

    Flux<OrderItem> findByOrderId(Long orderId);
}
