package com.icore.ecommerce_platform.dao;

import com.icore.ecommerce_platform.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("SELECT t FROM Token t INNER JOIN User u ON t.user.id = u.id WHERE t.user.id = :userId AND t.loggedOut = false")
    List<Token> findAllTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
