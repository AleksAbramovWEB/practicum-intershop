package ru.abramov.practicum.intershop.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.abramov.practicum.intershop.client.pay.api.PayApi;
import ru.abramov.practicum.intershop.model.Cart;
import ru.abramov.practicum.intershop.model.Order;
import ru.abramov.practicum.intershop.model.OrderItem;
import ru.abramov.practicum.intershop.model.Product;
import ru.abramov.practicum.intershop.repository.CartRepository;
import ru.abramov.practicum.intershop.repository.OrderItemRepository;
import ru.abramov.practicum.intershop.repository.OrderRepository;
import ru.abramov.practicum.intershop.service.OrderService;
import ru.abramov.practicum.intershop.service.ProductService;
import ru.abramov.practicum.intershop.service.impl.OrderServiceImpl;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = OrderServiceUnitTest.Config.class)
class OrderServiceUnitTest {

    private final String userId = "user-42";

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(new BigDecimal("100.00"));

        Cart cart = new Cart();
        cart.setProductId(1L);

        when(cartRepository.findAllByUserId(userId)).thenReturn(Flux.just(cart, cart, cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(10L);
            return Mono.just(o);
        });
        when(productService.getProduct(1L, userId)).thenReturn(Mono.just(product));
        when(cartRepository.deleteAll()).thenReturn(Mono.empty());
    }

    @Test
    void create_ShouldThrow_WhenCartEmpty() {
        when(cartRepository.findAllByUserId(userId)).thenReturn(Flux.empty());

        StepVerifier.create(orderService.create(userId))
                .expectErrorMatches(err -> err instanceof IllegalStateException &&
                        err.getMessage().equals("Cannot create order because no cart has been found"))
                .verify();
    }

    @Test
    void getOrder_ShouldReturnOrderWithItems() {
        Order order = new Order();
        order.setId(42L);
        order.setTotalSum(BigDecimal.ZERO);

        OrderItem item = new OrderItem();
        item.setOrderId(42L);
        item.setProductId(1L);
        item.setCount(1);
        item.setTotalSum(BigDecimal.valueOf(100));

        when(orderRepository.findById(42L)).thenReturn(Mono.just(order));
        when(orderItemRepository.findByOrderId(42L)).thenReturn(Flux.just(item));
        when(productService.getProduct(1L, userId)).thenReturn(Mono.just(product));

        StepVerifier.create(orderService.getOrder(42L, userId))
                .expectNextMatches(o -> o.getOrderItems().size() == 1 &&
                        o.getOrderItems().get(0).getProduct().getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void getOrderList_ShouldReturnOrdersWithItems() {
        Order order = new Order();
        order.setId(11L);
        order.setTotalSum(BigDecimal.ZERO);

        OrderItem item = new OrderItem();
        item.setOrderId(11L);
        item.setProductId(1L);
        item.setCount(1);
        item.setTotalSum(BigDecimal.valueOf(100));

        when(orderRepository.findAllByUserId(userId)).thenReturn(Flux.just(order));
        when(orderItemRepository.findByOrderId(11L)).thenReturn(Flux.just(item));
        when(productService.getProduct(1L, userId)).thenReturn(Mono.just(product));

        StepVerifier.create(orderService.getOrderList(userId))
                .expectNextMatches(o -> o.getOrderItems().size() == 1 &&
                        o.getOrderItems().get(0).getProduct().getId().equals(1L))
                .verifyComplete();
    }

    @Configuration
    static class Config {
        @Bean
        public OrderService orderService(OrderRepository orderRepository, CartRepository cartRepository,
                                         ProductService productService, OrderItemRepository orderItemRepository, PayApi payApi) {
            return new OrderServiceImpl(orderRepository, cartRepository, productService, orderItemRepository, payApi);
        }

        @Bean
        public OrderRepository orderRepository() {
            return mock(OrderRepository.class);
        }

        @Bean
        public CartRepository cartRepository() {
            return mock(CartRepository.class);
        }

        @Bean
        public ProductService productService() {
            return mock(ProductService.class);
        }

        @Bean
        public OrderItemRepository orderItemRepository() {
            return mock(OrderItemRepository.class);
        }

        @Bean
        public PayApi payApi() {
            return mock(PayApi.class);
        }
    }
}
