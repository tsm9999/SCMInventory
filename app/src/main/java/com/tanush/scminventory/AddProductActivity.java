package com.tanush.scminventory;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tanush.scminventory.models.Product;

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    TextInputEditText productID, productName, productPrice, productQuantity;
    MaterialButton addProductButton;

    TextInputEditText productMfgDay, productMfgMonth, productMfgYear;

    FirebaseFirestore db;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        setTitle("Add Product");

        db = FirebaseFirestore.getInstance();

        productID = findViewById(R.id.product_id);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productQuantity = findViewById(R.id.product_quantity);
        productMfgDay = findViewById(R.id.product_mfgDay);
        productMfgMonth = findViewById(R.id.product_mfgMonth);
        productMfgYear = findViewById(R.id.product_mfgYear);
        addProductButton = findViewById(R.id.addProductButton);

        id = productID.getText().toString();

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (productName.getText().toString().equals("") || productID.getText().toString().equals("") || productPrice.getText().toString().equals("")) {
                    Toast.makeText(AddProductActivity.this, "Enter All Details", Toast.LENGTH_SHORT).show();
                    productID.setError("Enter Valid Data!");
                    productID.requestFocus();
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
                //checkIfProductExists();  //If product not found creates new product
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    private void checkIfProductExists() {

        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        DocumentReference docRef = db.collection("Inventory").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("ProductFetch", "DocumentSnapshot data: " + document.getData());
                        Toast.makeText(AddProductActivity.this, "Product already exist!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("ProductFetch", "No such document");
                        saveNote();
                    }
                } else {
                    Log.d("ProductFetch", "get failed with ", task.getException());
                }
            }
        });
    }

    private void saveNote() {

        String prName = productName.getText().toString();
        String prID = productID.getText().toString();
        int prPrice = Integer.parseInt(productPrice.getText().toString());
        int Day = Integer.parseInt(productMfgDay.getText().toString());
        int Month = Integer.parseInt(productMfgMonth.getText().toString());

        String id = productID.getText().toString();
        String quantity = productQuantity.getText().toString();

        DocumentReference productRef = FirebaseFirestore.getInstance()
                .collection("Inventory").document(id);

        productRef.set(new Product(prID, prPrice, prName))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddProductActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProductActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                });

        String d = "";
        if (Day <= 15) {
            d = "15";
        } else {
            if (Month % 2 == 0) {
                d = "30";
            } else {
                d = "31";
            }
        }

        Map<String, String> stock = new HashMap();
        stock.put("prodID", id);
        stock.put("stock", quantity);
        stock.put("day", d);
        stock.put("month", productMfgMonth.getText().toString());
        stock.put("year", productMfgYear.getText().toString());

        productRef.collection("stocks").document(d + productMfgMonth.getText().toString() + productMfgYear.getText().toString())
                .set(stock)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddProductActivity.this, "Stock Added", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProductActivity.this, "Stock Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
