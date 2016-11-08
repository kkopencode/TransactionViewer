package com.betaonly.transactionviewer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.betaonly.transactionviewer.R;
import com.betaonly.transactionviewer.model.Product;
import com.betaonly.transactionviewer.ui.product.ProductAdapter;
import com.betaonly.transactionviewer.ui.product.ProductController;
import com.betaonly.transactionviewer.ui.product.ProductView;
import com.betaonly.transactionviewer.ui.transaction.TransactionActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ProductView {

    @BindView(R.id.product_list) RecyclerView mProductRecyclerView;

    ProductController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        setupActionBar();
        setupProductListView();

        mController = new ProductController(getApplicationContext(), this);
    }

    /*********************************
     * Setup Android view components
     *********************************/

    private void setupActionBar() {
        getSupportActionBar().setTitle(getString(R.string.main_activity_title));
    }

    private void setupProductListView() {
        mProductRecyclerView.setHasFixedSize(true);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /*********************************
     * View actions
     *********************************/

    @Override
    public void onLoadTransactionsJsonFail() {
        Toast.makeText(this, R.string.fail_to_load_transaction_from_transactions_json,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProducts(List<Product> products) {
        ProductAdapter adapter = new ProductAdapter(this, products, new ProductAdapter.ProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                goToTransactionDetails(product.getSku());
            }
        });
        mProductRecyclerView.setAdapter(adapter);
    }

    private void goToTransactionDetails(String sku) {
        Intent intent = TransactionActivity.create(this, sku);
        startActivity(intent);
    }
}
