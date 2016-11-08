package com.betaonly.transactionviewer.ui.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.betaonly.transactionviewer.R;
import com.betaonly.transactionviewer.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.betaonly.transactionviewer.utils.Util.formatCurrencyString;

public class TransactionActivity extends AppCompatActivity implements TransactionView{

    private static final String EXTRA_SKU = TransactionActivity.class.getName() + ".SKU";

    @BindView(R.id.transaction_list) RecyclerView mTransactionRecyclerView;
    @BindView(R.id.total) TextView mTotalTextView;

    TransactionController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_activity);
        ButterKnife.bind(this);

        String sku = getSkuFromIntent(getIntent());
        setupActionBar(sku);
        setupTransactionList();

        mController = new TransactionController(getApplicationContext(), this, sku);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Create intent for other activity to start this activity
     * @param context
     * @param sku
     * @return Intent to start this activity
     */
    public static Intent create(Context context, String sku) {
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.putExtra(EXTRA_SKU, sku);
        return intent;
    }

    private String getSkuFromIntent(Intent intent) {
        return intent.getStringExtra(EXTRA_SKU);
    }

    /*********************************
     * Setup Android view components
     *********************************/

    private void setupActionBar(String sku) {
        getSupportActionBar().setTitle(getString(R.string.transaction_activity_title, sku));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupTransactionList() {
        mTransactionRecyclerView.setHasFixedSize(true);
        mTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /*********************************
     * View actions
     *********************************/

    @Override
    public void onLoadRatesJsonFail() {
        Toast.makeText(this, R.string.fail_to_load_transaction_from_rates_json,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showConvertedTransactions(List<Transaction> transactions) {
        TransactionAdapter adapter = new TransactionAdapter(this, transactions);
        mTransactionRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showTotalConvertedAmount(BigDecimal amount, String currency) {
        mTotalTextView.setText(getString(R.string.transaction_total,
                formatCurrencyString(amount, currency)));
    }
}
