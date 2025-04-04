package ru.abramov.practicum.intershop.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public String getProducts(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", defaultValue = "NO") String sort,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            Model model
    ) {
        Page<Product> page = productService.getProducts(search, sort, pageNumber, pageSize);

        List<List<Product>> rows = new ArrayList<>();
        int size = 3;

        for (int i = 0; i < page.getContent().size(); i += size) {
            rows.add(page.getContent().subList(i, Math.min(i + size, page.getContent().size())));
        }

        model.addAttribute("items", rows);
        model.addAttribute("paging", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);

        return "main";
    }
}
