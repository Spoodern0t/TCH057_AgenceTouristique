package com.example.agencetouristique7;
//

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HistoriqueReservationsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout bottomNavHome, bottomNavHistorique, bottomNavLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_reservations);

        bottomNavHome = findViewById(R.id.button_accueil_accueil);
        bottomNavHistorique = findViewById(R.id.button_historique_accueil);
        bottomNavLogout = findViewById(R.id.button_logout_accueil);

         bottomNavHome.setOnClickListener(this);
        bottomNavHistorique.setOnClickListener(this);
        bottomNavLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_accueil_accueil) {
             Intent accueilIntent = new Intent(this, AccueilActivity.class);
            startActivity(accueilIntent);
         } else if (id == R.id.button_historique_accueil) {
             Toast.makeText(this, "Historique des réservations", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.button_logout_accueil) {
             Intent logoutIntent = new Intent(this, MainActivity.class);
            startActivity(logoutIntent);
            finish();
        }
    }
}