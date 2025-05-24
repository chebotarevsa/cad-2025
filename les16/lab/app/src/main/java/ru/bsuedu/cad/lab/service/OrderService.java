package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import jakarta.transaction.Transactional;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class OrderService {
    final private OrderRepository orderRepository;
    final private CustomerRepository customerRepository;
    final private ProductRepository productRepository;
    

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getOrders(){
        List<Order> orders = new ArrayList<>();
        for (Order order : orderRepository.findAll()) {
            orders.add(order);
        }
        return orders;
    }

    public List<Order> getOrdersByGuava(){
       return Lists.newArrayList(orderRepository.findAll());
    }

    @Transactional
    public Order createOrder(int customerId, String address, List<Item> items){
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Date orderDate = new Date(System.currentTimeMillis());
        BigDecimal totalPrice = BigDecimal.ZERO;
        Order newOrder = new Order(orderDate, totalPrice, "New order", address, customer);
        List<OrderDetail> details = new ArrayList<>();
        for (Item item : items) {
            Product product = productRepository.findById(item.productId).orElseThrow();
            totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(item.quantity)));
            OrderDetail orderItem = new OrderDetail(item.quantity, totalPrice, newOrder, product);
            details.add(orderItem);
        }
        newOrder.setTotalPrice(totalPrice);
        newOrder.setOrderDetails(details);
        return orderRepository.save(newOrder);
    }

    public Order getById(int id){
        return orderRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteById(int id){
        orderRepository.deleteById(id);
    }

    @Transactional
    public void updateOrder(int orderId ,Date date, String status, Integer customerId, String address){
        var order = getById(orderId);
        if (address != null) order.setShippingAddress(address);
        if (date != null) order.setOrderDate(date);
        if (status != null) order.setStatus(status);
        if (customerId != null){ 
            Customer customer = customerRepository.findById(customerId).orElseThrow();
            order.setCustomer(customer);
        }
        orderRepository.save(order);
    }
 
    public static class Item{
        private int productId;
        private int quantity;
        
        public Item(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public int getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
