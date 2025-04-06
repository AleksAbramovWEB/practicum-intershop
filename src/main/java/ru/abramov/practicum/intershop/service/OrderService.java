package ru.abramov.practicum.intershop.service;

import ru.abramov.practicum.intershop.model.Order;

import java.util.List;

public interface OrderService {

    Order create();

    List<Order> getOrderList();

    Order getOrder(Long id);
}
