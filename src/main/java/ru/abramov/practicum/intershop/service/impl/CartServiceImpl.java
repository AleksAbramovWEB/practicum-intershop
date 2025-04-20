package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.service.CartService;
import ru.abramov.practicum.intershop.service.ProductService;

import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductService productService;

    private final CartRepository cartRepository;

    @Override
    @Transactional
    public Flux<Product> getProductsInCart() {
        return cartRepository.findAll()
    }

    @Override
    public void minus(Long productId) {
        Product product = productService.getProduct(productId);

        cartRepository.findAllByProduct(product)
                .stream()
                .findFirst()
                .ifPresent(cartRepository::delete);
    }

    @Override
    public void plus(Long productId) {
        Product product = productService.getProduct(productId);

        Cart cart = new Cart();
        cart.setProduct(product);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void delete(Long productId) {
        Product product = productService.getProduct(productId);

        cartRepository.deleteAllByProduct(product);
    }
}
