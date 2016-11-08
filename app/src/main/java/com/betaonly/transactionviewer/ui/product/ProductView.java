package com.betaonly.transactionviewer.ui.product;

import com.betaonly.transactionviewer.model.Product;

import java.util.List;

/**
 * Created by kelvinko on 8/11/2016.
 */

public interface ProductView {
    void onLoadTransactionsJsonFail();
    void showProducts(List<Product> products);
}
