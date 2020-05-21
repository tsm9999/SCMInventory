package com.tanush.scminventory.loginActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tanush.scminventory.AdminActivity;
import com.tanush.scminventory.R;

public class AdminLoginActivity extends AppCompatActivity {


    MaterialButton OverViewButton, LoginButton;
    TextInputEditText EmailEditText, PasswordEditText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getBaseContext(), AdminActivity.class));
            finish();
        }
        setContentView(R.layout.activity_admin_login);


        LoginButton = findViewById(R.id.btnLogin);
        EmailEditText = findViewById(R.id.editTextEmail);
        PasswordEditText = findViewById(R.id.editTextPassword);
        firebaseAuth = FirebaseAuth.getInstance();


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = EmailEditText.getText().toString().trim();
                String password = PasswordEditText.getText().toString().trim();
                if (EmailEditText.getText().toString().equals("")) {
                    EmailEditText.setError("Required");
                    EmailEditText.requestFocus();
                }

                if (PasswordEditText.getText().toString().equals("")) {
                    PasswordEditText.setError("Required");
                    PasswordEditText.requestFocus();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(AdminLoginActivity.this);
                    progressDialog.setMessage("Verifying Credentials...");
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(mail, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    startActivity(new Intent(getBaseContext(), AdminActivity.class));
                                    progressDialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                }

            }
        });

    }
}
