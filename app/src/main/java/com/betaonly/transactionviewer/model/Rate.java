package com.betaonly.transactionviewer.model;

import java.math.BigDecimal;

/**
 * Created by kelvinko on 7/11/2016.
 */

public class Rate {
    private String from, to;
    private BigDecimal rate;

    public Rate() {
        // no-args constructor for gson
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
    public BigDecimal getRate() {
        return rate;
    }
}
