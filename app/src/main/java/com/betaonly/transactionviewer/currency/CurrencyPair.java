package com.betaonly.transactionviewer.currency;

import java.math.BigDecimal;

/**
 * Created by kelvinko on 6/11/2016.
 */
public class CurrencyPair {
    String mFrom;
    String mTo;
    BigDecimal mRate;
    public CurrencyPair(String from, String to, BigDecimal rate) {
        mFrom = from;
        mTo = to;
        mRate = rate;
    }

    public String getFrom() {
        return mFrom;
    }
    public String getTo() {
        return mTo;
    }
    public BigDecimal getRate() {
        return mRate;
    }
}
