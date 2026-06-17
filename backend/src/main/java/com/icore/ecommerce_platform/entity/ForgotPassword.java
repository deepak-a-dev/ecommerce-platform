package com.icore.ecommerce_platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "forgot_password")
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fp_id")
    private int fpId;

    @Column(name = "otp")
    private int otp;

    @Column(name = "creation")
    private LocalDateTime creation;

    @Column(name = "expiration")
    private LocalDateTime expiration;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
