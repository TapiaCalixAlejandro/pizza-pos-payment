package com.msvc.payment.services.processor;

import com.msvc.payment.entities.Payment;

public interface PaymentProcessor {

    /**
     * Procesa el pago
     * @param payment El pago a procesar
     * @return El ID de transacción del proveedor (o uno simulado).
     * @throws RuntimeException si el pago es rechazado.
     */
    String processor(Payment payment);

}
