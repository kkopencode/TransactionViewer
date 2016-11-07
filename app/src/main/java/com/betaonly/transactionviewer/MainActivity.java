package com.betaonly.transactionviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.product_list) RecyclerView mProductRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        setupProductListView();

    }

    private void setupProductListView() {
        mProductRecyclerView.setHasFixedSize(true);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Product> products = TransactionDataSource.getInstance().getProducts(this);
        ProductAdapter adapter = new ProductAdapter(this, products, new ProductAdapter.ProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                showTransactions(product.getSku());
            }
        });
        mProductRecyclerView.setAdapter(adapter);

    }

    private void showTransactions(String sku) {
        Intent intent = TransactionActivity.create(this, sku);
        startActivity(intent);
    }

}
