package com.betaonly.transactionviewer.currency;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by kelvinko on 6/11/2016.
 */
public class CurrencyConverter {

    private FxRates mFxRates;
    private CurrencyGraph mCurrencyGraph;
    public CurrencyConverter(FxRates fxRates) {
        mFxRates = fxRates;
        mCurrencyGraph = new CurrencyGraph();
        List<CurrencyPair> pairs = fxRates.getCurrencyPairs();
        if (pairs != null) {
            for (CurrencyPair pair : pairs) {
                mCurrencyGraph.addEdge(pair.getFrom(), pair.getTo(), pair.getRate());
            }
        }
    }

    public BigDecimal convert(BigDecimal amount, String from, String to) throws RateNotFoundException {

        // If from and to are the same, just return the original amount
        if (from.equalsIgnoreCase(to)) {
            return amount;
        }

        // try to get the rate from supplied currency pairs
        BigDecimal rate = mFxRates.getRate( from, to );

        // If not available directly, try to calculate the rate from pairs
        if (rate == null) {
            rate = calculateRate( from, to );
        }

        // If cannot induce it from supplied information, throw exception
        if (rate == null) throw new RateNotFoundException();

        BigDecimal converted = amount.multiply( rate );

        return converted;
    }

    private BigDecimal calculateRate( String from, String to ) {
        BigDecimal rate = mCurrencyGraph.calculateRate(from, to);

        return rate;
    }
}
