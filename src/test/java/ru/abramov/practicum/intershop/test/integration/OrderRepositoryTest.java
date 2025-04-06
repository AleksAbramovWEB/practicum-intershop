package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.abramov.practicum.intershop.model.Order;
import ru.abramov.practicum.intershop.model.OrderItem;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.OrderRepository;
import ru.abramov.practicum.intershop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
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
        productRepository.save(product);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setCount(2);
        orderItem.setTotalSum(product.getPrice().multiply(new BigDecimal(orderItem.getCount())));

        Order order = new Order();
        order.setTotalSum(orderItem.getTotalSum());
        order.setOrderItems(List.of(orderItem));

        orderItem.setOrder(order);

        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getTotalSum()).isEqualTo(order.getTotalSum());
        assertThat(savedOrder.getOrderItems()).hasSize(1);
        assertThat(savedOrder.getOrderItems().getFirst().getProduct().getTitle()).isEqualTo("Пряник");
    }

    @Test
    void findTopByOrderByIdDesc_shouldReturnLatestOrder() {
        Product product = new Product();
        product.setTitle("Пирог");
        product.setPrice(new BigDecimal("150.0"));
        product.setImgPath("image/path/to/pirog.jpg");
        product.setDescription("Вкусный пирог");
        productRepository.save(product);

        Order order1 = new Order();
        order1.setTotalSum(new BigDecimal("150.0"));
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setProduct(product);
        orderItem1.setCount(1);
        orderItem1.setTotalSum(new BigDecimal("150.0"));
        order1.setOrderItems(List.of(orderItem1));
        orderItem1.setOrder(order1);

        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setTotalSum(new BigDecimal("300.0"));
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setProduct(product);
        orderItem2.setCount(2);
        orderItem2.setTotalSum(new BigDecimal("300.0"));
        order2.setOrderItems(List.of(orderItem2));
        orderItem2.setOrder(order2);

        orderRepository.save(order2);

        Order latestOrder = orderRepository.findTopByOrderByIdDesc();

        assertThat(latestOrder).isNotNull();
        assertThat(latestOrder.getId()).isEqualTo(order2.getId());
        assertThat(latestOrder.getTotalSum()).isEqualTo(order2.getTotalSum());
    }
}
