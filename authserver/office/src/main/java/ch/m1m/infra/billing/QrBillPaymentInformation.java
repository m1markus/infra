package ch.m1m.infra.billing;

import java.math.BigDecimal;

public class QrBillPaymentInformation {

    private String currency = "CHF";

    private BigDecimal amount = new BigDecimal("0.00");

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
