package com.betaonly.transactionviewer.ui.transaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.betaonly.transactionviewer.AppConst;
import com.betaonly.transactionviewer.R;
import com.betaonly.transactionviewer.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.betaonly.transactionviewer.utils.Util.formatCurrencyString;

/**
 * Created by kelvinko on 6/11/2016.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private static final String TAG = TransactionAdapter.class.getSimpleName();

    List<Transaction> mTransactions;
    Context mContext;
    public TransactionAdapter(Context context, List<Transaction> transactions) {
        mContext = context;
        mTransactions = new ArrayList<>(transactions);
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_row, parent, false);
        TransactionViewHolder vh = new TransactionViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        Transaction transaction = mTransactions.get(position);
        String amountStr = formatCurrencyString(transaction.getAmount(), transaction.getCurrency());
        holder.amount.setText(amountStr);

        // Show cannot convert text if rate cannot be calculated from supplied rates
        if (transaction.getConvertedCurrency().equals(AppConst.CANNOT_CONVERT)) {
            holder.amountConverted.setText(
                    mContext.getString(R.string.cannot_converted_to, AppConst.HOME_CURRENCY));

        // Otherwise, Show the converted currency
        } else {
            String amountConvertedStr = formatCurrencyString(transaction.getConvertedAmount(),
                    transaction.getConvertedCurrency());
            holder.amountConverted.setText(amountConvertedStr);
        }
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.amount) TextView amount;
        @BindView(R.id.amount_converted) TextView amountConverted;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
