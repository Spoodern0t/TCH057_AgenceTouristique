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
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.VoyagesAdaptateur;

public class AccueilActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ModelView modelView;
    private EditText searchEditText;
    private Spinner spinBudgetAccueil, spinTypeAccueil;
    private Button btnRechercher;
    private ListView voyagesListView;
    private ImageButton btnHome, btnHistorique, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Composantes de la navbar
        btnHome = (ImageButton) findViewById(R.id.buttonHome);
        btnHistorique = (ImageButton) findViewById(R.id.buttonHistorique);
        btnLogout = (ImageButton) findViewById(R.id.buttonLogout);

        // ajouter un écouteur sur les boutons
        btnHome.setOnClickListener(this);
        btnHistorique.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        // récupérer les composantes de la vue
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        spinBudgetAccueil = (Spinner) findViewById(R.id.budgetSpinner);
        spinTypeAccueil = (Spinner) findViewById(R.id.typeSpinner);
        btnRechercher = (Button) findViewById(R.id.buttonRechercher);
        voyagesListView = (ListView) findViewById(R.id.voyagesListView);


        btnRechercher.setOnClickListener(this);

        voyagesListView.setOnItemClickListener(this);

        // Observer la liste des voyages
        modelView = new ViewModelProvider(this).get(ModelView.class);
        modelView.getVoyages().observe(this, new Observer<Voyage[]>() {
            @Override
            public void onChanged(Voyage[] voyages) {
                voyagesListView.setAdapter(new VoyagesAdaptateur(AccueilActivity.this, R.layout.layout_voyage, voyages));
            }
        });
        modelView.chargerVoyages("/");
    }

    // Recharger les destinations au retour de l'activité
    @Override
    protected void onResume() {
        super.onResume();

        modelView.chargerVoyages("/");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonHome) {
             Toast.makeText(this, "Accueil", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.buttonHistorique) {
             Intent intent = new Intent(this, HistoriqueReservationsActivity.class);
            startActivity(intent);

        } else if (id == R.id.buttonLogout) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.buttonRechercher) {
            // contruit la requete GET
            String url = "/?";
            if (!searchEditText.getText().toString().isEmpty()) {
                // TODO: REGEX
            }
            if (!spinBudgetAccueil.getSelectedItem().toString().equalsIgnoreCase("Tous les budgets")) {
                url += "prix=" + spinBudgetAccueil.getSelectedItem().toString() + "&";
            }
            if (!spinTypeAccueil.getSelectedItem().toString().equalsIgnoreCase("Tous les types")) {
                url += "type_de_voyage=" + spinTypeAccueil.getSelectedItem().toString() + "&";
            }
            url = url.substring(0, url.length() - 1);

            modelView.chargerVoyages(url);
        }
    }

    // Envoyer vers l'activité de détail
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        Voyage voyageClique = (Voyage) parent.getAdapter().getItem(i);
        intent.putExtra("ID", voyageClique.getId());
        /*
        intent.putExtra("NOM", voyageClique.getNomVoyage());
        intent.putExtra("DESCRIPTION", voyageClique.getDescription());
        intent.putExtra("PRIX", voyageClique.getPrix());
        intent.putExtra("DESTINATION", voyageClique.getDestination());
        intent.putExtra("IMAGE", voyageClique.getImageUrl());
        intent.putExtra("DUREE", voyageClique.getDureeJours());
        intent.putExtra("TYPE", voyageClique.getTypeDeVoyage());
        intent.putExtra("ACTIVITES", voyageClique.getActivitesIncluses()); */
        startActivity(intent);
    }
}