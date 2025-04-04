package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.ProductRepository;
import ru.abramov.practicum.intershop.service.ProductService;

@Component
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getProducts(String search, String sort, int page, int size) {
        Pageable pageable = switch (sort) {
            case "ALPHA" -> PageRequest.of(page, size, Sort.by("title"));
            case "PRICE" -> PageRequest.of(page, size, Sort.by("price"));
            default -> PageRequest.of(page, size);
        };

        if (search != null && !search.isBlank()) {
            return productRepository.findByTitleContainingIgnoreCase(search, pageable);
        }

        return productRepository.findAll(pageable);
    }
}
