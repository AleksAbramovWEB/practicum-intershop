package ru.abramov.practicum.intershop.service;

import org.springframework.data.domain.Page;
import ru.abramov.practicum.intershop.model.Product;

public interface ProductService {
    Page<Product> getProducts(String search, String sort, int page, int size);

    void addProduct(Product product);

    Product getProduct(Long id);
}
