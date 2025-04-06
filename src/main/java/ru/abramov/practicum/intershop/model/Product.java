package ru.abramov.practicum.intershop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private BigDecimal price;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Length(max = 2000)
    private String imgPath;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Cart> carts = new ArrayList<>();

    public Integer getCount() {
        return carts.size();
    }

    @Transient
    private MultipartFile image;
}
