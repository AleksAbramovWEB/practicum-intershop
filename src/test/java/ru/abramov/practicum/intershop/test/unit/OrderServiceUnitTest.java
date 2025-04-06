package ru.abramov.practicum.intershop.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Order;
import ru.abramov.practicum.intershop.model.OrderItem;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.repository.OrderRepository;
import ru.abramov.practicum.intershop.service.OrderService;
import ru.abramov.practicum.intershop.service.impl.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = OrderServiceUnitTest.Config.class)
class OrderServiceUnitTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(new BigDecimal("100.00"));
        product.setImgPath("path/to/image");

        when(cartRepository.findAll()).thenReturn(List.of(
                createCartWithProduct(product),
                createCartWithProduct(product),
                createCartWithProduct(product)
        ));

        when(orderRepository.save(any(Order.class))).thenReturn(createOrder());
    }

    @Test
    void create_ShouldThrowException_WhenCartIsEmpty() {
        when(cartRepository.findAll()).thenReturn(List.of());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.create();
        });

        assertEquals("Cannot create order because no cart has been found", exception.getMessage());
    }

    @Test
    void create_ShouldCreateOrder_WhenCartHasItems() {
        Order order = orderService.create();

        assertNotNull(order);
        assertEquals(1, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(300.00), order.getTotalSum());

        verify(cartRepository, times(1)).findAll();
        verify(cartRepository, times(1)).deleteAll();
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrder_ShouldReturnOrder_WhenIdExists() {
        Order order = createOrder();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrder(1L);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    private Cart createCartWithProduct(Product product) {
        Cart cart = new Cart();
        cart.setProduct(product);
        return cart;
    }

    private Order createOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setTotalSum(BigDecimal.valueOf(300.00));
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setCount(3);
        orderItem.setTotalSum(BigDecimal.valueOf(300.00));
        order.setOrderItems(List.of(orderItem));
        return order;
    }

    @Configuration
    static class Config {

        @Bean
        public OrderService orderService(OrderRepository orderRepository, CartRepository cartRepository) {
            return new OrderServiceImpl(orderRepository, cartRepository);
        }

        @Bean
        public CartRepository cartRepository() {
            return mock(CartRepository.class);
        }

        @Bean
        public OrderRepository orderRepository() {
            return mock(OrderRepository.class);
        }
    }
}