package com.icore.ecommerce_platform.dao;

import com.icore.ecommerce_platform.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.user.username = :username ORDER BY o.dateOfOrder DESC")
    List<Order> findOrdersByUsername(String username);
}