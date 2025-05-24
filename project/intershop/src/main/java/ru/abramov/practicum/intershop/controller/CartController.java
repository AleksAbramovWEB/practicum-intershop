package ru.abramov.practicum.intershop.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.annotation.CurrentUserId;
import ru.abramov.practicum.intershop.client.pay.api.PayApi;
import ru.abramov.practicum.intershop.client.pay.domain.BalanceResponse;
import ru.abramov.practicum.intershop.service.CartService;

import java.math.BigDecimal;

@Slf4j
@Controller
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    private final PayApi payApi;

    @GetMapping("/cart")
    public Mono<String> cart(Model model, @CurrentUserId String userId) {
        return cartService.getProductsInCart(userId)
                .collectList()
                .flatMap(products -> {
                    BigDecimal total = products.stream()
                            .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getCount())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    model.addAttribute("items", products);
                    model.addAttribute("total", total);

                    return payApi.balanceGet(userId)
                            .mapNotNull(BalanceResponse::getBalance)
                            .onErrorResume(ex -> {
                                log.error(ex.getMessage(), ex);
                                return Mono.justOrEmpty((BigDecimal) null);
                            })
                            .map(balance -> {
                                model.addAttribute("balance", balance);
                                return "cart";
                            });
                });
    }

    @PostMapping(value = "/product/{id}/cart/plus")
    public Mono<String> plus(@PathVariable Long id, @CurrentUserId String userId, @RequestHeader(value = "Referer", required = false) String referer) {

        return cartService.plus(id, userId)
                .thenReturn(redirect(referer));
    }

    @PostMapping(value = "/product/{id}/cart/minus")
    public Mono<String> minus(@PathVariable Long id, @CurrentUserId String userId, @RequestHeader(value = "Referer", required = false) String referer) {
        return cartService.minus(id, userId)
                .thenReturn(redirect(referer));
    }

    @PostMapping(value = "/product/{id}/cart/delete")
    public Mono<String> delete(@PathVariable Long id, @CurrentUserId String userId, @RequestHeader(value = "Referer", required = false) String referer) {
        return cartService.delete(id, userId)
                .thenReturn(redirect(referer));
    }

    private String redirect(String referer) {
        String link = referer != null ? referer : "/";

        return "redirect:" + link;
    }
}
