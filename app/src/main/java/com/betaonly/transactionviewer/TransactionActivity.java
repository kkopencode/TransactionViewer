package com.betaonly.transactionviewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionActivity extends AppCompatActivity {

    private static final String EXTRA_SKU = TransactionActivity.class.getName() + ".SKU";

    private String mSku;

    @BindView(R.id.transaction_list) RecyclerView mTransactionRecyclerView;
    @BindView(R.id.total) TextView mTotalTextView;

    List<Transaction> mTransactions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_activity);

        handleIntent(getIntent());
        ButterKnife.bind(this);

        setupTransactionList();
        setupTotalCount();
    }

    private void handleIntent(Intent intent) {
        mSku = intent.getStringExtra(EXTRA_SKU);
        mTransactions = TransactionDataSource.getInstance().getTransactions(this, mSku);
    }

    private void setupTransactionList() {
        mTransactionRecyclerView.setHasFixedSize(true);
        mTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        TransactionAdapter adapter = new TransactionAdapter(mTransactions);
        mTransactionRecyclerView.setAdapter(adapter);
    }

    private void setupTotalCount() {
        mTotalTextView.setText("Total : 123");
    }

    public static Intent create(Context context, String sku) {
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.putExtra(EXTRA_SKU, sku);
        return intent;
    }

}
