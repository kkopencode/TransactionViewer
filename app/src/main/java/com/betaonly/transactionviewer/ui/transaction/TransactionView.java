package com.betaonly.transactionviewer.ui.transaction;

import com.betaonly.transactionviewer.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by kelvinko on 8/11/2016.
 */

public interface TransactionView {
    void onLoadRatesJsonFail();
    void showConvertedTransaction(List<Transaction> transactions);
    void showTotalConvertedAmount(BigDecimal amount, String currency);
}
