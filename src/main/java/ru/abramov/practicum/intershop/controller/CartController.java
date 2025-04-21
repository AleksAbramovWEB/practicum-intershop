package ru.abramov.practicum.intershop.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.service.CartService;

import java.math.BigDecimal;

@Controller
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    public Mono<String> cart(Model model) {
        return cartService.getProductsInCart()
                .collectList()
                .map(products -> {
                    BigDecimal total = products.stream()
                            .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getCount())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    model.addAttribute("items", products);
                    model.addAttribute("total", total);
                    return "cart";
                });
    }

    @PostMapping(value = "/product/{id}/cart/plus")
    public Mono<String> plus(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer) {
        return cartService.plus(id)
                .thenReturn(redirect(referer));
    }

    @PostMapping(value = "/product/{id}/cart/minus")
    public Mono<String> minus(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer) {
        return cartService.minus(id)
                .thenReturn(redirect(referer));
    }

    @PostMapping(value = "/product/{id}/cart/delete")
    public Mono<String> delete(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer) {
        return cartService.delete(id)
                .thenReturn(redirect(referer));
    }

    private String redirect(String referer) {
        String link = referer != null ? referer : "/";

        return "redirect:" + link;
    }
}
