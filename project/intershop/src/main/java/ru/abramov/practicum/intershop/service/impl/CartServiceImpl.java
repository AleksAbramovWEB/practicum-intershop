package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.repository.ProductRepository;
import ru.abramov.practicum.intershop.service.CartService;
import ru.abramov.practicum.intershop.service.ProductService;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductService productService;

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    @Override
    public Flux<Product> getProductsInCart(String userId) {
        return productRepository.findAllInCart(userId);
    }

    @Override
    public Mono<Void> minus(Long productId, String userId) {
        return productService.getProduct(productId, userId)
                .map(Product::getId)
                .flatMapMany( id -> cartRepository.findAllByProductIdAndUserId(id, userId))
                .next()
                .flatMap(cartRepository::delete)
                .then();
    }

    @Override
    public Mono<Void> plus(Long productId, String userId) {
        return productService.getProduct(productId, userId)
                .map(product -> {
                    Cart cart = new Cart();
                    cart.setProductId(product.getId());
                    cart.setUserId(userId);
                    return cart;
                })
                .flatMap(cartRepository::save)
                .then();
    }

    @Override
    public Mono<Void> delete(Long productId, String userId) {
        return productService.getProduct(productId, userId)
                .map(Product::getId)
                .flatMapMany( id -> cartRepository.findAllByProductIdAndUserId(id, userId))
                .flatMap(cartRepository::delete)
                .then();
    }
}
