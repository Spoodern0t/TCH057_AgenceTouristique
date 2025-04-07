package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.AgenceViewModel;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;

public class InscriptionActivity extends AppCompatActivity {

    private final String FORMAT_EMAIL = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private AgenceViewModel modelView;
    private EditText editFirstName, editLastName, editAge, editPhone, editAddress,
            editCity, editProvince, editEmail, editPassword;
    private Button btnSignUp, btnAnnuler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        editFirstName = findViewById(R.id.editTextFirstNameInscription);
        editLastName = findViewById(R.id.editTextLastNameInscription);
        editAge = findViewById(R.id.editTextAgeInscription);
        editPhone = findViewById(R.id.editTextPhoneInscription);
        editAddress = findViewById(R.id.editTextAddressInscription);
        editCity = findViewById(R.id.editTextCityInscription);
        editProvince = findViewById(R.id.editTextProvinceInscription);
        editEmail = findViewById(R.id.editTextEmailRegisterInscription);
        editPassword = findViewById(R.id.editTextPasswordRegisterInscription);
        btnSignUp = findViewById(R.id.buttonSignUpInscription);
        btnAnnuler = findViewById(R.id.buttonReturnLoginInscription);

        // Remplir l'email et le mot de passe venant de la page de connexion
        Intent intent = getIntent();
        String iEmail = intent.getStringExtra("EMAIL");
        String iMdp = intent.getStringExtra("MDP");
        editEmail.setText(iEmail);
        editPassword.setText(iMdp);


        // Observer l'inscription
        modelView = new ViewModelProvider(this).get(AgenceViewModel.class);
        modelView.getInscription().observe(this, client -> {

            Toast.makeText(this, "Inscrit !", Toast.LENGTH_SHORT).show();
            // Renvoyer l'email et le mot de passe
            Intent resultIntent = new Intent();
            resultIntent.putExtra("EMAIL", client.getEmail());
            resultIntent.putExtra("MDP", client.getMdp());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Observer l'erreur
        modelView.getErreur().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });


        btnSignUp.setOnClickListener(v -> {

            String firstName = editFirstName.getText().toString();
            String lastName = editLastName.getText().toString();
            String ageString = editAge.getText().toString();
            String phone = editPhone.getText().toString();
            String address = editAddress.getText().toString();
            String city = editCity.getText().toString();
            String province = editProvince.getText().toString();
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();

            if (lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || password.isEmpty() ||
                    ageString.isEmpty() || phone.isEmpty() || address.isEmpty() || city.isEmpty() || province.isEmpty()) {

                Toast.makeText(InscriptionActivity.this, "Veuillez remplir tous les champs !", Toast.LENGTH_LONG).show();

            } else if (!email.matches(FORMAT_EMAIL)) {

                Toast.makeText(InscriptionActivity.this, "Veuillez saisir une addresse courriel valide!", Toast.LENGTH_LONG).show();

            } else if (estMotPasseValide(password)) {

                int age = Integer.parseInt(ageString);
                String adresse = address + ", " + city + ", " + province;

                Client client = new Client(null, lastName, firstName, email, password, age, phone, adresse);
                modelView.postClient(client);
            }
        });


        btnAnnuler.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private boolean estMotPasseValide(String motPasse){
        //un mot de passe valide a au moins 8 caractères
        if (motPasse.length() > 7){
            //un mot de passe valide est sans espace
            if (!motPasse.contains(" ")){
                //un mot de passe valide contient au moins une majuscule et un chiffre
                if (motPasse.matches(".*[A-Z]+.*") && motPasse.matches(".*[0-9]+.*")){
                    return true;
                } else {
                    Toast.makeText(InscriptionActivity.this, "Le mot de passe doit contenir un chiffre et une majuscule!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(InscriptionActivity.this, "Le mot de passe ne doit pas contenir d'espaces!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(InscriptionActivity.this, "Le mot de passe doit contenir au moins 8 caractères!", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}