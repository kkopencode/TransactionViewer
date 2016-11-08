package com.betaonly.transactionviewer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.betaonly.transactionviewer.R;
import com.betaonly.transactionviewer.model.Product;
import com.betaonly.transactionviewer.repos.TransactionDataSource;

import java.util.Collections;
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

        initTransactionDataSource();
        setupActionBar();
        setupProductListView();

    }

    private void initTransactionDataSource() {
        try {
            TransactionDataSource.getInstance().init(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.fail_to_load_transaction_from_transactions_json,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setupActionBar() {
        getSupportActionBar().setTitle(getString(R.string.main_activity_title));
    }

    private void setupProductListView() {
        mProductRecyclerView.setHasFixedSize(true);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Product> products = TransactionDataSource.getInstance().getProducts();
        Collections.sort(products);
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
