package com.betaonly.transactionviewer.repos;

import android.content.Context;

import com.betaonly.transactionviewer.AppConst;
import com.betaonly.transactionviewer.model.Product;
import com.betaonly.transactionviewer.model.Transaction;
import com.betaonly.transactionviewer.Util;
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

    public static TransactionDataSource getInstance() {
        return sInstance;
    }

    private TransactionDataSource() {
        // empty private constructor
    }

    public List<Transaction> getTransactions(Context context) {
        if (mTransactions == null) {
            Transaction[] transactions = readTransactionFromFile(context);
            mTransactions = Arrays.asList(transactions);
        }

        return mTransactions;
    }

    public List<Transaction> getTransactions(Context context, String sku) {
        List<Transaction> productTransactions = mTransactionMap.get(sku);
        if (productTransactions == null) {
            productTransactions = new ArrayList<>();
            List<Transaction> transactions = getTransactions(context);
            for(Transaction t : transactions) {
                if (t.getSku().equals(sku)) {
                    productTransactions.add(t);
                }
            }
            mTransactionMap.put(sku, productTransactions);
        }
        return productTransactions;
    }

    public List<Product> getProducts(Context context) {
        if ( mProducts == null ) {
            List<Transaction> transactions = getTransactions(context);
            Map<String, Product> products = new HashMap<>();
            for(Transaction t : transactions) {
                Product product = products.get(t.getSku());
                if (product == null) {
                    product = new Product(t.getSku());
                    products.put(product.getSku(), product);
                }
                product.increaseTransactionCount();
            }
            mProducts = new ArrayList<>(products.values());
        }
        return mProducts;
    }
    private Transaction[] readTransactionFromFile(Context context) {
        String content = Util.loadAssetFile(context, AppConst.DEFAULT_TRANSACTION_FILE);
        Transaction[] transactions = mGson.fromJson(content, Transaction[].class);
        return transactions;
    }
}
