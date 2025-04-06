package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import ru.abramov.practicum.intershop.repository.ProductRepository;
import ru.abramov.practicum.intershop.model.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@Sql(scripts = "/sql/cart-controller-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CartControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void getCart_shouldReturnCartPageWithItemsAndTotal() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attributeExists("total"))
                .andExpect(view().name("cart"));
    }

    @Test
    void postPlusAction_shouldIncrementProductCount() throws Exception {
        mockMvc.perform(post("/product/100/cart")
                        .param("action", "plus")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        Product product = productRepository.findById(100L).orElseThrow();
        assertThat(product.getCount()).isEqualTo(3);
    }

    @Test
    void postMinusAction_shouldDecrementProductCount() throws Exception {
        mockMvc.perform(post("/product/100/cart")
                        .param("action", "minus")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        Product product = productRepository.findById(100L).orElseThrow();
        assertThat(product.getCount()).isEqualTo(1);
    }

    @Test
    void postDeleteAction_shouldClearProductFromCart() throws Exception {
        mockMvc.perform(post("/product/100/cart")
                        .param("action", "delete")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        Product product = productRepository.findById(100L).orElseThrow();
        assertThat(product.getCount()).isZero();
    }
}