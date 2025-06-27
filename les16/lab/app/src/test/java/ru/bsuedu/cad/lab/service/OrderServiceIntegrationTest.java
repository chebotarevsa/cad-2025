package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.config.TestConfigDB;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfigDB.class)
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class OrderServiceIntegrationTest {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup(){
        Customer customer = new Customer();
        customer.setName("John");
        customerRepository.save(customer);
    }
    
    @Test
    void createOrder_ShouldCreateNewOrder(){
        orderService.createOrder(1, "Belgorod", new ArrayList<>());
        var result = orderRepository.findAll();

        int count = 0;
        Order newOrder = null;
        
        for (Order order : result) {
            count++;
            newOrder = order;
        }

        assertEquals(1, count);
        assertEquals("John", newOrder.getCustomer().getName());
        assertEquals("Belgorod", newOrder.getShippingAddress());
    }

    @Test
    void createOrder_CustomerShouldNotFind(){
        try{
            orderService.createOrder(2, "Belgorod", new ArrayList<>());
            fail("Error, exception not caused");
        }
        catch (Exception exception){
            assertEquals("java.util.NoSuchElementException", exception.getClass().getName());
        }
    }
}
