package com.betaonly.transactionviewer;

import com.betaonly.transactionviewer.currency.CurrencyPair;
import com.betaonly.transactionviewer.currency.FxRates;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by kelvinko on 6/11/2016.
 */

public class FxRatesTest {
    @Test
    public void constructor_SetRatesFromCurrencyPair_ReturnRate() {
        // create USD to GBP pair rate as 0.77
        BigDecimal usdGbpRate = new BigDecimal("0.77");
        ArrayList<CurrencyPair> pairs =new ArrayList<>();
        pairs.add(new CurrencyPair("USD", "GBP", usdGbpRate));

        // set rates through constructor
        FxRates fxRates = new FxRates(pairs);
        BigDecimal rate = fxRates.getRate("USD", "GBP");

        // expect return 0.77
        assertThat( rate, is( equalTo( usdGbpRate ) ));
    }
}
