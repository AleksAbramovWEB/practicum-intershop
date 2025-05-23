package ru.abramov.practicum.intershop.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.annotation.CurrentUserId;
import ru.abramov.practicum.intershop.service.OrderService;

@Controller
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public Mono<String> orders(Model model, @CurrentUserId String userId) {
        return orderService.getOrderList(userId)
                .collectList()
                .map(orders -> {
                    model.addAttribute("orders", orders);
                    return "orders";
                });
    }

    @GetMapping("/order/{id}")
    public Mono<String> order(Model model, @PathVariable Long id, @RequestParam(required = false) Long created, @CurrentUserId String userId) {
        return orderService.getOrder(id, userId)
                .map(order -> {
                    model.addAttribute("order", order);
                    if (created != null && created.equals(id)) {
                        model.addAttribute("newOrder", created);
                    }
                    return "order";
                });
    }

    @PostMapping("/order")
    public Mono<String> create(@CurrentUserId String userId) {
        return orderService.create(userId)
                .map(order -> "redirect:/order/" + order.getId() + "?created=" + order.getId());
    }
}
