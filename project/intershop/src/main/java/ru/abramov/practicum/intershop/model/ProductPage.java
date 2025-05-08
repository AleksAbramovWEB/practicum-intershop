package ru.abramov.practicum.intershop.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductPage implements Serializable {
    private final List<Product> products;
    private final long totalElements;
}