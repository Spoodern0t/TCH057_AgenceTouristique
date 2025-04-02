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
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.AgenceViewModel;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ReservationDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.TripsAdaptateur;
import com.squareup.picasso.Picasso;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private AgenceViewModel modelView;
    private TextView tNom, tDescription, tDestination, tDuree, tPrix, tActivites, tPlacesDisponible;
    private Spinner spinDateDepart;
    private EditText editPlacesReservees;
    private Button btnReserver;
    private ImageButton btn_Home, btn_Historique, btn_Logout;
    private LinearLayout imageGalleryLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_voyage);

        modelView = new ViewModelProvider(this).get(AgenceViewModel.class);

        btn_Home = findViewById(R.id.buttonHome);
        btn_Historique = findViewById(R.id.buttonHistorique);
        btn_Logout = findViewById(R.id.buttonLogout);

        btn_Home.setOnClickListener(this);
        btn_Historique.setOnClickListener(this);
        btn_Logout.setOnClickListener(this);

        imageGalleryLinearLayout = findViewById(R.id.linearLayoutImageGallery);
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

        /*
        // Load the voyage data
        Intent intent = getIntent();
        modelView.chargerVoyages("/?id=" + intent.getIntExtra("ID", 0));


        modelView.getVoyages().observe(this, new Observer<Voyage[]>() {
            @Override
            public void onChanged(Voyage[] voyages) {
                if (voyages == null || voyages.length == 0) {
                    Log.w("DetailActivity", "Voyages array is null or empty.");
                    return;
                }

                Voyage voyage = voyages[0];

                // Image Gallery
                imageGalleryLinearLayout.removeAllViews();
                List<String> imageUrls = voyage.getImageUrls();
                if (imageUrls != null) {
                    for (String imageUrl : imageUrls) {
                        ImageView imageView = new ImageView(DetailActivity.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                400,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        layoutParams.setMarginEnd(20);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Picasso.get().load(imageUrl).into(imageView);
                        imageGalleryLinearLayout.addView(imageView);
                    }
                }

                // Set text views
                tNom.setText(voyage.getNomVoyage());
                tDescription.setText(voyage.getDescription());
                tDestination.setText(voyage.getDestination());
                tDuree.setText(String.valueOf(voyage.getDureeJours()));
                tPrix.setText(String.valueOf(voyage.getPrix()));
                tActivites.setText(voyage.getActivitesIncluses());

                // Set up the Spinner
                TripsAdaptateur tripsAdaptateur = new TripsAdaptateur(DetailActivity.this, R.layout.layout_trips, voyage.getTrips());
                spinDateDepart.setAdapter(tripsAdaptateur);

                // Update places available
                updatePlacesAvailable(voyage);
            }

        });

        spinDateDepart.setOnItemSelectedListener(this);
        btnReserver.setOnClickListener(v -> handleReservation(modelView.getVoyages().getValue()[0], spinDateDepart.getSelectedItem()));
        */
    }

    private void updatePlacesAvailable(Voyage voyage) {
        if (voyage.getTrips() != null && voyage.getTrips().length > 0) {
            spinDateDepart.setSelection(0, false);
            Voyage.Trip premierTrip = (Voyage.Trip) spinDateDepart.getSelectedItem();
            tPlacesDisponible.setText(premierTrip.getStrNbPlacesDisponibles());

        } else {
            Log.w("DetailActivity", "Voyage or trips null or empty in onChanged");
            tPlacesDisponible.setText("No trips available");
        }
    }

    private void handleReservation(Voyage voyage, Object selectedItem) {
        Voyage.Trip tripChoisi = (Voyage.Trip) selectedItem;
        String nbPlacesString = editPlacesReservees.getText().toString();
        int nbPlacesReserveesInt = 0;

        if (nbPlacesString.isEmpty()) {
            Toast.makeText(DetailActivity.this, "Veuillez entrer le nombre de places.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            nbPlacesReserveesInt = Integer.parseInt(nbPlacesString);
        } catch (NumberFormatException e) {
            Toast.makeText(DetailActivity.this, "Nombre de places invalide.", Toast.LENGTH_SHORT).show();
            return;
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
        reservation.setDestination(voyage.getDestination());
        reservation.setDateVoyage(tripChoisi.getDate());
        reservation.setMontantPaye(voyage.getPrix() * nbPlacesReserveesInt);
        reservation.setStatut("Confirmée");
        reservation.setNbPersonnes(nbPlacesReserveesInt);
        reservation.setImageUrl(voyage.getImageUrl());

        ReservationDao reservationDao = new ReservationDao(DetailActivity.this);
        long nouvelleReservationId = reservationDao.ajouterReservation(reservation);

        if (nouvelleReservationId > 0) {
            Toast.makeText(DetailActivity.this, "Réservation enregistrée avec succès!", Toast.LENGTH_SHORT).show();

            int nouveauNbPlacesDisponibles = tripChoisi.getNbPlacesDisponibles() - nbPlacesReserveesInt;
            //modelView.updateTripAvailability(voyage.getId(), tripChoisi.getDate(), nouveauNbPlacesDisponibles);

            //After success, simply end.
            setResult(RESULT_OK);
            finish();

        } else {
            Toast.makeText(DetailActivity.this, "Erreur lors de l'enregistrement de la réservation.", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonHome) {
            startActivity(new Intent(this, AccueilActivity.class));
        } else if (id == R.id.buttonHistorique) {
            startActivity(new Intent(this, HistoriqueReservationsActivity.class));
        } else if (id == R.id.buttonLogout) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Voyage.Trip tripChoisi = (Voyage.Trip) parent.getItemAtPosition(position);
        tPlacesDisponible.setText(tripChoisi.getStrNbPlacesDisponibles());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }


}