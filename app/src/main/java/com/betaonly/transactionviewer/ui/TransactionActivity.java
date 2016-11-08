package com.betaonly.transactionviewer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.betaonly.transactionviewer.AppConst;
import com.betaonly.transactionviewer.R;
import com.betaonly.transactionviewer.currency.CurrencyConverter;
import com.betaonly.transactionviewer.currency.FxRates;
import com.betaonly.transactionviewer.currency.RateNotFoundException;
import com.betaonly.transactionviewer.model.Transaction;
import com.betaonly.transactionviewer.repos.RateDataSource;
import com.betaonly.transactionviewer.repos.TransactionDataSource;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.betaonly.transactionviewer.Util.formatCurrencyString;

public class TransactionActivity extends AppCompatActivity {

    private static final String TAG = TransactionActivity.class.getSimpleName();
    private static final String EXTRA_SKU = TransactionActivity.class.getName() + ".SKU";

    private String mSku;

    @BindView(R.id.transaction_list) RecyclerView mTransactionRecyclerView;
    @BindView(R.id.total) TextView mTotalTextView;

    List<Transaction> mTransactions;
    BigDecimal mTotal = new BigDecimal("0");
    CurrencyConverter mCurrencyConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_activity);
        ButterKnife.bind(this);

        initRatesDataSource();
        initCurrencyConverter();
        handleIntent(getIntent());

        setupActionBar();
        setupTransactionList();
        setupTotalCount();
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

    private void initRatesDataSource() {
        try {
            RateDataSource.getInstance().init(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.fail_to_load_transaction_from_rates_json,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initCurrencyConverter() {
        FxRates rates = new FxRates(RateDataSource.getInstance().getRates());
        mCurrencyConverter = new CurrencyConverter(rates);
    }

    private void handleIntent(Intent intent) {
        mSku = intent.getStringExtra(EXTRA_SKU);
        handleTransactions();
    }

    private void handleTransactions() {
        Log.d(TAG, "sku:"+mSku);
        mTransactions = TransactionDataSource.getInstance().getTransactions(mSku);
        for(Transaction t : mTransactions) {
            try {
                BigDecimal converted = mCurrencyConverter.convert(t.getAmount(),
                        t.getCurrency(),
                        AppConst.HOME_CURRENCY);
                t.setConvertedCurrency(AppConst.HOME_CURRENCY);
                t.setConvertedAmount(converted);

                mTotal = mTotal.add(converted);
            } catch (RateNotFoundException e) {
                t.setConvertedCurrency(AppConst.CANNOT_CONVERT);
            }
        }
    }

    private void setupActionBar() {
        getSupportActionBar().setTitle(getString(R.string.transaction_activity_title, mSku));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupTransactionList() {
        mTransactionRecyclerView.setHasFixedSize(true);
        mTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        TransactionAdapter adapter = new TransactionAdapter(this, mTransactions);
        mTransactionRecyclerView.setAdapter(adapter);
    }

    private void setupTotalCount() {
        mTotalTextView.setText(getString(R.string.transaction_total,
                formatCurrencyString(mTotal, AppConst.HOME_CURRENCY)));
    }

    public static Intent create(Context context, String sku) {
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.putExtra(EXTRA_SKU, sku);
        return intent;
    }

}
