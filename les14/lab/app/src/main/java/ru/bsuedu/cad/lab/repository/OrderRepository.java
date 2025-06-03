package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.lab.entity.CustomerOrder;

public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {
}