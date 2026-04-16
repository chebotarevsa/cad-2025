package ru.bsuedu.cad.lab.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderDetailRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Client {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderService orderService;

    public void createSampleOrder() {
        Product product = productRepository.findById(1L).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        OrderDetail orderDetail = new OrderDetail(
                1, product, 2, product.getPrice()
        );

        orderDetailRepository.save(orderDetail);
        orderService.createOrder(new Order(
           1, customerRepository.findById(1L).orElse(null), LocalDateTime.now(),
                product.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())),
                "Pending", "Address: sampleHouse 2", List.of(orderDetail)
        ));
    }

}
