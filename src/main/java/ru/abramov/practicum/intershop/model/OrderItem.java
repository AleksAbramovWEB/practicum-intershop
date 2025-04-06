package ru.abramov.practicum.intershop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Data
@ToString(exclude = {"product", "order"})
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_id_seq")
    @SequenceGenerator(name = "order_item_id_seq", sequenceName = "order_item_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_item__product"))
    private Product product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_item__orders"))
    private Order order;

    @NotNull
    private Integer count;

    @NotNull
    private BigDecimal totalSum;
}
