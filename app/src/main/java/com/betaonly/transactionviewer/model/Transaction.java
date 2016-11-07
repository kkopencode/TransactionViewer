package com.betaonly.transactionviewer.model;

import java.math.BigDecimal;

/**
 * Created by kelvinko on 6/11/2016.
 */

public class Transaction {
    private BigDecimal amount;
    private String currency;
    private String sku;
    private BigDecimal convertedAmount;
    private String convertedCurrency;

    public Transaction() {
        // no-arg constructor for gson
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getSku() {
        return sku;
    }

    public String getConvertedCurrency() {
        return convertedCurrency;
    }

    public void setConvertedCurrency(String currency) {
        convertedCurrency = currency;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal amount) {
        convertedAmount = amount;
    }
}
