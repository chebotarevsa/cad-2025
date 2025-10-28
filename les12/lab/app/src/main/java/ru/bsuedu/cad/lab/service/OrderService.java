package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.bsuedu.cad.lab.dto.UpdateOrderDto;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// получение списка заказов кк
// создание заказа кк
// удаление заказа кк
// изменение заказа

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;

  public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
      ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.customerRepository = customerRepository;
    this.productRepository = productRepository;
  }

  // Получение всех заказов
  @Transactional
  public List<Order> getAllOrders() {
    return orderRepository.findAllWithDetails();
  }

  // Создание заказа
  @Transactional
  public Order createOrder(Integer customerId, Integer productId, Integer quantity) {
    Customer customer = (customerRepository.findById(customerId)).get();
    Product product = (productRepository.findById(productId)).get();

    Order order = new Order();
    order.setCustomer(customer);
    order.setOrderDate(new Date());
    order.setStatus("Created");
    order.setShippingAddress(customer.getAddress());
    order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

    // Формируем OrderDetail
    OrderDetail detail = new OrderDetail();
    detail.setProduct(product);
    detail.setQuantity(quantity);
    detail.setPrice(product.getPrice());
    detail.setOrder(order); // связь обратная

    List<OrderDetail> orderDetails = new ArrayList<>();
    orderDetails.add(detail);

    order.setOrderDetails(orderDetails);

    Order savedOrder = orderRepository.save(order);

    return savedOrder;
  }

  // Удаление заказа по ID
  @Transactional
  public void deleteOrderById(Integer Id) {
    if (!orderRepository.existsById(Id)) {
      throw new RuntimeException("Заказ с ID " + Id + " не найден");
    }
    orderRepository.deleteById(Id);
  }

  @Transactional
  public Order updateOrder(Integer id, UpdateOrderDto dto) {
    Order order = orderRepository.findById(id).get();

    if (dto.getStatus() != null) {
      order.setStatus(dto.getStatus());
    }
    if (dto.getShippingAddress() != null) {
      order.setShippingAddress(dto.getShippingAddress());
    }

    Order updated = orderRepository.save(order);

    return updated;
  }
}
