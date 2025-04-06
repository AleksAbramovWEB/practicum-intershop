package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Order;
import ru.abramov.practicum.intershop.model.OrderItem;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.repository.OrderRepository;
import ru.abramov.practicum.intershop.service.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    @Override
    @Transactional
    public Order create() {

        Map<Product, List<Cart>> productCartMap = cartRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Cart::getProduct));

        if (productCartMap.isEmpty()) {
            throw new IllegalStateException("Cannot create order because no cart has been found");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        Order order = new Order();

        productCartMap.forEach((product, list) -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);

            orderItem.setCount(list.size());
            orderItem.setTotalSum(product.getPrice().multiply(BigDecimal.valueOf(list.size())));
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        });

        order.setOrderItems(orderItems);
        order.setTotalSum(orderItems.stream()
                .map(OrderItem::getTotalSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        cartRepository.deleteAll();

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}
