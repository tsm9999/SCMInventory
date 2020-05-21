package com.tanush.scminventory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tanush.scminventory.adapters.StockAdapter;
import com.tanush.scminventory.models.ProductMFG;

import java.util.ArrayList;

public class ProductOverviewActivity extends AppCompatActivity {

    final ArrayList<ProductMFG> stocks = new ArrayList<>();
    TextView ProdIDTextView;
    TextView ProdNameTextView, PriceTextView;
    String id;
    MaterialButton ButtonRefresh;
    StockAdapter madapter;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    int stock;
    private FirebaseFirestore db;
    private CollectionReference StockRef, InventoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_overview);
        db = FirebaseFirestore.getInstance();
        Intent i = getIntent();
        id = i.getStringExtra("ID");

        StockRef = db.collection("Inventory").document(id).collection("stocks");

        ProdIDTextView = findViewById(R.id.textViewID);
        ProdNameTextView = findViewById(R.id.textViewName);
        PriceTextView = findViewById(R.id.textViewPrice);

        ButtonRefresh = findViewById(R.id.buttonRefresh);

        db = FirebaseFirestore.getInstance();

        InventoryRef = db.collection("Inventory");
        InventoryRef.document(id).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("EditStock", "DocumentSnapshot data: " + document.getData());
                        ProdIDTextView.setText("Product ID: " + document.get("prodID").toString());
                        ProdNameTextView.setText("Product Name: " + document.get("prodName").toString());
                        PriceTextView.setText("Price: " + document.get("prodPrice").toString());
                    } else {
                        Log.d("EditStock", "No such document");
                    }
                } else {
                    Log.d("EditStock", "get failed with ", task.getException());
                }
            }
        });

        Toast.makeText(this, "Getting Stocks...", Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.stockRecycler);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        getData();

        ButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductOverviewActivity.this, "Loading...", Toast.LENGTH_LONG).show();
                getData();
                stocks.clear();
            }
        });
    }

    void getData() {
        db.collection("Inventory").document(id).collection("stocks")
                .orderBy("month", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductMFG p = new ProductMFG(id, Integer.parseInt(document.getId()), document.get("day").toString(), document.get("month").toString(), document.get("year").toString(), Integer.parseInt(document.get("stock").toString()));
                                stocks.add(p);
                                Log.d("Document Fetch", document.getId() + " => " + document.getData());
                            }
                            madapter = new StockAdapter(stocks);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(madapter);
                            madapter.notifyDataSetChanged();

                        } else {
                            Log.d("Document Fetch", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
