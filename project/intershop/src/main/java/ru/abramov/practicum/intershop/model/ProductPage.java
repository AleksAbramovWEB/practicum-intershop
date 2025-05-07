package ru.abramov.practicum.intershop.model;

import lombok.Data;

import java.util.List;

@Data
public class ProductPage {
    private final List<Product> products;
    private final long totalElements;
}