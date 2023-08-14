package com.example.demo.api.controller;

import com.example.demo.api.model.Payment;
import com.example.demo.services.IpResolverService;
import com.example.demo.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    private IpResolverService ipResolverService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getPayments(Optional.empty());
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment,
                                                 @RequestHeader(name = "X-Forwarded-For", required = false) String ip) {
        if (ip != null && !ip.isEmpty()) {
            Optional<String> country = ipResolverService.resolveCountry(ip);
            country.ifPresent(payment::setCountryCode);
        }
        Payment createdPayment = paymentService.createPayment(payment);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @GetMapping("/{paymentId}")
    public Payment getPayment(@PathVariable UUID paymentId) {
        return paymentService.getPaymentById(paymentId);
    }

    @PostMapping("/payment-files")
    public ResponseEntity<String> uploadPaymentsCSV(@RequestParam("file") MultipartFile file,
                                                    @RequestHeader(name = "X-Forwarded-For", required = false) String ip) {
        try {
            String countryCode = null;
            if (ip != null && !ip.isEmpty()) {
                Optional<String> country = ipResolverService.resolveCountry(ip);
                if (country.isPresent()) {
                    countryCode = country.get();
                }
            }
            paymentService.processPaymentsFromCSV(file, countryCode);
            return ResponseEntity.ok("CSV processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process CSV: " + e.getMessage());
        }
    }
}

