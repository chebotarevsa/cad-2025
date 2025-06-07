package ru.bsuedu.cad.lab.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>{
    
}
