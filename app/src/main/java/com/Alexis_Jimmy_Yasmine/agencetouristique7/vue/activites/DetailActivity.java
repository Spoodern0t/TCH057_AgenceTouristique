package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.ModelView;
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

        // Composantes de la navbar
        btnHome = (ImageButton) findViewById(R.id.buttonHome);
        btnHistorique = (ImageButton) findViewById(R.id.buttonHistorique);
        btnLogout = (ImageButton) findViewById(R.id.buttonLogout);

        // ajouter un écouteur sur les boutons
        btnHome.setOnClickListener(this);
        btnHistorique.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        imageVoyage = (ImageView) findViewById(R.id.iv_voyage_detail);
        tNom = (TextView) findViewById(R.id.tv_nom_voyage_detail);
        tDescription = (TextView) findViewById(R.id.tv_description_voyage_detail);
        tDestination = (TextView) findViewById(R.id.tv_destination_detail);
        tDuree = (TextView) findViewById(R.id.tv_duree_detail);
        tPrix = (TextView) findViewById(R.id.tv_prix_detail);
        tActivites = (TextView) findViewById(R.id.tv_activites_detail);
        tPlacesDisponible = (TextView) findViewById(R.id.tv_places_disponibles_detail);
        spinDateDepart = (Spinner) findViewById(R.id.spinner_date_depart_detail);
        editPlacesReservees = (EditText) findViewById(R.id.editText_nb_places_detail);
        btnReserver = (Button) findViewById(R.id.button_reserver_detail);


        // Observer la liste des voyages
        modelView = new ViewModelProvider(this).get(ModelView.class);
        modelView.getVoyages().observe(this, new Observer<Voyage[]>() {
            @Override
            public void onChanged(Voyage[] voyages) {
                // Picasso set l'image
                Picasso.get().load(voyages[0].getImageUrl()).into(imageVoyage);
                tNom.setText(voyages[0].getNomVoyage());
                tDescription.setText(voyages[0].getDescription());
                tDestination.setText(voyages[0].getDestination());
                tDuree.setText(voyages[0].getStrDuree());
                tPrix.setText(voyages[0].getStrPrix());
                tActivites.setText(voyages[0].getActivitesIncluses());
                // FIXME: La vue ne devrait pas parler au Model "getTrips"
                spinDateDepart.setAdapter(new TripsAdaptateur(DetailActivity.this, R.layout.layout_trips, voyages[0].getTrips()));
            }
        });

        Intent intent = getIntent();

        // Lancer une nouvelle requete GET pour avoir seulement la destination choisie
        modelView = new ViewModelProvider(this).get(ModelView.class);
        modelView.chargerVoyages("/?id=" + intent.getIntExtra("ID", 0));

        spinDateDepart.setOnItemSelectedListener(this);

        btnReserver.setOnClickListener(v -> {
            // TODO: POST SQLite local

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
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

        Object tripChoisi = spinDateDepart.getItemAtPosition(i);
        tPlacesDisponible.setText(((Voyage.Trip) tripChoisi).getStrNbPlacesDisponibles());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Ne rien faire
    }
}