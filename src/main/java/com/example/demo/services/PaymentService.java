package com.example.demo.services;

import com.example.demo.api.model.Payment;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.iban4j.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final List<Payment> payments;

    public PaymentService() {
        this.payments = new ArrayList<>();
    }

    public Payment getPaymentById(UUID paymentId) {
        return this.payments.stream()
                .filter(payment -> payment.getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);
    }

    public List<Payment> getPayments(Optional<String> debtorIban) {
        return payments.stream()
                .filter(payment -> !debtorIban.isPresent() || payment.getDebtorIban().equals(debtorIban.get()))
                .collect(Collectors.toList());
    }

    public Payment createPayment(Payment payment) {
        if (payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        CountryCode countryCode;
        try {
            Iban iban = Iban.valueOf(payment.getDebtorIban());
            countryCode = iban.getCountryCode();
            if (!(countryCode == CountryCode.EE || countryCode == CountryCode.LV || countryCode == CountryCode.LT)) {
                throw new IllegalArgumentException("IBAN must be from a Baltic country (EE, LV, LT)");
            }
        } catch (IbanFormatException | InvalidCheckDigitException | UnsupportedCountryException e) {
            throw new IllegalArgumentException("Invalid IBAN", e);
        }

        payment.setCountryCode(String.valueOf(countryCode));
        UUID paymentId = UUID.randomUUID();
        payment.setPaymentId(paymentId);
        LocalDateTime timestamp = LocalDateTime.now();
        payment.setTimestamp(timestamp);
        this.payments.add(payment);
        return payment;
    }

    public void processPaymentsFromCSV(MultipartFile file, String countryCode) throws Exception {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            // Create CSV bean reader
            CsvToBean<Payment> csvToBean = new CsvToBeanBuilder<Payment>(reader)
                    .withType(Payment.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // Convert CSV to Payment List
            List<Payment> csvPayments = csvToBean.parse();

            for (Payment payment : csvPayments) {
                createPayment(payment); // Using the existing createPayment method to add each payment
            }
        }
    }
}
