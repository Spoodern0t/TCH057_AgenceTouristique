package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;
//

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.ModelView;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;

import org.json.JSONException;

public class InscriptionActivity extends AppCompatActivity {

    private ModelView modelView;
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
            } else {

                int age = Integer.parseInt(ageString);
                String adresse = address + ", " + city + ", " + province;

                Client client = new Client(0, lastName, firstName, email, password, age, phone, adresse);

                modelView = new ViewModelProvider(this).get(ModelView.class);

                try {

                    modelView.postClient(client);
                    setResult(RESULT_OK);
                    finish();

                } catch (JSONException e) {

                    Toast.makeText(InscriptionActivity.this, "Une erreur s'est produite !", Toast.LENGTH_LONG).show();
                }
            }

            /*
            if (firstName.isEmpty() ) {
                Toast.makeText(InscriptionActivity.this, "Please fill in first name...", Toast.LENGTH_LONG).show();
                return;
            }

            if ( lastName.isEmpty() ) {
                Toast.makeText(InscriptionActivity.this, "Please fill in last name...", Toast.LENGTH_LONG).show();
                return;
            }

            if ( ageString.isEmpty()  ) {
                Toast.makeText(InscriptionActivity.this, "Please fill in age...", Toast.LENGTH_LONG).show();
                return;
            }

            if ( email.isEmpty()  ) {
                Toast.makeText(InscriptionActivity.this, "Please fill in email...", Toast.LENGTH_LONG).show();
                return;
            }

            if  (password.isEmpty()) {
                Toast.makeText(InscriptionActivity.this, "Please fill in password...", Toast.LENGTH_LONG).show();
                return;
            }


            int ageInt;
            try {
                ageInt = Integer.parseInt(ageString);
            } catch (NumberFormatException e) {
                Toast.makeText(InscriptionActivity.this, "Invalid Age. Please enter a number...", Toast.LENGTH_LONG).show();
                return;
            }
              Toast.makeText(InscriptionActivity.this, "Account created successfully! :)", Toast.LENGTH_SHORT).show();
            finish();
             */
        });

        btnAnnuler.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}