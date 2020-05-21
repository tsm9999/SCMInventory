package com.tanush.scminventory.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tanush.scminventory.R;
import com.tanush.scminventory.models.Product;

public class ProductsAdminAdapter extends FirestoreRecyclerAdapter<Product, ProductsAdminAdapter.ProductsHolder> {

    //For Button Click Interface
    private OnItemClickListener listener;


    public ProductsAdminAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductsHolder holder, int position, @NonNull Product model) {
        holder.textViewID.setText(model.getProdID());
        holder.textViewName.setText(model.getProdName());
    }

    @NonNull
    @Override
    public ProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_layout_admin,
                parent, false);
        return new ProductsHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // For Button Click
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class ProductsHolder extends RecyclerView.ViewHolder {
        TextView textViewID;
        TextView textViewName;
        MaterialButton CheckButton;

        public ProductsHolder(View itemView) {
            super(itemView);
            textViewID = itemView.findViewById(R.id.textViewProductID);
            textViewName = itemView.findViewById(R.id.textViewProductName);
            CheckButton = itemView.findViewById(R.id.buttonCheckStock);

            CheckButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
}