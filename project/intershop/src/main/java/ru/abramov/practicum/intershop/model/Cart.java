package ru.abramov.practicum.intershop.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Table("cart")
public class Cart {

    @Id
    private Long id;

    @NotNull
    @Column("product_id")
    private Long productId;

    @NotNull
    @Column("user_id")
    private String userId;
}
