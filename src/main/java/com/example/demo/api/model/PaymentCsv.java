package com.example.demo.api.model;

import com.opencsv.bean.CsvBindByName;

public class PaymentCsv {

    @CsvBindByName(column = "amount")
    private String amount;

    @CsvBindByName(column = "debtorIban")
    private String debtorIban;

    public PaymentCsv(String amount, String debtorIban) {
        this.amount = amount;
        this.debtorIban = debtorIban;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDebtorIban() {
        return debtorIban;
    }

    public void setDebtorIban(String debtorIban) {
        this.debtorIban = debtorIban;
    }
}
