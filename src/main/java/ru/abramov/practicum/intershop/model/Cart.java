package ru.abramov.practicum.intershop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(indexes = {
        @Index(name = "idx_cart__product", columnList = "product_id"),
})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_id_seq")
    @SequenceGenerator(name = "cart_id_seq", sequenceName = "cart_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_cart__product"))
    private Product product;
}