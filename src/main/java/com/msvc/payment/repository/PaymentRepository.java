package com.msvc.payment.repository;

import com.msvc.payment.entities.Payment;
import com.msvc.payment.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    List<Payment> findByOrderId(Long orderId);

    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);

}
