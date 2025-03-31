package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.ModelView;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private ModelView modelView;
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


        // Observer la liste des voyages
        modelView = new ViewModelProvider(this).get(ModelView.class);
        modelView.getConnexion().observe(this, result -> {
            switch (result) {
                case 201: {
                    Intent intent = new Intent(this, AccueilActivity.class);
                    startActivity(intent);
                    break;
                }
                case 401: {
                    Toast.makeText(this, "L'email est incorrect !", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 402: {
                    Toast.makeText(this, "Le mot de passe est incorrect !", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 404: {
                    Toast.makeText(this, "Une erreur de connexion est subvenue !", Toast.LENGTH_SHORT).show();
                    break;
                }
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

        buttonCreateAccount.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });
    }
}