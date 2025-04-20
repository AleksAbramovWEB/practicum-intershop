package ru.abramov.practicum.intershop.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Product;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {

    @Query("SELECT p.*, c.count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "WHERE p.title ILIKE concat('%', :title, '%') " +
            "ORDER BY p.title LIMIT :limit OFFSET :offset ")
    Flux<Product> searchByTitleAlpha(@Param("title") String title, @Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT p.*, c.count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "WHERE p.title ILIKE concat('%', :title, '%') " +
            "ORDER BY p.price LIMIT :limit OFFSET :offset ")
    Flux<Product> searchByTitlePrice(@Param("title") String title, @Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT p.*, c.count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "ORDER BY p.title  LIMIT :limit OFFSET :offset")
    Flux<Product> findAllAlpha(@Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT p.*, c.count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "ORDER BY p.price LIMIT :limit OFFSET :offset")
    Flux<Product> findAllPrice(@Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT p.*, c.count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "LIMIT :limit OFFSET :offset")
    Flux<Product> findAllPaged(@Param("offset") long offset, @Param("limit") int limit);

    Mono<Product> findByTitle(String title);
}
