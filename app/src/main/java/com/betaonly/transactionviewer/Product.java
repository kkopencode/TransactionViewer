package com.betaonly.transactionviewer;

/**
 * Created by kelvinko on 6/11/2016.
 */
public class Product {

    private String mSku;
    private int mTransactionCount;

    public Product(String sku) {
        mSku = sku;
        mTransactionCount = 0;
    }

    public String getSku() {
        return mSku;
    }

    public int getTransactionCount() {
        return mTransactionCount;
    }

    public void increaseTransactionCount() {
        mTransactionCount++;
    }
}
