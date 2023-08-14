package com.example.demo.api.model;


import com.opencsv.bean.CsvBindByName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Payment {
    private UUID paymentId;

    @CsvBindByName(column = "debtorIban")
    private String debtorIban;

    @CsvBindByName(column = "amount")
    private BigDecimal amount;

    private LocalDateTime timestamp;
    private String countryCode;


    public Payment() {}


    public Payment(UUID paymentId, String debtorIban, BigDecimal amount, LocalDateTime timestamp) {
        this.paymentId = paymentId;
        this.debtorIban = debtorIban;
        this.amount = amount;
        this.timestamp = timestamp;
    }
    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getDebtorIban() {
        return debtorIban;
    }

    public void setDebtorIban(String debtorIban) {
        this.debtorIban = debtorIban;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", debtorIban='" + debtorIban + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
        return Objects.equals(getPaymentId(), payment.getPaymentId()) && Objects.equals(getDebtorIban(), payment.getDebtorIban()) && Objects.equals(getAmount(), payment.getAmount()) && Objects.equals(getTimestamp(), payment.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPaymentId(), getDebtorIban(), getAmount(), getTimestamp());
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
