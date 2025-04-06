package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import ru.abramov.practicum.intershop.model.Order;
import ru.abramov.practicum.intershop.repository.OrderRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@Sql(scripts = "/sql/order-controller-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderControllerTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void getOrders_shouldReturnOrdersPageWithOrderList() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("orders"));
    }

    @Test
    void getOrder_shouldReturnOrderPageById() throws Exception {
        mockMvc.perform(get("/order/300"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(view().name("order"));
    }

    @Test
    void getOrder_withCreatedParam_shouldIncludeNewOrderAttribute() throws Exception {
        mockMvc.perform(get("/order/300")
                        .param("created", "300"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("newOrder"))
                .andExpect(view().name("order"));
    }

    @Test
    void postOrder_shouldCreateNewOrderAndRedirectToIt() throws Exception {
        mockMvc.perform(post("/order"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/order/*?created=*"));

        Order latest = orderRepository.findTopByOrderByIdDesc();
        assertThat(latest).isNotNull();
    }
}
