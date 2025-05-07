package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.abramov.practicum.intershop.model.Order;
import ru.abramov.practicum.intershop.model.OrderItem;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.OrderRepository;
import ru.abramov.practicum.intershop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

public class OrderRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveOrder_shouldSaveOrder() {
        Product product = new Product();
        product.setTitle("Пряник");
        product.setPrice(new BigDecimal("100.0"));
        product.setImgPath("image/path/to/pryanik.jpg");
        product.setDescription("Вкусный пряник");

        Mono<Product> savedProductMono = productRepository.save(product);

        savedProductMono
                .flatMap(savedProduct -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(savedProduct.getId());
                    orderItem.setCount(2);
                    orderItem.setTotalSum(savedProduct.getPrice().multiply(new BigDecimal(orderItem.getCount())));

                    Order order = new Order();
                    order.setTotalSum(orderItem.getTotalSum());
                    order.setOrderItems(List.of(orderItem));

                    orderItem.setOrderId(order.getId());

                    return orderRepository.save(order)
                            .flatMap(savedOrder -> orderRepository.findById(savedOrder.getId()));
                })
                .as(StepVerifier::create)
                .expectNextMatches(savedOrder -> savedOrder.getTotalSum().equals(new BigDecimal("200.0")))
                .verifyComplete();
    }

    @Test
    void findTopByOrderByIdDesc_shouldReturnLatestOrder() {
        Product product = new Product();
        product.setTitle("Пирог");
        product.setPrice(new BigDecimal("150.0"));
        product.setImgPath("image/path/to/pirog.jpg");
        product.setDescription("Вкусный пирог");

        // Сохраняем продукт
        Mono<Product> savedProductMono = productRepository.save(product);

        savedProductMono
                .flatMap(savedProduct -> {
                    Order order1 = new Order();
                    order1.setTotalSum(new BigDecimal("150.0"));
                    OrderItem orderItem1 = new OrderItem();
                    orderItem1.setProductId(savedProduct.getId());
                    orderItem1.setCount(1);
                    orderItem1.setTotalSum(new BigDecimal("150.0"));
                    order1.setOrderItems(List.of(orderItem1));
                    orderItem1.setOrderId(order1.getId());

                    Order order2 = createOrder(savedProduct, new BigDecimal("300.0"));

                    return orderRepository.save(order1)
                            .then(orderRepository.save(order2));
                })
                .flatMap(savedOrder -> orderRepository.findTopByOrderByIdDesc())
                .as(StepVerifier::create)
                .expectNextMatches(latestOrder -> latestOrder.getTotalSum().equals(new BigDecimal("300.0")))
                .verifyComplete();
    }

    private Order createOrder(Product product, BigDecimal totalSum) {
        Order order = new Order();
        order.setTotalSum(totalSum);
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(product.getId());
        orderItem.setCount(totalSum.intValue() / product.getPrice().intValue());
        orderItem.setTotalSum(totalSum);
        order.setOrderItems(List.of(orderItem));
        orderItem.setOrderId(order.getId());
        return order;
    }
}
