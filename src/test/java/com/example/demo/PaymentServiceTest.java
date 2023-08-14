package com.example.demo;

import com.example.demo.api.model.Payment;
import com.example.demo.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentServiceTest {

    private PaymentService paymentService;

    @BeforeEach
    public void setup() {
        paymentService = new PaymentService();
    }

    @Test
    public void testGetPaymentById_exists() {
        Payment mockPayment = new Payment(UUID.randomUUID(), "LT356437978869712537", BigDecimal.TEN, null);
        paymentService.createPayment(mockPayment);

        Payment result = paymentService.getPaymentById(mockPayment.getPaymentId());
        assertNotNull(result);
        assertEquals(mockPayment, result);
    }

    @Test
    public void testGetPaymentById_notExists() {
        UUID randomUUID = UUID.randomUUID();
        Payment result = paymentService.getPaymentById(randomUUID);
        assertNull(result);
    }



    @Test
    public void testCreatePayment_valid() {
        Payment mockPayment = new Payment(null, "LT356437978869712537", BigDecimal.TEN, null);
        Payment result = paymentService.createPayment(mockPayment);
        assertNotNull(result);
        assertNotNull(result.getPaymentId());
    }

    @Test
    public void testCreatePayment_invalidAmount() {
        Payment mockPayment = new Payment(null, "LT356437978869712537", BigDecimal.ZERO, null);
        assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(mockPayment));
    }


}
