package ru.abramov.practicum.intershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.abramov.practicum.intershop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
