package com.betaonly.transactionviewer.ui.product;

import android.content.Context;

import com.betaonly.transactionviewer.model.Product;
import com.betaonly.transactionviewer.repos.TransactionDataSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by kelvinko on 8/11/2016.
 */

public class ProductController {

    ProductView mView;

    public ProductController(Context context, ProductView view) {
        mView = view;

        initTransactionDataSource(context);

        processProducts();
    }

    private void initTransactionDataSource(Context context) {
        try {
            TransactionDataSource.getInstance().init(context);
        } catch (Exception e) {
            e.printStackTrace();
            mView.onLoadTransactionsJsonFail();
        }
    }

    private void processProducts() {
        List<Product> products = TransactionDataSource.getInstance().getProducts();
        Collections.sort(products);
        mView.showProducts(products);
    }
}
