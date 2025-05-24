package ru.abramov.practicum.intershop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.annotation.CurrentUserId;
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
            Model model,
            @CurrentUserId String userId
    ) {
        return productService.getProductsWithCount(search, sort, pageNumber, pageSize, userId)
                .map(page -> {
                    List<List<Product>> rows = new ArrayList<>();
                    int rowSize = 3;

                    for (int i = 0; i < page.getProducts().size(); i += rowSize) {
                        rows.add(page.getProducts().subList(i, Math.min(i + rowSize, page.getProducts().size())));
                    }

                    long totalElements = page.getTotalElements();

                    model.addAttribute("items", rows);
                    model.addAttribute("search", search);
                    model.addAttribute("sort", sort);
                    model.addAttribute("pageSize", pageSize);
                    model.addAttribute("pageNumber", pageNumber);
                    model.addAttribute("hasPrevious", pageNumber > 0);
                    model.addAttribute("hasNext", ((long) (pageNumber + 1) * pageSize) < totalElements);

                    return "main";
                });
    }

    @GetMapping("/product/{id}")
    public Mono<String> getProduct(@PathVariable Long id, Model model, @CurrentUserId String userId) {
        return productService.getProduct(id, userId)
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

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> addProducts(@ModelAttribute Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return Mono.just("form-product");
        }

        return productService.addProduct(product)
                .thenReturn("redirect:/");
    }
}
