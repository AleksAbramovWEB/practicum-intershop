package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.client.pay.api.PayApi;
import ru.abramov.practicum.intershop.client.pay.domain.PaymentRequest;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Order;
import ru.abramov.practicum.intershop.model.OrderItem;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.repository.OrderItemRepository;
import ru.abramov.practicum.intershop.repository.OrderRepository;
import ru.abramov.practicum.intershop.service.OrderService;
import ru.abramov.practicum.intershop.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    private final ProductService productService;

    private final OrderItemRepository orderItemRepository;

    private final PayApi payApi;

    @Override
    public Mono<Order> create(String userId) {
        return cartRepository.findAllByUserId(userId)
                .collectList()
                .flatMap(carts -> {
                    if (carts.isEmpty()) {
                        return Mono.error(new IllegalStateException("Cannot create order because no cart has been found"));
                    }

                    Map<Long, Long> productCounts = carts.stream()
                            .collect(Collectors.groupingBy(Cart::getProductId, Collectors.counting()));

                    Order order = new Order();
                    order.setTotalSum(BigDecimal.ZERO);
                    order.setUserId(userId);

                    return orderRepository.save(order)
                            .flatMap(savedOrder -> {
                                List<Mono<OrderItem>> itemMonos = productCounts.entrySet().stream()
                                        .map(entry -> {
                                            Long productId = entry.getKey();
                                            Long count = entry.getValue();

                                            return productService.getProduct(productId, userId)
                                                    .map(product -> {
                                                        OrderItem item = new OrderItem();
                                                        item.setOrderId(savedOrder.getId());
                                                        item.setProductId(productId);
                                                        item.setCount(count.intValue());
                                                        item.setTotalSum(product.getPrice().multiply(BigDecimal.valueOf(count)));
                                                        return item;
                                                    });
                                        })
                                        .toList();

                                return Flux.concat(itemMonos)
                                        .collectList()
                                        .flatMap(orderItems -> {
                                            BigDecimal totalSum = orderItems.stream()
                                                    .map(OrderItem::getTotalSum)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                                            savedOrder.setTotalSum(totalSum);

                                            return orderItemRepository.saveAll(orderItems)
                                                    .then(orderRepository.save(savedOrder));
                                        });
                            })
                            .flatMap(savedOrder -> {

                                PaymentRequest paymentRequest = new PaymentRequest();

                                paymentRequest.setAmount(savedOrder.getTotalSum());
                                paymentRequest.setUserId(userId);

                                return payApi.payPost(paymentRequest)
                                        .thenReturn(savedOrder);
                            })
                            .flatMap(savedOrder -> cartRepository.deleteAll().thenReturn(savedOrder));
                });
    }


    @Override
    public Flux<Order> getOrderList(String userId) {
        return orderRepository.findAllByUserId(userId)
                .flatMap(order -> setOrderItemsWithProducts(order, userId));
    }

    @Override
    public Mono<Order> getOrder(Long id, String userId) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found")))
                .flatMap(order -> setOrderItemsWithProducts(order, userId));
    }

    private Mono<Order> setOrderItemsWithProducts(Order order, String userId) {
        return orderItemRepository.findByOrderId(order.getId())
                .collectList()
                .flatMap(orderItems -> Flux.fromIterable(orderItems)
                        .flatMap(orderItem ->
                                productService.getProduct(orderItem.getProductId(), userId)
                                        .map(product -> {
                                            orderItem.setProduct(product);
                                            return orderItem;
                                        })
                        )
                        .collectList()
                        .map(updatedOrderItems -> {
                            order.setOrderItems(updatedOrderItems);
                            return order;
                        })
                );
    }
}
