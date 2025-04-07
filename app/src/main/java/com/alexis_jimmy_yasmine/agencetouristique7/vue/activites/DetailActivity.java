package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.AgenceViewModel;
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

        // Récupérer l'id du voyage cliqué
        Intent intent = getIntent();
        String voyageId = intent.getStringExtra("ID");

        Voyage voyage = modelView.getVoyageById(voyageId);


        modelView.getVoyages().observe(this, voyages -> {
            Voyage.Trip tripChoisi = (Voyage.Trip) spinDateDepart.getSelectedItem();
            tPlacesDisponible.setText(String.valueOf(tripChoisi.getStrNbPlacesDisponibles()));
        });

        // Charger l'interface selon l'id du voyage cliqué
        chargerVoyage(voyage);

        spinDateDepart.setOnItemSelectedListener(this);
        btnReserver.setOnClickListener(v ->
                handleReservation(voyage, spinDateDepart.getSelectedItem()));
    }


    private void chargerVoyage(Voyage voyage) {
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
        tDuree.setText(voyage.getDureeJours() + " jours");
        tPrix.setText(String.format("%.2f", voyage.getPrix())+" $");
        tActivites.setText(voyage.getActivitesIncluses());

        // Set up the Spinner
        if (!voyage.getTrips().isEmpty()) {
            TripsAdaptateur tripsAdaptateur = new TripsAdaptateur(DetailActivity.this, R.layout.layout_trips, voyage.getTrips());
            spinDateDepart.setAdapter(tripsAdaptateur);
        }
    }


    private void handleReservation(Voyage voyage, Object selectedItem) {

        Voyage.Trip tripChoisi = (Voyage.Trip) selectedItem;
        String nbPlacesString = editPlacesReservees.getText().toString();

        int nbPlacesReserveesInt;

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
        }else if (nbPlacesReserveesInt <= 0) {
            Toast.makeText(DetailActivity.this, "Veuillez entrer un nombre de places valide et supérieur à zéro.", Toast.LENGTH_LONG).show();
            return;
        }
        if (tripChoisi.estDateValide()) {
            Toast.makeText(DetailActivity.this, "La date choisie est déjà passée.", Toast.LENGTH_LONG).show();
            return;
        }

        long resultat = modelView.ajouterReservation(this, voyage, tripChoisi, nbPlacesReserveesInt);

        if (resultat > 0) {
            Toast.makeText(DetailActivity.this, "Réservation enregistrée avec succès!", Toast.LENGTH_SHORT).show();

            tripChoisi.diminuerNbPlaces(nbPlacesReserveesInt);

            modelView.updateTripAvailability(voyage);

            //After success, simply end.
            //setResult(RESULT_OK);
            //finish();

        } else if (resultat == -1) {
            Toast.makeText(DetailActivity.this, "Vous avez déjà réservé ce voyage.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(DetailActivity.this, "Erreur lors de l'enregistrement de la réservation.", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonHome) {
            setResult(RESULT_CANCELED);
            finish();
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