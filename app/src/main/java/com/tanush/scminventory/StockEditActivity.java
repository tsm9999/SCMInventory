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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tanush.scminventory.adapters.StockAdminAdapter;
import com.tanush.scminventory.models.ProductMFG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockEditActivity extends AppCompatActivity {

    final ArrayList<ProductMFG> mstocks = new ArrayList<>();
    TextView ProdIDTextView, StockTextView;
    TextInputEditText ProdNameEditText, PriceEditText, productQuantity;
    MaterialButton addProductButton;
    MaterialButton deleteProductButton;
    TextInputEditText productMfgDay, productMfgMonth, productMfgYear;
    StockAdminAdapter madapter;
    RecyclerView recyclerView;
    LinearLayoutManager LayoutManager;

    String id;
    String d = "";

    private FirebaseFirestore db;
    private CollectionReference InventoryRef;

    private StockAdminAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_edit);

        ProdIDTextView = findViewById(R.id.textViewID);
        ProdNameEditText = findViewById(R.id.editTextName);
        PriceEditText = findViewById(R.id.editTextPrice);
        StockTextView = findViewById(R.id.textViewStock);
        deleteProductButton = findViewById(R.id.deleteProductButton);

        Intent i = getIntent();
        id = i.getStringExtra("ID");

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
                        ProdNameEditText.setText(document.get("prodName").toString());
//                        StockTextView.setText("Stock: " + document.get("prodQuantity").toString());
                        PriceEditText.setText(document.get("prodPrice").toString());
                    } else {
                        Log.d("EditStock", "No such document");
                    }
                } else {
                    Log.d("EditStock", "get failed with ", task.getException());
                }
            }
        });


        productQuantity = findViewById(R.id.product_quantity);
        productMfgDay = findViewById(R.id.product_mfgDay);
        productMfgMonth = findViewById(R.id.product_mfgMonth);
        productMfgYear = findViewById(R.id.product_mfgYear);
        addProductButton = findViewById(R.id.addProductButton);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ProdNameEditText.getText().toString().equals("") || ProdIDTextView.getText().toString().equals("") || ProdIDTextView.getText().toString().equals("")) {
                    Toast.makeText(StockEditActivity.this, "Enter All Details", Toast.LENGTH_SHORT).show();
                    ProdIDTextView.setError("Enter Valid Data!");
                    ProdIDTextView.requestFocus();
                    return;
                }

                if (productMfgDay.getText().toString().equals("") || productMfgMonth.getText().toString().length() != 2) {
                    productMfgDay.setError("Enter Valid Day");
                    productMfgDay.requestFocus();
                }

                if (productMfgMonth.getText().toString().equals("") || productMfgMonth.getText().toString().length() != 2) {
                    productMfgMonth.setError("Enter Valid Month");
                    productMfgMonth.requestFocus();
                    return;
                }

                if (productMfgYear.getText().toString().equals("") || productMfgYear.getText().toString().length() != 4) {
                    productMfgYear.setError("Enter Valid Year");
                    productMfgYear.requestFocus();
                    return;
                }
                int Day = Integer.parseInt(productMfgDay.getText().toString());
                int Month = Integer.parseInt(productMfgMonth.getText().toString());
                int Year = Integer.parseInt(productMfgYear.getText().toString());

                if (Month >= 1 || Month <= 12) {
                    if (Month % 2 == 1) {
                        if (Day < 1 || Day > 31) {
                            productMfgDay.setError("Invalid Date!");
                            productMfgDay.requestFocus();
                            return;
                        }
                    } else {
                        if (Day < 1 || Day > 30) {
                            productMfgDay.setError("Invalid Date!");
                            productMfgDay.requestFocus();
                            return;
                        }
                    }
                }

                saveNote();
            }
        });

        recyclerView = findViewById(R.id.adminStockRecycler);
        LayoutManager = new LinearLayoutManager(getApplicationContext());

        getAdminData();

        deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InventoryRef.document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Document Delete", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Document Delete", "Error deleting document", e);
                            }
                        });
                Intent i = new Intent(StockEditActivity.this, AdminActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    private void saveNote() {

        String prName = ProdNameEditText.getText().toString();
        String prID = ProdIDTextView.getText().toString();
        int prPrice = Integer.parseInt(PriceEditText.getText().toString());
        int Day = Integer.parseInt(productMfgDay.getText().toString());
        int Month = Integer.parseInt(productMfgMonth.getText().toString());

        final int quantity = Integer.parseInt(productQuantity.getText().toString());

        final DocumentReference productRef = FirebaseFirestore.getInstance()
                .collection("Inventory").document(id);
        final DocumentReference productRef2 = FirebaseFirestore.getInstance()
                .collection("Inventory").document(id);


        Toast.makeText(this, "Adding Stock...", Toast.LENGTH_SHORT).show();

        if (Day <= 15) {
            d = "15";
        } else {
            if (Month % 2 == 0) {
                d = "30";
            } else {
                d = "31";
            }
        }

        final Map<String, String> stock = new HashMap();
        stock.put("prodID", id);
        stock.put("stock", productQuantity.getText().toString());
        stock.put("day", d);
        stock.put("month", productMfgMonth.getText().toString());
        stock.put("year", productMfgYear.getText().toString());

        productRef.collection("stocks").document(d + productMfgMonth.getText().toString() + productMfgYear.getText().toString())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("ProductFetch", "DocumentSnapshot data: " + document.getData());

                        productRef2.collection("stocks").document(d + productMfgMonth.getText().toString() + productMfgYear.getText().toString())
                                .update("stock", Integer.parseInt(document.get("stock").toString()) + quantity)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("UpdateDoc", "DocumentSnapshot successfully updated!");
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("UpdateDoc", "Error updating document", e);
                                    }
                                });
                    } else {
                        Log.d("ProductFetch", "No such document");

                        productRef2.collection("stocks").document(d + productMfgMonth.getText().toString() + productMfgYear.getText().toString())
                                .set(stock)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(StockEditActivity.this, "Stock Added", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(StockEditActivity.this, "Stock Error Occurred", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Log.d("ProductFetch", "get failed with ", task.getException());
                }
            }
        });
    }


    void getAdminData() {
        db.collection("Inventory").document(id).collection("stocks")
                .orderBy("month", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductMFG p = new ProductMFG(id, Integer.parseInt(document.getId()), document.get("day").toString(), document.get("month").toString(), document.get("year").toString(), Integer.parseInt(document.get("stock").toString()));
                                mstocks.add(p);
                                Log.d("Document Fetch", document.getId() + " => " + document.getData());
                            }
                            adapter = new StockAdminAdapter(mstocks);
                            recyclerView.setLayoutManager(LayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d("Document Fetch", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}

