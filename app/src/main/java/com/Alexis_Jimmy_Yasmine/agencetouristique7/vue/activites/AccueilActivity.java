package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.ModelView;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.VoyagesAdaptateur;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AccueilActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, TextWatcher {

    private ModelView modelView;
    private EditText searchEditText;
    private Spinner spinBudgetAccueil, spinTypeAccueil;
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
        voyagesListView = (ListView) findViewById(R.id.voyagesListView);

        searchEditText.addTextChangedListener(this);
        spinBudgetAccueil.setOnItemSelectedListener(this);
        spinTypeAccueil.setOnItemSelectedListener(this);

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

        }
    }

    // Gestion du filtre
    public void filtrerVoyages() {
        // contruit la requete GET
        String nomLike = searchEditText.getText().toString();
        String budget = spinBudgetAccueil.getSelectedItem().toString();
        String type = spinTypeAccueil.getSelectedItem().toString();

        String url = "/?";
        int[] budgetRange = null;
        Pattern regex = null;

        // Barre de recherche
        if (!nomLike.isEmpty()) {
            regex = Pattern.compile(nomLike, Pattern.CASE_INSENSITIVE);
        }
        // Budget
        if (!budget.equalsIgnoreCase("Tous les budgets")) {
            switch (budget) {
                case "Moins de 300$": budgetRange = new int[]{0, 300}; break;
                case "300$ - 600$": budgetRange = new int[]{300, 600}; break;
                case "Plus de 600$": budgetRange = new int[]{600, 2147483647}; break;
            }
        }
        // Type
        if (!type.equalsIgnoreCase("Tous les types")) {
            url += "type_de_voyage=" + type + "&";
        }
        url = url.substring(0, url.length() - 1);

        modelView.chargerVoyages(url, budgetRange, regex);
    }

    // Envoyer vers l'activité de détail
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        Voyage voyageClique = (Voyage) parent.getAdapter().getItem(i);
        intent.putExtra("ID", voyageClique.getId());
        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filtrerVoyages();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        filtrerVoyages();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Ne rien faire
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Ne rien faire
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Ne rien faire
    }
}