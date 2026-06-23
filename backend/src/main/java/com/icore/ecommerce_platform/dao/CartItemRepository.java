package com.icore.ecommerce_platform.dao;

import com.icore.ecommerce_platform.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByUser_UserId(int userId);

    Optional<CartItem> findByUser_UserIdAndProduct_ProductId(int userId, int productId);

    void deleteByUser_UserId(int userId);
}