package com.betaonly.transactionviewer;

import com.betaonly.transactionviewer.currency.CurrencyConverter;
import com.betaonly.transactionviewer.currency.CurrencyPair;
import com.betaonly.transactionviewer.currency.FxRates;
import com.betaonly.transactionviewer.currency.RateNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kelvinko on 6/11/2016.
 */
@RunWith(JUnitParamsRunner.class)
public class CurrencyConverterTest {
    @Test
    public void convert_RateFromTableDirectly_Calculated() throws RateNotFoundException {
        FxRates fxRates = mock(FxRates.class);
        CurrencyConverter converter = new CurrencyConverter(fxRates);

        when(fxRates.getRate("USD","GBP")).thenReturn(new BigDecimal("0.77"));
        BigDecimal amountInUsd = new BigDecimal("1");
        BigDecimal amountInGbp = converter.convert(amountInUsd, "USD", "GBP");

        assertThat( amountInGbp, is( equalTo( new BigDecimal("0.77") )));
    }

    @Test
    public void convert_FromToSameCurrency_ReturnInputAmount() throws RateNotFoundException {
        FxRates fxRates = mock(FxRates.class);
        CurrencyConverter converter = new CurrencyConverter(fxRates);

        when(fxRates.getRate("USD","GBP")).thenReturn(new BigDecimal("0.77"));
        BigDecimal amountFrom = new BigDecimal("3");
        BigDecimal amountTo = converter.convert(amountFrom, "USD", "USD");

        assertThat( amountFrom, is( equalTo( amountTo )));
    }

    @Test(expected = RateNotFoundException.class)
    public void convert_CurrencyNotCoveredInSuppliedRate_ThrowException() throws RateNotFoundException {
        FxRates fxRates = mock(FxRates.class);
        CurrencyConverter converter = new CurrencyConverter(fxRates);

        // Given AUD to GBP pair, but try to convert USD to GBP
        when(fxRates.getRate("AUD","GBP")).thenReturn(new BigDecimal("0.77"));

        BigDecimal amountInUsd = new BigDecimal("1");
        converter.convert(amountInUsd, "USD", "GBP");

    }

    @Test
    @Parameters(method = "suppliedRates_ConversionPair_ExpectedAmount")
    public void convert_RateRequireConversion_Calculated(
            ArrayList<CurrencyPair> suppliedPairs,
            String from, String to,
            BigDecimal expectedRate
    ) throws RateNotFoundException {

        FxRates fxRates = mock(FxRates.class);
        when(fxRates.getCurrencyPairs()).thenReturn(suppliedPairs);

        CurrencyConverter converter = new CurrencyConverter(fxRates);
        // convert amount set to 1, so the converted result equal to rate
        BigDecimal amountInSource = new BigDecimal("1");
        BigDecimal amountInTarget = converter.convert(amountInSource, from, to);

        amountInTarget = amountInTarget.setScale(AppConst.CURRENCY_DECIMAL_PLACE, RoundingMode.HALF_UP);
        assertThat(amountInTarget, is( equalTo( expectedRate ) ));
    }
    private Object suppliedRates_ConversionPair_ExpectedAmount() {
        //-----------------------------------------------
        // Case 1
        // Given USD -> GBP -> AUD
        ArrayList<CurrencyPair> pairs1 = new ArrayList<>();
        pairs1.add(new CurrencyPair("USD","GBP",new BigDecimal("0.77")));
        pairs1.add(new CurrencyPair("GBP","AUD",new BigDecimal("0.83")));

        // convert USD to AUD
        String from1 = "USD", to1 = "AUD";
        BigDecimal expected1 = new BigDecimal("0.64");

        //-----------------------------------------------
        // Case 2
        // Given
        //
        //          /-->- GBP---->----CAD
        //         /        \           \
        // USD ->--\         \           \
        //          \--->---- AUD --->-- EUR
        ArrayList<CurrencyPair> pairs2 = new ArrayList<>();
        pairs2.add(new CurrencyPair("USD","GBP",new BigDecimal("0.77")));
        pairs2.add(new CurrencyPair("USD","AUD",new BigDecimal("0.30")));
        pairs2.add(new CurrencyPair("GBP","AUD",new BigDecimal("0.83")));
        pairs2.add(new CurrencyPair("AUD","EUR",new BigDecimal("1.2")));
        pairs2.add(new CurrencyPair("GBP","CAD",new BigDecimal("0.9")));
        pairs2.add(new CurrencyPair("CAD","EUR",new BigDecimal("0.88")));

        // convert USD to AUD
        String from2 = "USD", to2 = "EUR";
        // expected USD -> AUD -> EUR = 0.3 * 1.2
        BigDecimal expected2= new BigDecimal("0.36");

        return new Object[] {
            // Case 1
            new Object[] {
                    pairs1,
                    from1, to1,
                    expected1
            },
            // Case 2
            new Object[] {
                    pairs2,
                    from2, to2,
                    expected2
            },
        };
    }
}
