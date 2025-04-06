package ru.abramov.practicum.intershop.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.abramov.practicum.intershop.service.CartService;

@Controller
@RequestMapping("/product/{id}/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(params = "action=plus")
    public String plus(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer) {

        cartService.plus(id);

        return "redirect:" + sanitize(referer);
    }

    @PostMapping(params = "action=minus")
    public String minus(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer) {

        cartService.minus(id);

        return "redirect:" + sanitize(referer);
    }

    private String sanitize(String referer) {
        return referer != null ? referer : "/";
    }
}
