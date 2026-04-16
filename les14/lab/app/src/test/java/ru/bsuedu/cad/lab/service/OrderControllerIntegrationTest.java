package ru.bsuedu.cad.lab.service;

import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;


    @Test
    void shouldReturnOrdersPage() throws Exception {

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));
    }


    @Test
    void shouldReturnCreateForm() throws Exception {

        mockMvc.perform(get("/orders/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-order"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attributeExists("products"));
    }


    @Test
    void shouldCreateOrderAndPersistInDatabase() throws Exception {


        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setName("Test Customer");
        customer = customerRepository.save(customer);

        Product product = new Product();
        product.setProductId(1);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
        product.setStockQuantity(10);
        product = productRepository.save(product);

        mockMvc.perform(post("/orders/create")
                        .param("customerId", customer.getCustomerId().toString())
                        .param("productId", product.getProductId().toString())
                        .param("quantity", "2")
                        .param("shippingAddress", "Test Address"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));
    }
}
