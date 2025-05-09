package ru.abramov.practicum.intershop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Transient;
import org.springframework.http.codec.multipart.FilePart;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Table("product")
public class Product implements Serializable {

    @Id
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private BigDecimal price;

    private String description;

    @NotNull
    @Length(max = 2000)
    @Column("img_path")
    private String imgPath;

    @ReadOnlyProperty
    private Integer count = 0;

    @Transient
    private FilePart image;
}
