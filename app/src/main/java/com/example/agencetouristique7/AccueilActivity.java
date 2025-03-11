package com.example.agencetouristique7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AccueilActivity extends AppCompatActivity implements View.OnClickListener {
//
     private EditText searchEditText;
    private Spinner spinBudgetAccueil, spinTypeAccueil;
    private ListView voyagesListView;
    private LinearLayout bottomNavHome, bottomNavHistorique, bottomNavLogout;

    private List<Voyage> voyageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        searchEditText = findViewById(R.id.searchEditText);
        spinBudgetAccueil = findViewById(R.id.budgetSpinner);
        spinTypeAccueil = findViewById(R.id.typeSpinner);
        voyagesListView = findViewById(R.id.voyagesListView);
        bottomNavHome = findViewById(R.id.bottomNavHome);
        bottomNavHistorique = findViewById(R.id.bottomNavHistorique);
        bottomNavLogout = findViewById(R.id.bottomNavLogout);

         bottomNavHome.setOnClickListener(this);
        bottomNavHistorique.setOnClickListener(this);
        bottomNavLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.bottomNavHome) {
             Toast.makeText(this, "Accueil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.bottomNavHistorique) {
             Intent historiqueIntent = new Intent(this, HistoriqueReservationsActivity.class);
            startActivity(historiqueIntent);
        } else if (id == R.id.bottomNavLogout) {
             Intent logoutIntent = new Intent(this, MainActivity.class);
            startActivity(logoutIntent);
            finish();
        }
    }
}