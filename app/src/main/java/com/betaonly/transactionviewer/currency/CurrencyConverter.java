package com.betaonly.transactionviewer.currency;

import com.betaonly.transactionviewer.AppConst;
import com.betaonly.transactionviewer.CurrencyGraph;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        for(CurrencyPair pair : pairs) {
            mCurrencyGraph.addEdge(pair.getFrom(), pair.getTo(), pair.getRate());
        }
    }

    public BigDecimal convert(BigDecimal amount, String from, String to) throws RateNotFoundException {

        // try to get the rate from supplied currency pairs
        BigDecimal rate = mFxRates.getRate( from, to );

        // If not available directly, try to induce the rate from pairs
        if (rate == null) {
            rate = calculateRate( from, to );
        }
        // If cannot induce it from supplied information, throw exception
        if (rate == null) throw new RateNotFoundException();

        BigDecimal converted = amount.multiply( rate );

        return converted.setScale( AppConst.CURRENCY_DECIMAL_PLACE, RoundingMode.HALF_UP );
    }

    private BigDecimal calculateRate( String from, String to ) {
        BigDecimal rate = mCurrencyGraph.calculateRate(from, to);

        return rate;
    }
}
