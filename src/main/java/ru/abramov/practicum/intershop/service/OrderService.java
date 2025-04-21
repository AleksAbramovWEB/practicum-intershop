package ru.abramov.practicum.intershop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Order;


public interface OrderService {

    Mono<Order> create();

    Flux<Order> getOrderList();

    Mono<Order> getOrder(Long id);
}
