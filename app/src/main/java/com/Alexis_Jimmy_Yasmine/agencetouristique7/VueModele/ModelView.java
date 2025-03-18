package com.alexis_jimmy_yasmine.agencetouristique7.VueModele;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ClientRepository;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.VoyageRepository;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import org.json.JSONException;

public class ModelView extends ViewModel {

    private final VoyageRepository voyageRepository;
    private final LiveData<Voyage[]> voyages;
    private final ClientRepository clientRepository;
    private final LiveData<String> connexion;

    public ModelView() {
        voyageRepository = new VoyageRepository();
        voyages = voyageRepository.getVoyages();
        clientRepository = new ClientRepository();
        connexion = clientRepository.getConnexion();
    }

    public LiveData<Voyage[]> getVoyages() {
        return voyages;
    }

    public void chargerVoyages(String url) {
        voyageRepository.chargerVoyage(url);
    }

    public LiveData<String> getConnexion() {
        return connexion;
    }

    public void postConnexion(String email, String mdp) throws JSONException {
        clientRepository.connexion(email, mdp);
    }

    public void postClient(Client client) throws JSONException {
        clientRepository.postNouveauClient(client);
    }
}
