package com.alexis_jimmy_yasmine.agencetouristique7.VueModele;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.Modele;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.ModeleManager;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ClientDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.VoyageDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class AgenceViewModel extends ViewModel {

    private final MutableLiveData<Boolean> connexionLiveData = new MutableLiveData<>();
    private final MutableLiveData<Client> inscriptionLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Voyage>> voyagesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreurLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> reservationResult = new MutableLiveData<>();

    private final Modele modele;

    public AgenceViewModel() {
        this.modele = ModeleManager.getModele();
    }

    public LiveData<Boolean> getConnexion() {
        return connexionLiveData;
    }

    public LiveData<Client> getInscription() {
        return inscriptionLiveData;
    }

    public LiveData<List<Voyage>> getVoyages() {
        return voyagesLiveData;
    }

    public LiveData<String> getErreur() {
        return erreurLiveData;
    }

    public void postConnexion(String email, String mdp) {
        try {
            ClientDao.postConnexion(email, mdp, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    connexionLiveData.postValue(true);
                }

                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des comptes");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }

    public void postClient(Client client) {
        try {
            ClientDao.postClient(client, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    inscriptionLiveData.postValue(client);
                }
                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des comptes");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }

    public void chargerVoyages(String filtreType, int[] filtreBudget, String filtreDestination) {
        try {
            VoyageDao.getVoyages(filtreType, filtreBudget, filtreDestination, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    List<Voyage> voyages = (List<Voyage>) data;
                    modele.setVoyages(voyages);
                    voyagesLiveData.postValue(voyages);
                }

                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des comptes");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }
}
