package com.alexis_jimmy_yasmine.agencetouristique7.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AccueilActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {

    private AgenceViewModel modelView;
    private EditText searchEditText;
    private ListView voyagesListView;
    private ImageButton btnHome, btnHistorique, btnLogout, btnFilter;
    private PopupWindow popupWindow;
    private ActivityResultLauncher<Intent> launcher;
    private List<String> destinationListe = new ArrayList<>();

    // Composant du popup
    View popupView;
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
        voyagesListView = findViewById(R.id.voyagesListView);

        searchEditText.addTextChangedListener(this);

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

            // Singleton pour la liste de destinations du filtre
            if (destinationListe.isEmpty()) {
                destinationListe = modelView.getVoyagesDestinations();
                ArrayAdapter<String> destinationAdaptateur = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, destinationListe);
                popupDestinationSpinner.setAdapter(destinationAdaptateur);
            }

            voyagesListView.setAdapter(new VoyagesAdaptateur(AccueilActivity.this, R.layout.layout_voyage, tabVoyages));
        });
        modelView.chargerVoyages(null, null, null, null);
        creerPopUpFiltre();
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
            popupWindow.showAsDropDown(btnFilter, 0, 0);
        }
    }

    private void creerPopUpFiltre() {
        popupView = View.inflate(this, R.layout.popup_filtre, null);
        popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );
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

        popupWindow.setOnDismissListener(this::appliquerPopupFiltres);
    }

    // Appliquer les filtres depuis le popup
    private void appliquerPopupFiltres() {
        if (popupWindow != null) {

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

    // Envoyer vers l'activité de détail du voyage cliqué
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        Voyage voyageClique = (Voyage) parent.getAdapter().getItem(i);
        intent.putExtra("ID", voyageClique.getId());
        launcher.launch(intent);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String nomLike = String.valueOf(s);
        if (!nomLike.isEmpty()) {
            Pattern regex = Pattern.compile(nomLike, Pattern.CASE_INSENSITIVE);
            modelView.regexVoyages(regex);
        } else {
            modelView.regexVoyages(null);
        }
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