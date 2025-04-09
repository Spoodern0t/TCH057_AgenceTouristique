package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.ReservationViewModel;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.VoyageViewModel;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.ReservationsAdaptateur;

public class HistoriqueReservationsActivity extends AppCompatActivity implements View.OnClickListener {

    private VoyageViewModel voyageViewModel;
    private ReservationViewModel reservationViewModel;
    private ListView listViewReservationsHistorique;
    private ImageButton bottomNavHome, bottomNavHistorique, bottomNavLogout;
    private ReservationsAdaptateur adaptateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_reservations);

        listViewReservationsHistorique = findViewById(R.id.listView_reservations_historique);

        bottomNavHome = findViewById(R.id.buttonHome);
        bottomNavHistorique = findViewById(R.id.buttonHistorique);
        bottomNavLogout = findViewById(R.id.buttonLogout);

        bottomNavHome.setOnClickListener(this);
        bottomNavHistorique.setOnClickListener(this);
        bottomNavLogout.setOnClickListener(this);

        voyageViewModel = new ViewModelProvider(this).get(VoyageViewModel.class);
        voyageViewModel.getVoyages().observe(this, voyages -> {

        });

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        reservationViewModel.getReservations().observe(this, reservations -> {
            if (reservations.isEmpty())
                Toast.makeText(this, "Aucune réservation enregistrée.", Toast.LENGTH_SHORT).show();

            adaptateur = new ReservationsAdaptateur(this, R.layout.layout_reservation_historique_item, reservations, this);
            listViewReservationsHistorique.setAdapter(adaptateur);
        });
        reservationViewModel.getReservation().observe(this, reservation -> {
            Toast.makeText(this, "Réservation annulée.", Toast.LENGTH_SHORT).show();
        });
        reservationViewModel.getErreur().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        reservationViewModel.setConnexionBD(this);

        reservationViewModel.chargerReservations();
    }

    public void annulerReservation(Reservation reservation) {
        Voyage.Trip trip = voyageViewModel.getTripOfVoyage(reservation.getVoyageId(), reservation.getDateVoyage());
        trip.augmenterNbPlaces(reservation.getNbPersonnes());
        reservationViewModel.annulerReservation(reservation);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonHome) {
            Intent accueilIntent = new Intent(this, AccueilActivity.class);
            startActivity(accueilIntent);
        } else if (id == R.id.buttonHistorique) {
            Toast.makeText(this, "Historique des réservations", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.buttonLogout) {
            Intent logoutIntent = new Intent(this, MainActivity.class);
            startActivity(logoutIntent);
            finish();
        }
    }
}