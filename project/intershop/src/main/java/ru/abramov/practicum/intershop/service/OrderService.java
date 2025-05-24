package ru.abramov.practicum.intershop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Order;


public interface OrderService {

    Mono<Order> create(String userId);

    Flux<Order> getOrderList(String userId);

    Mono<Order> getOrder(Long id, String userId);
}
