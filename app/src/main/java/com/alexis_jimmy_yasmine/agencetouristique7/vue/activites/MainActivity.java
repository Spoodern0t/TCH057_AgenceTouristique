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


        buttonCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });
    }
}