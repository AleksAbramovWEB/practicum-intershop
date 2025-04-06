package ru.abramov.practicum.intershop.service;

import ru.abramov.practicum.intershop.model.Product;

import java.util.List;

public interface CartService {

    List<Product> getProductsInCart();

    void minus(Long productId);

    void plus(Long productId);

    void delete(Long productId);
}
