package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.ClientViewModel;

public class MainActivity extends AppCompatActivity {

    private ClientViewModel clientViewModel;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonCreate;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreate = findViewById(R.id.buttonCreateAccount);

        // Reprend l'email et mot de passe d'une nouvelle inscription
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent resultData = result.getData();
                        if (resultData != null) {
                            String email = resultData.getStringExtra("EMAIL");
                            String mdp = resultData.getStringExtra("MDP");

                            // Remplir les champs avec les informations de la nouvelle inscription
                            editTextEmail.setText(email);
                            editTextPassword.setText(mdp);
                        }
                    }
                }
        );


        // Observer la connexion
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        clientViewModel.getConnexion().observe(this, succes -> {
            Toast.makeText(this, "Connexion !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AccueilActivity.class);
            startActivity(intent);
        });

        // Observer l'erreur
        clientViewModel.getErreur().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });


        buttonSignIn.setOnClickListener(v -> {

            String email = editTextEmail.getText().toString();
            String mdp = editTextPassword.getText().toString();

            if (email.isEmpty()){
                Toast.makeText(MainActivity.this, "Please fill in email...", Toast.LENGTH_LONG).show();
                return;
            }

            if (mdp.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in password...", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, AccueilActivity.class);
            startActivity(intent);
        });


        buttonSignIn.setOnClickListener(v -> {

            String email = editTextEmail.getText().toString();
            String mdp = editTextPassword.getText().toString();

            if (email.isEmpty() || mdp.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs !", Toast.LENGTH_SHORT).show();
            } else {
                clientViewModel.postConnexion(email, mdp);
            }
        });


        buttonCreate.setOnClickListener(v -> {

            String email = editTextEmail.getText().toString();
            String mdp = editTextPassword.getText().toString();

            Intent intent = new Intent(MainActivity.this, InscriptionActivity.class);
            intent.putExtra("EMAIL", email);
            intent.putExtra("MDP", mdp);
            launcher.launch(intent);
        });
    }
}