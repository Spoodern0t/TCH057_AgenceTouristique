package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.ModelView;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.VoyagesAdaptateur;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private ModelView modelView;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreate = findViewById(R.id.buttonCreateAccount);

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

        // Observer la liste des voyages
        modelView = new ViewModelProvider(this).get(ModelView.class);
        modelView.getConnexion().observe(this, message -> {
            if (message.equals("Connexion en cours")) {
                Intent intent = new Intent(this, AccueilActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });


        buttonSignIn.setOnClickListener(v -> {

            String email = editTextEmail.getText().toString();
            String mdp = editTextPassword.getText().toString();

            if (email.isEmpty() || mdp.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs !", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    modelView.postConnexion(email, mdp);
                } catch (JSONException e) {
                    //
                }
            }
        });

        buttonCreate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });
    }
}