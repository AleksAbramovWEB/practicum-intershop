package ru.abramov.practicum.intershop.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public Mono<String> getProducts(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", defaultValue = "NO") String sort,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            Model model
    ) {
        return productService.getProducts(search, sort, pageNumber, pageSize)
                .collectList()
                .map(products -> {
                    List<List<Product>> rows = new ArrayList<>();
                    int size = 3;
                    for (int i = 0; i < products.size(); i += size) {
                        rows.add(products.subList(i, Math.min(i + size, products.size())));
                    }
                    model.addAttribute("items", rows);
                    model.addAttribute("search", search);
                    model.addAttribute("sort", sort);
                    model.addAttribute("pageSize", pageSize);
                    model.addAttribute("pageNumber", pageNumber);
                    return "main";
                });
    }

    @GetMapping("/product/{id}")
    public Mono<String> getProduct(@PathVariable Long id, Model model) {
        return productService.getProduct(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product";
                });
    }

    @GetMapping("/product/new")
    public Mono<String> addProductsForm(Model model) {
        model.addAttribute("product", new Product());
        return Mono.just("form-product");
    }

    @PostMapping("/product")
    public Mono<String> addProducts(@ModelAttribute Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return Mono.just("form-product");
        }

        return productService.addProduct(product)
                .thenReturn("redirect:/");
    }
}
