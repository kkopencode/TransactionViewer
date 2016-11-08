package com.betaonly.transactionviewer.currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kelvinko on 6/11/2016.
 *
 * Store all the currency pair and allow the user to get the rate of corresponding pair
 * Here, assumed the currency code is case in-sensitive. The rates are store in Map with key
 * like USDGBP, AUDGBP
 */
public class FxRates {

    private Map<String, BigDecimal> mPairMap;
    private List<CurrencyPair> mPairs;
    public FxRates(List<CurrencyPair> pairs) {
        mPairs = pairs;
        mPairMap = new HashMap<>();
        if (pairs != null) {
            for (CurrencyPair pair : pairs) {
                mPairMap.put(pair.getFrom().toUpperCase() + pair.getTo().toUpperCase(),
                        pair.getRate());
            }
        }

    }

    public List<CurrencyPair> getCurrencyPairs() {
        return mPairs;
    }

    public BigDecimal getRate(String from, String to) {
        return mPairMap.get( from.toUpperCase() + to.toUpperCase() );
    }
}
