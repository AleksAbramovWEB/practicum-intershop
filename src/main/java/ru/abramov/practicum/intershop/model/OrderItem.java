package ru.abramov.practicum.intershop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Table("order_item")
public class OrderItem {

    @Id
    private Long id;

    @Column("product_id")
    private Long productId;

    @Column("order_id")
    private Long orderId;

    @NotNull
    private Integer count;

    @NotNull
    @Column("total_sum")
    private BigDecimal totalSum;
}