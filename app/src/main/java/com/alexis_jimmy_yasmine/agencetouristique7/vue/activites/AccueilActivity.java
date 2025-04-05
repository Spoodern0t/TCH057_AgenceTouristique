package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.AgenceViewModel;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs.VoyagesAdaptateur;
import com.google.android.material.slider.RangeSlider;

import java.time.LocalDate;
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

    // Composant du popup
    Spinner popupTypeSpinner, popupDestinationSpinner;
    RangeSlider popupBudgetSlider;
    SwitchCompat popupDateSwitch;
    DatePicker popupDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Composantes de la navbar
        btnHome = findViewById(R.id.buttonHome);
        btnHistorique = findViewById(R.id.buttonHistorique);
        btnLogout = findViewById(R.id.buttonLogout);
        btnFilter = findViewById(R.id.buttonFilter);

        // ajouter un écouteur sur les boutons
        btnHome.setOnClickListener(this);
        btnHistorique.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnFilter.setOnClickListener(this);

        // récupérer les composantes de la vue
        searchEditText = findViewById(R.id.searchEditText);
        spinBudgetAccueil = findViewById(R.id.budgetSpinner);
        spinTypeAccueil = findViewById(R.id.typeSpinner);
        voyagesListView = findViewById(R.id.voyagesListView);

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

        modelView.chargerVoyages(null, null, null, null);
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
        popupWindow.showAsDropDown(btnFilter, 0, 0);

        Button popupFilterButton = popupView.findViewById(R.id.popupFilterButton);
        popupTypeSpinner = popupView.findViewById(R.id.popupTypeSpinner);
        popupBudgetSlider = popupView.findViewById(R.id.popupBudgetSlider);
        popupDestinationSpinner = popupView.findViewById(R.id.popupDestinationSpinner);
        popupDatePicker = popupView.findViewById(R.id.popupDatePicker);
        popupDateSwitch = popupView.findViewById(R.id.popupDateSwitch);

        popupDateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                popupDatePicker.setVisibility(View.VISIBLE);
            } else {
                popupDatePicker.setVisibility(View.GONE);
            }
        });

        popupFilterButton.setOnClickListener(v -> {
                appliquerPopupFiltres();
                popupWindow.dismiss();
        });
    }


    // Appliquer les filtres depuis le popup
    private void appliquerPopupFiltres() {
        if (popupWindow != null && popupWindow.isShowing()) {

            String filtreType = popupTypeSpinner.getSelectedItem().toString();
            List<Float> filtreBudget = popupBudgetSlider.getValues();
            String filtreDestination = popupDestinationSpinner.getSelectedItem().toString();
            LocalDate filtreDate = null;

            if (filtreType.equalsIgnoreCase("Tous les types")) {
                filtreType = null;
            }
            if (filtreDestination.equalsIgnoreCase("Toutes les destinations")) {
                filtreDestination = null;
            }
            if (popupDateSwitch.isChecked()) {
                int jour = popupDatePicker.getDayOfMonth();
                int mois = popupDatePicker.getMonth() + 1;
                int annee = popupDatePicker.getYear();
                filtreDate = LocalDate.of(annee, mois, jour);
            }

            modelView.chargerVoyages(filtreType, filtreBudget, filtreDestination, filtreDate);
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