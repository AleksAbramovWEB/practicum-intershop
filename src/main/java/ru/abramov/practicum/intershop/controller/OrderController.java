package ru.abramov.practicum.intershop.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.abramov.practicum.intershop.model.Order;
import ru.abramov.practicum.intershop.service.OrderService;

@Controller
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String orders(Model model) {

        model.addAttribute("orders", orderService.getOrderList());

        return "orders";
    }

    @GetMapping("/order/{id}")
    public String order(Model model, @PathVariable Long id, @RequestParam(required = false) Long created) {

        model.addAttribute("order", orderService.getOrder(id));

        if (created != null && created.equals(id)) {
            model.addAttribute("newOrder", created);
        }
        return "order";
    }

    @PostMapping("/order")
    public String create() {
        Order order = orderService.create();

        return "redirect:/order/" + order.getId() + "?created=" + order.getId();
    }
}
