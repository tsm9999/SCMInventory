package com.tanush.scminventory.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanush.scminventory.R;
import com.tanush.scminventory.models.ProductMFG;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyViewHolder> {

    private List<ProductMFG> stocks;

    public StockAdapter(List<ProductMFG> stocks) {
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductMFG stock = stocks.get(position);
        holder.mfg.setText(stock.getDay() + "/" + stock.getMonth() + "/" + stock.getYear());
        holder.stock.setText(String.valueOf(stock.getStock()));
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mfg;
        TextView stock;

        MyViewHolder(View view) {
            super(view);
            mfg = itemView.findViewById(R.id.textViewMFGDate);
            stock = itemView.findViewById(R.id.textViewstock);
        }
    }
}
