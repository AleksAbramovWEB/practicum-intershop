package ru.abramov.practicum.intershop.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import ru.abramov.practicum.intershop.repository.ProductRepository;
import ru.abramov.practicum.intershop.model.Product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@Sql(scripts = "/sql/product-controller-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void getProducts_shouldReturnMainPageWithProducts() throws Exception {
        mockMvc.perform(get("/")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sort", "NO"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("pageSize"))
                .andExpect(model().attributeExists("pageNumber"));
    }

    @Test
    void getProduct_shouldReturnProductPage() throws Exception {

        mockMvc.perform(get("/product/200"))
                .andExpect(status().isOk())
                .andExpect(view().name("product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void addProductForm_shouldReturnProductFormPage() throws Exception {
        mockMvc.perform(get("/product/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("form-product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void addProduct_shouldRedirectToMainPage() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "pryanik.jpg",
                "image/jpeg",
                "image content".getBytes()
        );

        mockMvc.perform(multipart("/product")
                        .file(file)
                        .param("title", "Пряник 2")
                        .param("description", "Описание пряника")
                        .param("price", "150.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Product product = productRepository.findByTitle("Пряник 2");
        assertThat(product).isNotNull();
    }
}
