package com.betaonly.transactionviewer.ui.product;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.betaonly.transactionviewer.model.Product;
import com.betaonly.transactionviewer.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kelvinko on 6/11/2016.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    List<Product> mProducts;
    Resources mRes;
    ProductClickListener mListener;

    public ProductAdapter(Context context, List<Product> products, ProductClickListener listener) {
        mProducts = new ArrayList<>(products);
        mRes = context.getResources();
        mListener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);
        ProductViewHolder vh = new ProductViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Product product = mProducts.get(position);
        holder.sku.setText(product.getSku());
        holder.transactionCount.setText(mRes.getQuantityString(R.plurals.transaction_count,
                product.getTransactionCount(),
                product.getTransactionCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public interface ProductClickListener {
        void onProductClick(Product product);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sku) TextView sku;
        @BindView(R.id.transaction_count) TextView transactionCount;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
