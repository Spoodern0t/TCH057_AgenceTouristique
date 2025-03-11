package com.example.agencetouristique7;
//

 import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
 import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InscriptionActivity extends AppCompatActivity {
    private EditText editTextFirstNameInscription;
    private EditText editTextLastNameInscription;
    private EditText editTextAgeInscription;
    private EditText editTextPhoneInscription;
    private EditText editTextAddressInscription;
    private EditText editTextCityInscription;
    private EditText editTextProvinceInscription;
    private EditText editTextEmailRegisterInscription;
    private EditText editTextPasswordRegisterInscription;
    private Button buttonSignUpInscription;
    private Button buttonReturnLoginInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        editTextFirstNameInscription = findViewById(R.id.editTextFirstNameInscription);
        editTextLastNameInscription = findViewById(R.id.editTextLastNameInscription);
        editTextAgeInscription = findViewById(R.id.editTextAgeInscription);
        editTextPhoneInscription = findViewById(R.id.editTextPhoneInscription);
        editTextAddressInscription = findViewById(R.id.editTextAddressInscription);
        editTextCityInscription = findViewById(R.id.editTextCityInscription);
        editTextProvinceInscription = findViewById(R.id.editTextProvinceInscription);
        editTextEmailRegisterInscription = findViewById(R.id.editTextEmailRegisterInscription);
        editTextPasswordRegisterInscription = findViewById(R.id.editTextPasswordRegisterInscription);
        buttonSignUpInscription = findViewById(R.id.buttonSignUpInscription);
        buttonReturnLoginInscription = findViewById(R.id.buttonReturnLoginInscription);


        buttonSignUpInscription.setOnClickListener(v -> {
            String firstName = editTextFirstNameInscription.getText().toString();
            String lastName = editTextLastNameInscription.getText().toString();
            String ageString = editTextAgeInscription.getText().toString();
            String phone = editTextPhoneInscription.getText().toString();
            String address = editTextAddressInscription.getText().toString();
            String city = editTextCityInscription.getText().toString();
            String province = editTextProvinceInscription.getText().toString();
            String email = editTextEmailRegisterInscription.getText().toString();
            String password = editTextPasswordRegisterInscription.getText().toString();

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
        });
        buttonReturnLoginInscription.setOnClickListener(v -> {
            finish();
        });
    }
}