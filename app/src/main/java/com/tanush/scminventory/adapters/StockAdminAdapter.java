package com.tanush.scminventory.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tanush.scminventory.R;
import com.tanush.scminventory.models.ProductMFG;

import java.util.ArrayList;

public class StockAdminAdapter extends RecyclerView.Adapter<StockAdminAdapter.Viewholder> {

    private ArrayList<ProductMFG> stocks;

    public StockAdminAdapter(ArrayList<ProductMFG> stocks) {
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public StockAdminAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_admin_card_layout, parent, false);
        return new StockAdminAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdminAdapter.Viewholder holder, int position) {

        ProductMFG stock = stocks.get(position);
        holder.mfgdate.setText(stock.getDay() + "/" + stock.getMonth() + "/" + stock.getYear());
        holder.StockTextView.setText(String.valueOf(stock.getStock()));
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        TextView mfgdate;
        TextView StockTextView;
        TextView StockChangeTextView;
        TextInputEditText StockEdit;
        ImageButton RemoveButton, AddButton;

        public Viewholder(@NonNull View itemView) {
            super(itemView);


            mfgdate = itemView.findViewById(R.id.textViewMFGDate);
            StockTextView = itemView.findViewById(R.id.textViewStock);
            StockEdit = itemView.findViewById(R.id.editTextStock);
            StockChangeTextView = itemView.findViewById(R.id.textViewStockUpdate);
            RemoveButton = itemView.findViewById(R.id.imageButtonRemove);
            AddButton = itemView.findViewById(R.id.imageButtonAdd);

            AddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ProductMFG p = stocks.get(position);
                    int v = p.getStock() + Integer.parseInt(StockEdit.getText().toString());
                    p.setStock(v);

                    DocumentReference productRef2 = FirebaseFirestore.getInstance()
                            .collection("Inventory").document(String.valueOf(p.getProdID()));

                    productRef2.collection("stocks").document(String.valueOf(p.getDocID()))
                            .update("stock", v)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("UpdateDoc", "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("UpdateDoc", "Error updating document", e);
                                }
                            });

                    StockTextView.setText(String.valueOf(v));
                    StockEdit.setText("");
                    StockChangeTextView.setText("Stock Updated!");
                }
            });

            RemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ProductMFG p = stocks.get(position);
                    int v = p.getStock() - Integer.parseInt(StockEdit.getText().toString());
                    if (v == 0) {

                        DocumentReference productRef2 = FirebaseFirestore.getInstance()
                                .collection("Inventory").document(String.valueOf(p.getProdID()));
                        productRef2.collection("stocks").document(String.valueOf(p.getDocID()))
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("DeleteDoc", "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("DeleteDoc", "Error deleting document", e);
                                    }
                                });

                        StockTextView.setText(String.valueOf(v));
                        StockEdit.setText("");
                        StockChangeTextView.setText("Stock Updated!");

                    } else if (v < 0) {
                        StockEdit.setError("Stock Cannot be reduced than available!");
                        StockEdit.requestFocus();
                        return;
                    } else {
                        p.setStock(v);

                        DocumentReference productRef2 = FirebaseFirestore.getInstance()
                                .collection("Inventory").document(String.valueOf(p.getProdID()));

                        productRef2.collection("stocks").document(String.valueOf(p.getDocID()))
                                .update("stock", v)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("UpdateDoc", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("UpdateDoc", "Error updating document", e);
                                    }
                                });

                        StockTextView.setText(String.valueOf(v));
                        StockEdit.setText("");
                        StockChangeTextView.setText("Stock Updated!");
                    }
                }
            });

        }
    }
}