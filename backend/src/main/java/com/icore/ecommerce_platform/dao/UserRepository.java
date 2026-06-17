package com.icore.ecommerce_platform.dao;

import com.icore.ecommerce_platform.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User usernameVerification(String username);

    @Query("SELECT u FROM User u")
    List<User> getAllUsers();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.userId = :userId")
    void updatePassword(String password, int userId);
}
