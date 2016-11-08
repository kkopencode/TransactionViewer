package com.betaonly.transactionviewer.repos;

import android.content.Context;

import com.betaonly.transactionviewer.AppConst;
import com.betaonly.transactionviewer.DebugLogger;
import com.betaonly.transactionviewer.Util;
import com.betaonly.transactionviewer.model.Product;
import com.betaonly.transactionviewer.model.Transaction;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kelvinko on 7/11/2016.
 */

public class TransactionDataSource {

    private static TransactionDataSource sInstance = new TransactionDataSource();

    private List<Transaction> mTransactions = null;
    private List<Product> mProducts = null;
    private Map<String, List<Transaction>> mTransactionMap = new HashMap<>();
    private Gson mGson = new Gson();
    private boolean inited = false;
    public static TransactionDataSource getInstance() {
        return sInstance;
    }

    private TransactionDataSource() {
        // empty private constructor
    }

    public void init(Context context) throws Exception {
        if (!inited) {
            Transaction[] transactions = readTransactionFromFile(context);
            mTransactions = Arrays.asList(transactions);

            inited = true;
        }
    }

    public List<Transaction> getTransactions() {
        return mTransactions;
    }

    public List<Transaction> getTransactions(String sku) {
        DebugLogger.d("getTransactions sku:" + sku);
        List<Transaction> productTransactions = mTransactionMap.get(sku);
        if (productTransactions == null) {
            productTransactions = new ArrayList<>();

            List<Transaction> transactions = getTransactions();
            for(Transaction t : transactions) {
                if (t.getSku().equals(sku)) {
                    productTransactions.add(t);
                }
            }
            mTransactionMap.put(sku, productTransactions);
        }

        return productTransactions;
    }

    public List<Product> getProducts() {
        if ( mProducts == null ) {
            List<Transaction> transactions = getTransactions();
            Map<String, Product> products = new HashMap<>();

            if (transactions != null) {
                for (Transaction t : transactions) {
                    Product product = products.get(t.getSku());
                    if (product == null) {
                        product = new Product(t.getSku());
                        products.put(product.getSku(), product);
                    }
                    product.increaseTransactionCount();
                }
            }

            mProducts = new ArrayList<>(products.values());
        }
        return mProducts;
    }

    // TODO: better exception handling, currently just report unable to load the data on any error
    private Transaction[] readTransactionFromFile(Context context) throws Exception{
        String content = Util.loadAssetFile(context, AppConst.DEFAULT_TRANSACTION_FILE);
        Transaction[] transactions;
        transactions = mGson.fromJson(content, Transaction[].class);

        return transactions;
    }
}
