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

    @Query("SELECT p.*, coalesce(c.count, 0) as count  FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "WHERE p.title ILIKE concat('%', :title, '%') " +
            "ORDER BY p.title LIMIT :limit OFFSET :offset ")
    Flux<Product> searchByTitleAlpha(@Param("title") String title, @Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT p.*, coalesce(c.count, 0) as count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "WHERE p.title ILIKE concat('%', :title, '%') " +
            "ORDER BY p.price LIMIT :limit OFFSET :offset ")
    Flux<Product> searchByTitlePrice(@Param("title") String title, @Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT p.*, coalesce(c.count, 0) as count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "ORDER BY p.title  LIMIT :limit OFFSET :offset")
    Flux<Product> findAllAlpha(@Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT p.*, coalesce(c.count, 0) as count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "ORDER BY p.price LIMIT :limit OFFSET :offset")
    Flux<Product> findAllPrice(@Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT p.*, coalesce(c.count, 0) as count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "LIMIT :limit OFFSET :offset")
    Flux<Product> findAllPaged(@Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT * FROM product WHERE title = :title")
    Mono<Product> findByTitle(@Param("title") String title);

    @Query("SELECT p.*, c.count FROM product p " +
            "INNER JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id ")
    Flux<Product> findAllInCart();

    @Query("SELECT p.*, coalesce(c.count, 0) as count FROM product p " +
            "LEFT JOIN (SELECT product_id, count(*) as count FROM cart GROUP BY product_id) c on c.product_id = p.id " +
            "WHERE p.id = :productId")
    Mono<Product> findByIdWithCountCart(@Param("productId") Long productId);

    Mono<Long> count();

    @Query("SELECT COUNT(*) FROM product WHERE title ILIKE concat('%', :title, '%')")
    Mono<Long> countByTitleContainingIgnoreCase(String title);
}
