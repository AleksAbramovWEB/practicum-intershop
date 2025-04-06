package ru.abramov.practicum.intershop.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.service.CartService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String cart(Model model) {

        List<Product> productsInCart = cartService.getProductsInCart();
        BigDecimal total = productsInCart.stream()
                        .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getCount())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("items", productsInCart);
        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping(value = "/product/{id}/cart", params = "action=plus")
    public String plus(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer) {

        cartService.plus(id);

        return redirect(referer);
    }

    @PostMapping(value = "/product/{id}/cart", params = "action=minus")
    public String minus(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer) {

        cartService.minus(id);

        return redirect(referer);
    }

    @PostMapping(value = "/product/{id}/cart",params = "action=delete")
    public String delete(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer) {

        cartService.delete(id);

        return redirect(referer);
    }

    private String redirect(String referer) {
        String link = referer != null ? referer : "/";

        return "redirect:" + link;
    }
}
