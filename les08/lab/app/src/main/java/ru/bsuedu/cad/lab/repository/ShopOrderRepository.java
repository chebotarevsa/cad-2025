package ru.bsuedu.cad.lab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab.entity.ShopOrder;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Integer> {

    default ShopOrder create(ShopOrder order) {
        return save(order);
    }

    @Override
    Optional<ShopOrder> findById(Integer id);

    @Override
    List<ShopOrder> findAll();
}
