package ru.abramov.practicum.intershop.service;

public interface CartService {

    void minus(Long productId);

    void plus(Long productId);
}
