package hse.kpo.hw.kpohw3shop.repository;

import hse.kpo.hw.kpohw3shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
