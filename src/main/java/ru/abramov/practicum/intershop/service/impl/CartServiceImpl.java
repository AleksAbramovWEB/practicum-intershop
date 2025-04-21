package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Flux<Product> getProductsInCart() {
        return productRepository.findAllInCart();
    }

    @Override
    public Mono<Void> minus(Long productId) {
        return productService.getProduct(productId)
                .map(Product::getId)
                .flatMapMany(cartRepository::findAllByProductId)
                .next()
                .flatMap(cartRepository::delete)
                .then();
    }

    @Override
    public Mono<Void> plus(Long productId) {
        return productService.getProduct(productId)
                .map(product -> {
                    Cart cart = new Cart();
                    cart.setProductId(product.getId());
                    return cart;
                })
                .flatMap(cartRepository::save)
                .then();
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long productId) {
        return productService.getProduct(productId)
                .map(Product::getId)
                .flatMapMany(cartRepository::findAllByProductId)
                .flatMap(cartRepository::delete)
                .then();
    }
}
