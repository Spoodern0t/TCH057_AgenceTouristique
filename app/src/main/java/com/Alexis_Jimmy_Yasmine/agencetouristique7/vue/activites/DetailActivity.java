package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.ModelView;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ReservationDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.TripsAdaptateur;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ModelView modelView;
    private ImageView imageVoyage;
    private TextView tNom, tDescription, tDestination, tDuree, tPrix, tActivites, tPlacesDisponible;
    Spinner spinDateDepart;
    EditText editPlacesReservees;
    Button btnReserver;
    private ImageButton btnHome, btnHistorique, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_voyage);

        // Observer la liste des voyages
        modelView = new ViewModelProvider(this).get(ModelView.class);

        // Composantes de la navbar
        btnHome = findViewById(R.id.buttonHome);
        btnHistorique = findViewById(R.id.buttonHistorique);
        btnLogout = findViewById(R.id.buttonLogout);

        // ajouter un écouteur sur les boutons
        btnHome.setOnClickListener(this);
        btnHistorique.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        imageVoyage = findViewById(R.id.iv_voyage_detail);
        tNom = findViewById(R.id.tv_nom_voyage_detail);
        tDescription = findViewById(R.id.tv_description_voyage_detail);
        tDestination = findViewById(R.id.tv_destination_detail);
        tDuree = findViewById(R.id.tv_duree_detail);
        tPrix = findViewById(R.id.tv_prix_detail);
        tActivites = findViewById(R.id.tv_activites_detail);
        tPlacesDisponible = findViewById(R.id.tv_places_disponibles_detail);
        spinDateDepart = findViewById(R.id.spinner_date_depart_detail);
        editPlacesReservees = findViewById(R.id.editText_nb_places_detail);
        btnReserver = findViewById(R.id.button_reserver_detail);

        modelView.getVoyages().observe(this, voyages -> {
            Log.d("DetailActivity", "onChanged appelé, taille du tableau voyages: " + voyages.length); // Log existant

            // Picasso set l'image
            Picasso.get().load(voyages[0].getImageUrl()).into(imageVoyage);
            tNom.setText(voyages[0].getNomVoyage());
            tDescription.setText(voyages[0].getDescription());
            tDestination.setText(voyages[0].getDestination());
            tDuree.setText(voyages[0].getStrDuree());
            tPrix.setText(voyages[0].getStrPrix());
            tActivites.setText(voyages[0].getActivitesIncluses());

            // Mettre à jour le Spinner des dates de départ
            TripsAdaptateur tripsAdaptateur = new TripsAdaptateur(DetailActivity.this, R.layout.layout_trips, voyages[0].getTrips());
            spinDateDepart.setAdapter(tripsAdaptateur);

            // Mettre à jour le TextView des places disponibles
            if (voyages[0].getTrips() != null && voyages[0].getTrips().length > 0) {
                spinDateDepart.setSelection(0);
                Voyage.Trip premierTrip = (Voyage.Trip) spinDateDepart.getSelectedItem();

                String nbPlacesDisponiblesAvantSetText = premierTrip.getStrNbPlacesDisponibles();
                Log.d("DetailActivity", "Nombre de places disponibles (premier trip avant setText): " + nbPlacesDisponiblesAvantSetText);
                tPlacesDisponible.setText(premierTrip.getStrNbPlacesDisponibles());
                String nbPlacesDisponiblesApresSetText = premierTrip.getStrNbPlacesDisponibles();
                Log.d("DetailActivity", "Nombre de places disponibles (premier trip après setText): " + tPlacesDisponible.getText());
            } else {
                Log.w("DetailActivity", "Voyage ou trips null ou vide dans onChanged");
            }
        });

        Intent intent = getIntent();

         modelView = new ViewModelProvider(this).get(ModelView.class);
        modelView.chargerVoyages("/?id=" + intent.getIntExtra("ID", 0));

        spinDateDepart.setOnItemSelectedListener(this);

        btnReserver.setOnClickListener(v -> {
            // Récupérer les informations nécessaires pour la réservation
            Voyage voyage = modelView.getVoyages().getValue()[0];
            Voyage.Trip tripChoisi = (Voyage.Trip) spinDateDepart.getSelectedItem();
            String nbPlacesString = editPlacesReservees.getText().toString();
            int nbPlacesReserveesInt = 0;

            // Vérifier si le nombre de places est valide
            if (!nbPlacesString.isEmpty()) {
                nbPlacesReserveesInt = Integer.parseInt(nbPlacesString);
            }else{
                Toast.makeText(DetailActivity.this, "Veuillez entrer le nombre de places.", Toast.LENGTH_SHORT).show();
            }

             if (nbPlacesReserveesInt > tripChoisi.getNbPlacesDisponibles()) {
                Toast.makeText(DetailActivity.this, "Nombre de places souhaitées supérieur aux places disponibles.", Toast.LENGTH_LONG).show();
                return;
            }
            if (nbPlacesReserveesInt <= 0) {
                Toast.makeText(DetailActivity.this, "Veuillez entrer un nombre de places valide et supérieur à zéro.", Toast.LENGTH_LONG).show();
                return;
            }

            Reservation reservation = new Reservation();
            reservation.setDestination(voyage.getDestination().replace("Destination : ", ""));
            reservation.setDateVoyage(tripChoisi.getDate());
            reservation.setMontantPaye(voyage.getPrix() * nbPlacesReserveesInt);
            reservation.setStatut("Confirmée");
            reservation.setNbPersonnes(nbPlacesReserveesInt);

            // Ajouter la réservation à la base de données SQLite locale
            ReservationDao reservationDao = new ReservationDao(DetailActivity.this);
            long nouvelleReservationId = reservationDao.ajouterReservation(reservation);

            if (nouvelleReservationId > 0) {
                Toast.makeText(DetailActivity.this, "Réservation enregistrée avec succès!", Toast.LENGTH_SHORT).show();

                // Mettre à jour le nombre de places disponibles sur le serveur JSON

                // Calculer le nouveau nombre de places disponibles après la réservation
                int nouveauNbPlacesDisponibles = tripChoisi.getNbPlacesDisponibles() - nbPlacesReserveesInt;

                modelView.updateTripAvailability(voyage.getId(), tripChoisi.getDate(), nouveauNbPlacesDisponibles);

            } else {
                Toast.makeText(DetailActivity.this, "Erreur lors de l'enregistrement de la réservation.", Toast.LENGTH_LONG).show();
            }

            modelView.chargerVoyages("/?id=" + voyage.getId());

            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonHome) {
            Intent intent = new Intent(this, AccueilActivity.class);
            startActivity(intent);

        } else if (id == R.id.buttonHistorique) {
            Intent intent = new Intent(this, HistoriqueReservationsActivity.class);
            startActivity(intent);

        } else if (id == R.id.buttonLogout) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Voyage.Trip tripChoisi = (Voyage.Trip) parent.getItemAtPosition(position);
        tPlacesDisponible.setText(tripChoisi.getStrNbPlacesDisponibles());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Ne rien faire
    }
}