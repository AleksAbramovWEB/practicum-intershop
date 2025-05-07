package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
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
    public Mono<ProductPage> getProductsWithCount(String search, String sort, int page, int size) {
        long offset = (long) page * size;

        Mono<Long> totalMono = (search != null && !search.isBlank())
                ? productRepository.countByTitleContainingIgnoreCase(search)
                : productRepository.count();

        Flux<Product> productFlux = switch (sort.toUpperCase()) {
            case "ALPHA" -> (search != null && !search.isBlank())
                    ? productRepository.searchByTitleAlpha(search, offset, size)
                    : productRepository.findAllAlpha(offset, size);

            case "PRICE" -> (search != null && !search.isBlank())
                    ? productRepository.searchByTitlePrice(search, offset, size)
                    : productRepository.findAllPrice(offset, size);

            default -> (search != null && !search.isBlank())
                    ? productRepository.searchByTitleAlpha(search, offset, size)
                    : productRepository.findAllPaged(offset, size);
        };

        return Mono.zip(productFlux.collectList(), totalMono)
                .map(tuple -> new ProductPage(tuple.getT1(), tuple.getT2()));
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
        return productRepository.findByIdWithCountCart(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")));
    }
}
