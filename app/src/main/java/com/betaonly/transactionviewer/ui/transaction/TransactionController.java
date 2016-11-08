package com.betaonly.transactionviewer.ui.transaction;

import android.content.Context;

import com.betaonly.transactionviewer.AppConst;
import com.betaonly.transactionviewer.currency.CurrencyConverter;
import com.betaonly.transactionviewer.currency.FxRates;
import com.betaonly.transactionviewer.currency.RateNotFoundException;
import com.betaonly.transactionviewer.model.Transaction;
import com.betaonly.transactionviewer.repos.RateDataSource;
import com.betaonly.transactionviewer.repos.TransactionDataSource;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by kelvinko on 8/11/2016.
 */

public class TransactionController {
    private TransactionView mView;
    private CurrencyConverter mCurrencyConverter;
    private String mHomeCurrency = AppConst.HOME_CURRENCY;

    public TransactionController(Context context, TransactionView view, String sku) {
        mView = view;

        initRatesDataSource(context);
        initCurrencyConverter();

        processTransactions(sku);

    }

    private void initRatesDataSource(Context context) {
        try {
            RateDataSource.getInstance().init(context);
        } catch (Exception e) {
            e.printStackTrace();
            mView.onLoadRatesJsonFail();
        }
    }

    private void initCurrencyConverter() {
        FxRates rates = new FxRates(RateDataSource.getInstance().getRates());
        mCurrencyConverter = new CurrencyConverter(rates);
    }

    private void processTransactions(String sku) {
        List<Transaction> transactions = TransactionDataSource.getInstance().getTransactions(sku);
        BigDecimal total = convertTransactionToHomeCurrency(transactions, mHomeCurrency);
        mView.showTotalConvertedAmount(total, mHomeCurrency);
        mView.showConvertedTransaction(transactions);
    }

    /**
     * Convert all the transaction amount to corresponding home currency and return the total
     * amount in home currency
     * @param transactions List of transaction to process
     * @return total amount in home currency
     */
    private BigDecimal convertTransactionToHomeCurrency(List<Transaction> transactions, String targetCurrency) {
        BigDecimal total = new BigDecimal("0");
        for(Transaction t : transactions) {
            try {
                // Convert all the transaction amount in home currency
                BigDecimal converted = mCurrencyConverter.convert(t.getAmount(),
                        t.getCurrency(),
                        AppConst.HOME_CURRENCY);
                t.setConvertedCurrency(targetCurrency);
                t.setConvertedAmount(converted);

                // Sum the total amount in home currency
                total = total.add(converted);
            } catch (RateNotFoundException e) {
                // Mark it as not able to convert, so the ui can display error message
                t.setConvertedCurrency(AppConst.CANNOT_CONVERT);
            }
        }

        return total;
    }

}
