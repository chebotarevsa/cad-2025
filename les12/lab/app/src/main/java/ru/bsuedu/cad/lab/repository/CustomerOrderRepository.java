package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.lab.entity.CustomerOrder;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {
}