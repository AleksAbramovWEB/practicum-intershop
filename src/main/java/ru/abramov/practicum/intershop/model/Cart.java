package ru.abramov.practicum.intershop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Table("cart")
public class Cart {

    @Id
    private Long id;

    @Column("product_id")
    private Long productId;
}
