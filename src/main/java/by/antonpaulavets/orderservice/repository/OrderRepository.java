package by.antonpaulavets.orderservice.repository;

import by.antonpaulavets.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusIn(List<String> statuses);
}