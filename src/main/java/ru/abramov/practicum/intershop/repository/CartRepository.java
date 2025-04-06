package ru.abramov.practicum.intershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Product;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByProduct(Product product);
}
