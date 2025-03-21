package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ReservationDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.ReservationsAdaptateur;

import java.util.List;

public class HistoriqueReservationsActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewReservationsHistorique;
    private ReservationDao reservationDao;
    private LinearLayout bottomNavHome, bottomNavHistorique, bottomNavLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_reservations);

        listViewReservationsHistorique = findViewById(R.id.listView_reservations_historique);
        reservationDao = new ReservationDao(this);

        bottomNavHome = findViewById(R.id.button_accueil_accueil);
        bottomNavHistorique = findViewById(R.id.button_historique_accueil);
        bottomNavLogout = findViewById(R.id.button_logout_accueil);

        bottomNavHome.setOnClickListener(this);
        bottomNavHistorique.setOnClickListener(this);
        bottomNavLogout.setOnClickListener(this);

        afficherHistoriqueReservations();
    }

    private void afficherHistoriqueReservations() {
        ReservationDao reservationDao = new ReservationDao(this);
        List<Reservation> reservations = reservationDao.getAllReservations();

        if (reservations.isEmpty()) {
            Toast.makeText(this, "Aucune réservation enregistrée.", Toast.LENGTH_SHORT).show();
        } else {
            ReservationsAdaptateur adaptateur = new ReservationsAdaptateur(this, R.layout.layout_reservation_historique_item, reservations);
            listViewReservationsHistorique.setAdapter(adaptateur);
        }
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