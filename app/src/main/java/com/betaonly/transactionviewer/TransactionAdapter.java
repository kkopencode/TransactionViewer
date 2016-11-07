package com.betaonly.transactionviewer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kelvinko on 6/11/2016.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    List<Transaction> mTransactions;
    public TransactionAdapter(List<Transaction> transactions) {
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
        String amountStr = transaction.getCurrency() + transaction.getAmount();
        holder.amount.setText(amountStr);

        String amountGbpStr = transaction.getCurrency() + transaction.getAmount();
        holder.amountGbp.setText(amountGbpStr);
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.amount) TextView amount;
        @BindView(R.id.amount_in_gbp) TextView amountGbp;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
