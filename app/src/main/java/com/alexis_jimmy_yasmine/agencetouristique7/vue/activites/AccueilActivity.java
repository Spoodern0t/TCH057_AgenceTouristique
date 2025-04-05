package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.AgenceViewModel;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.VoyagesAdaptateur;

import java.util.List;
import java.util.regex.Pattern;

public class AccueilActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, TextWatcher {

    private AgenceViewModel modelView;
    private EditText searchEditText;
    private Spinner spinBudgetAccueil, spinTypeAccueil;
    private ListView voyagesListView;
    private ImageButton btnHome, btnHistorique, btnLogout, btnFilter;
    private PopupWindow popupWindow;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Composantes de la navbar
        btnHome = (ImageButton) findViewById(R.id.buttonHome);
        btnHistorique = (ImageButton) findViewById(R.id.buttonHistorique);
        btnLogout = (ImageButton) findViewById(R.id.buttonLogout);
        btnFilter = (ImageButton) findViewById(R.id.buttonFilter);

        // ajouter un écouteur sur les boutons
        btnHome.setOnClickListener(this);
        btnHistorique.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnFilter.setOnClickListener(this);

        // récupérer les composantes de la vue
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        spinBudgetAccueil = (Spinner) findViewById(R.id.budgetSpinner);
        spinTypeAccueil = (Spinner) findViewById(R.id.typeSpinner);
        voyagesListView = (ListView) findViewById(R.id.voyagesListView);

        searchEditText.addTextChangedListener(this);
        spinBudgetAccueil.setOnItemSelectedListener(this);
        spinTypeAccueil.setOnItemSelectedListener(this);

        voyagesListView.setOnItemClickListener(this);

        // Aucun resultat attendu
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Aucun resultat attendu
                }
        );


        // Observer la liste des voyages
        modelView = new ViewModelProvider(this).get(AgenceViewModel.class);
        modelView.getVoyages().observe(this, voyages -> {
            Voyage[] tabVoyages = new Voyage[voyages.size()];
            voyages.toArray(tabVoyages);

            voyagesListView.setAdapter(new VoyagesAdaptateur(AccueilActivity.this, R.layout.layout_voyage, tabVoyages));
        });

        modelView.chargerVoyages(null, null, null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonHome) {
            Toast.makeText(this, "Accueil", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.buttonHistorique) {
            Intent intent = new Intent(this, HistoriqueReservationsActivity.class);
            launcher.launch(intent);

        } else if (id == R.id.buttonLogout) {
            Intent intent = new Intent(this, MainActivity.class);
            launcher.launch(intent);

        }  else if (id == R.id.buttonFilter) {
            afficherPopupFiltre(view);

        }
    }

    private void afficherPopupFiltre(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_filtre, null);

        popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button popupFilterButton = popupView.findViewById(R.id.popupFilterButton);

        popupFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appliquerPopupFiltres();
                popupWindow.dismiss();
            }
        });
    }


    // Appliquer les filtres depuis le popup
    private void appliquerPopupFiltres() {
        if (popupWindow != null && popupWindow.isShowing()) {
            View popupView = popupWindow.getContentView();

            Spinner popupTypeSpinner = popupView.findViewById(R.id.popupTypeSpinner);
            Spinner popupBudgetSlider = popupView.findViewById(R.id.popupBudgetSlider);
            Spinner popupDestinationSpinner = popupView.findViewById(R.id.popupDestinationSpinner);
            Spinner popupDateSpinner = popupView.findViewById(R.id.popupDateSpinner);


            String filtreType = popupTypeSpinner.getSelectedItem().toString();
            String filtreBudget = popupBudgetSlider.getSelectedItem().toString();
            String filtreDestination = popupDestinationSpinner.getSelectedItem().toString();
            String filtreDate = popupDateSpinner.getSelectedItem().toString();

            String url = "/?";
            int[] rangeeBudget = obtenirRangeeBudgetDepuisTexte(filtreBudget);
            Pattern regex = null;
            /*
            String nomRecherche = searchEditText.getText().toString();
            if (!nomRecherche.isEmpty()) {
                regex = Pattern.compile(nomRecherche, Pattern.CASE_INSENSITIVE);
            }
             */


            if (filtreType.equalsIgnoreCase("Tous les types")) {
                filtreType = null;
            }
            if (filtreDestination.equalsIgnoreCase("Toutes les destinations")) {
                filtreDestination = null;
            }

            /*
            if (url.endsWith("&") && url.length() > 2) {
                url = url.substring(0, url.length() - 1);
            } else if (url.equals("/?")) {
                url = "/";
            }
             */

            modelView.chargerVoyages(filtreType, rangeeBudget, filtreDestination);
        }
    }

    private int[] obtenirRangeeBudgetDepuisTexte(String texteBudget) {
        switch (texteBudget) {
            case "Moins de 300$": return new int[]{0, 300};
            case "300$ - 600$": return new int[]{300, 600};
            case "Plus de 600$": return new int[]{600, Integer.MAX_VALUE};
            default: return null;
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
                case "Plus de 600$": budgetRange = new int[]{600, Integer.MAX_VALUE}; break;
            }
        }
        // Type
        if (!type.equalsIgnoreCase("Tous les types")) {
            url += "type_de_voyage=" + type + "&";
        }
        url = url.substring(0, url.length() - 1);

        modelView.regexVoyages(regex);
    }

    // Afficher un message d'erreur
    public void afficherMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Envoyer vers l'activité de détail du voyage cliqué
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

        Intent intent = new Intent(this, DetailActivity.class);
        Voyage voyageClique = (Voyage) parent.getAdapter().getItem(i);
        intent.putExtra("ID", voyageClique.getId());
        launcher.launch(intent);
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