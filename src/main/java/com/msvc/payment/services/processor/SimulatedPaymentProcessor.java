package com.msvc.payment.services.processor;

import com.msvc.payment.entities.Payment;
import com.msvc.payment.enums.PaymentMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class SimulatedPaymentProcessor implements PaymentProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedPaymentProcessor.class);

    @Override
    public String processor(Payment payment) {
        LOGGER.info(
                "Processing simulated payment for amount: {}",
                payment.getAmount()
        );

        // Regla 1: Si es efectivo, siempre es exitoso (el cajero ya recibió el dinero)
        if (payment.getMethod() == PaymentMethod.CASH) {
            return "CASH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

        // Regla 2: Simulamos un rechazo de tarjeta si el monto es mayor a $1000
        if (payment.getAmount().compareTo(new BigDecimal("1000.00")) > 0) {
            throw new RuntimeException("Tarjeta rechazada: Limite de crédito excedido.");
        }

        // Regla 3: Para tarjetas normales, aprobadas y generamos un ID falso de Stripe
        String mockStripeId = "ch_mock_" + UUID.randomUUID().toString().substring(0, 12);

        LOGGER.info(
                "Card approved. Mock Reference: {}",
                mockStripeId
        );

        return mockStripeId;
    }

}
