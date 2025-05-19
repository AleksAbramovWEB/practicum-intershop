package ru.abramov.practicum.intershop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Table("orders")
public class Order {

    @Id
    private Long id;

    @NotNull
    @Column("total_sum")
    private BigDecimal totalSum;

    @NotNull
    @Column("user_id")
    private String userId;

    @Transient
    private List<OrderItem> orderItems = new ArrayList<>();
}