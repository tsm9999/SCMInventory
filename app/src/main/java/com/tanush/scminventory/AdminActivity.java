package com.tanush.scminventory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tanush.scminventory.adapters.ProductsAdminAdapter;
import com.tanush.scminventory.models.Product;

public class AdminActivity extends AppCompatActivity {

    FloatingActionButton AddProductButton;
    boolean doubleBackToExitPressedOnce = false;


    private FirebaseFirestore db;
    private CollectionReference InventoryRef;

    private ProductsAdminAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        AddProductButton = findViewById(R.id.add_new_product);

        AddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, AddProductActivity.class);
                startActivity(i);
            }
        });


        //-------------------------Firestore
        db = FirebaseFirestore.getInstance();
        InventoryRef = db.collection("Inventory");

        setUpRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            finish();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getBaseContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        //super.onCreateOptionsMenu(menu)
        return true;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to EXIT", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void setUpRecyclerView() {
        Query query = InventoryRef.orderBy("prodID", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Product> products = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new ProductsAdminAdapter(products);

        RecyclerView recyclerView = findViewById(R.id.recyclerAdminProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //For Button Click
        adapter.setOnItemClickListener(new ProductsAdminAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Product product = documentSnapshot.toObject(Product.class);
                String id = documentSnapshot.getId();
                //String path = documentSnapshot.getReference().getPath();
                Toast.makeText(AdminActivity.this, "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AdminActivity.this, StockEditActivity.class);
                i.putExtra("ID", id);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
