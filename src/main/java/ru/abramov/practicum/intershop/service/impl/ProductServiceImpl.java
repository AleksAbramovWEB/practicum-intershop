package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.ProductRepository;
import ru.abramov.practicum.intershop.service.ImageService;
import ru.abramov.practicum.intershop.service.ProductService;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ImageService imageService;

    @Override
    public Flux<Product> getProducts(String search, String sort, int page, int size) {
        long offset = (long) page * size;

        if (search != null && !search.isBlank()) {
            return switch (sort.toUpperCase()) {
                case "ALPHA" -> productRepository.searchByTitleAlpha(search, offset, size);
                case "PRICE" -> productRepository.searchByTitlePrice(search, offset, size);
                default -> productRepository.searchByTitleAlpha(search, offset, size);
            };
        } else {
            return switch (sort.toUpperCase()) {
                case "ALPHA" -> productRepository.findAllAlpha(offset, size);
                case "PRICE" -> productRepository.findAllPrice(offset, size);
                default -> productRepository.findAllPaged(offset, size);
            };
        }
    }

    @Override
    public Mono<Void> addProduct(Product product) {
        return imageService.save(product.getImage())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Image is required")))
                .doOnNext(product::setImgPath)
                .then(productRepository.save(product))
                .then();
    }

    @Override
    public Mono<Product> getProduct(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")));
    }
}
