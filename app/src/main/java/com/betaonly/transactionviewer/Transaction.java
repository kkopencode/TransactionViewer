package com.betaonly.transactionviewer;

import java.math.BigDecimal;

/**
 * Created by kelvinko on 6/11/2016.
 */

public class Transaction {
    private BigDecimal amount;
    private String currency;
    private String sku;

    public Transaction() {

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
}
