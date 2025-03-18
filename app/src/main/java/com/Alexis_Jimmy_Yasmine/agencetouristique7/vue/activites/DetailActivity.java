package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.ResultSet;

import javax.xml.transform.Result;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

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


        Intent intent = getIntent();
        intent.getStringExtra("ID");
        intent.getStringExtra("TYPE");

        /* TODO: Image from URL
        imageVoyage.setImageResource(intent.getStringExtra("IMAGE")); */
        tNom.setText(intent.getStringExtra("NOM"));
        tDescription.setText(intent.getStringExtra("DESCRIPTION"));
        tDestination.setText("Destination : " + intent.getStringExtra("DESTINATION"));
        tDuree.setText("Durée : " + intent.getIntExtra("DUREE", 0));
        tPrix.setText("Prix par personne : " + intent.getIntExtra("PRIX", 0));
        tActivites.setText("Activités Incluses : " + intent.getStringExtra("ACTIVITES"));
        /* TODO: Trips from date de départ
        tPlacesDisponible.setText(); */


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
}