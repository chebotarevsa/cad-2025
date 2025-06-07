package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.transaction.Transactional;
import ru.bsuedu.cad.lab.dto.CreateOrderItemDto;
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


    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getOrders(){
        List<Order> orderList = new ArrayList<>();
        for (Order i : orderRepository.findAll()) {
            orderList.add(i);
        }
        return orderList;
    }

    @Transactional
    public Order createOrder(Long customerId, List<Item> items, String address){
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderDetail> listOrderDetail = new ArrayList<>();
        Order order = new Order(new Date(System.currentTimeMillis()), totalPrice, "New", address, listOrderDetail, customer);

        for(Item item : items)
        {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            BigDecimal price = product.getPrice();
            totalPrice = totalPrice.add(price.multiply(new BigDecimal(item.quantity)));
            OrderDetail orderDetail = new OrderDetail(item.quantity, price, order, product);
            listOrderDetail.add(orderDetail);
        }
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        return order;
    }
    
    public Order getOrderById(Long id)
    {
        return orderRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteOrder(Long id)
    {
        orderRepository.deleteById(id);
    }
    
    @Transactional
    public void editItem(Long id, Long itemId, Item item)
    {
        Order order = getOrderById(id);
        for(OrderDetail d : order.getItems())
        {
            if(d.getOrderDetailId() == itemId)
            {
                d.setQuantity(item.quantity);
                d.setProduct(productRepository.findById(item.getProductId()).orElseThrow());
            }
        }
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrder(Long id, Long customerId, String address)
    {
        Order order = getOrderById(id);
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        order.setShippingAddress(address);
        order.setCustomer(customer);
        orderRepository.save(order);
    }

    public static class Item
    {
        private Long productId;
        private int quantity;


        public Long getProductId() {
            return productId;
        }
        public void setProductId(Long productId) {
            this.productId = productId;
        }
        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        public Item(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
        
        
    }
}
