package com.alexis_jimmy_yasmine.agencetouristique7.VueModele;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.VoyageRepository;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

public class VoyageModelView extends ViewModel {

    private final VoyageRepository repository;
    private final LiveData<Voyage[]> voyages;

    public VoyageModelView() {
        repository = new VoyageRepository();
        voyages = repository.getVoyages();
    }

    public LiveData<Voyage[]> getVoyages() {
        return voyages;
    }

    public void chargerVoyages() {
        repository.chargerVoyage();
    }
}
