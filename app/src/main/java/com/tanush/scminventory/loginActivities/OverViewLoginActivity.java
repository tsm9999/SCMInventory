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
import com.tanush.scminventory.OverViewActivity;
import com.tanush.scminventory.R;

public class OverViewLoginActivity extends AppCompatActivity {

    MaterialButton LoginButton;
    TextInputEditText EmailEditText, PasswordEditText;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getBaseContext(), OverViewActivity.class));
            finish();
        }
        setContentView(R.layout.activity_over_view_login);


        LoginButton = findViewById(R.id.overbtnLogin);
        EmailEditText = findViewById(R.id.overeditTextEmail);
        PasswordEditText = findViewById(R.id.overeditTextPassword);
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
                    final ProgressDialog progressDialog = new ProgressDialog(OverViewLoginActivity.this);
                    progressDialog.setMessage("Verifying Credentials...");
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(mail, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    startActivity(new Intent(getBaseContext(), OverViewActivity.class));
                                    progressDialog.dismiss();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getBaseContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                }

            }
        });
    }


}
