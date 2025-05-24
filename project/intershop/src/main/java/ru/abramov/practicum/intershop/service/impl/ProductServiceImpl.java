package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.model.ProductPage;
import ru.abramov.practicum.intershop.repository.ProductRepository;
import ru.abramov.practicum.intershop.service.ImageService;
import ru.abramov.practicum.intershop.service.ProductService;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ImageService imageService;

    @Override
    @Cacheable(value = "products", key = "#search + '_' + #sort + '_' + #page + '_' + #size")
    public Mono<ProductPage> getProductsWithCount(String search, String sort, int page, int size, String userId) {
        long offset = (long) page * size;

        Mono<Long> totalMono = (search != null && !search.isBlank())
                ? productRepository.countByTitleContainingIgnoreCase(search)
                : productRepository.count();

        Flux<Product> productFlux = switch (sort.toUpperCase()) {
            case "ALPHA" -> (search != null && !search.isBlank())
                    ? productRepository.searchByTitleAlpha(search, offset, size, userId)
                    : productRepository.findAllAlpha(offset, size, userId);

            case "PRICE" -> (search != null && !search.isBlank())
                    ? productRepository.searchByTitlePrice(search, offset, size, userId)
                    : productRepository.findAllPrice(offset, size, userId);

            default -> (search != null && !search.isBlank())
                    ? productRepository.searchByTitleAlpha(search, offset, size, userId)
                    : productRepository.findAllPaged(offset, size, userId);
        };

        return Mono.zip(productFlux.collectList(), totalMono)
                .map(tuple -> new ProductPage(tuple.getT1(), tuple.getT2()));
    }

    @Override
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public Mono<Void> addProduct(Product product) {
        return imageService.save(product.getImage())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Image is required")))
                .doOnNext(product::setImgPath)
                .then(productRepository.save(product))
                .then();
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public Mono<Product> getProduct(Long id, String userId) {
        return productRepository.findByIdWithCountCart(id, userId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")));
    }
}
