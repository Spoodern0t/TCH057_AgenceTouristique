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
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.AgenceViewModel;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ReservationDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.ReservationsAdaptateur;

import java.util.List;

public class HistoriqueReservationsActivity extends AppCompatActivity implements View.OnClickListener {

    private AgenceViewModel modelView;
    private ListView listViewReservationsHistorique;
    private ReservationDao reservationDao;
    private ImageButton bottomNavHome, bottomNavHistorique, bottomNavLogout;
    private ReservationsAdaptateur adaptateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_reservations);

        modelView = new ViewModelProvider(this).get(AgenceViewModel.class);

        listViewReservationsHistorique = findViewById(R.id.listView_reservations_historique);
        reservationDao = new ReservationDao(this);

        bottomNavHome = findViewById(R.id.buttonHome);
        bottomNavHistorique = findViewById(R.id.buttonHistorique);
        bottomNavLogout = findViewById(R.id.buttonLogout);

        bottomNavHome.setOnClickListener(this);
        bottomNavHistorique.setOnClickListener(this);
        bottomNavLogout.setOnClickListener(this);

        afficherHistoriqueReservations();
    }

    private void afficherHistoriqueReservations() {

        List<Reservation> reservations = reservationDao.getAllReservations();

        if (reservations.isEmpty()) {
            Toast.makeText(this, "Aucune réservation enregistrée.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HistoriqueReservationsActivity.this, AccueilActivity.class);
            startActivity(intent);
        } else {
            adaptateur = new ReservationsAdaptateur(this, R.layout.layout_reservation_historique_item, reservations, this);
            listViewReservationsHistorique.setAdapter(adaptateur);
        }
    }

    public void deleteReservation(Reservation reservationToDelete) {
        ReservationDao reservationDao = new ReservationDao(this);
        boolean succes = reservationDao.supprimerReservation(reservationToDelete.getId());

        String voyageId = reservationToDelete.getVoyageId();
        String voyageDate = reservationToDelete.getDateVoyage();
        int nbPlacesCancelees = reservationToDelete.getNbPersonnes();

        Voyage voyage = modelView.getVoyageById(voyageId);

        for (Voyage.Trip trip: voyage.getTrips()) {
            if (trip.getDate().equals(voyageDate)) {
                trip.augmenterNbPlaces(nbPlacesCancelees);
                break;
            }
        }

        modelView.updateTripAvailability(voyage);

        if (succes) {
            Toast.makeText(this, "Réservation supprimée!", Toast.LENGTH_SHORT).show();
            afficherHistoriqueReservations();
        } else {
            Toast.makeText(this, "Erreur lors de la suppression de la réservation.", Toast.LENGTH_LONG).show();
        }
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