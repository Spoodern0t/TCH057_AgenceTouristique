package com.example.agencetouristique7;
//

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonSignIn.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (email.isEmpty()){
                Toast.makeText(MainActivity.this, "Please fill in email...", Toast.LENGTH_LONG).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in password...", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, AccueilActivity.class);
            startActivity(intent);
        });


        buttonSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccueilActivity.class);
            startActivity(intent);
        });

        buttonCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });
    }
}