package com.alexis_jimmy_yasmine.agencetouristique7.VueModele;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ClientDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;

import org.json.JSONException;

import java.io.IOException;

public class ClientViewModel extends ViewModel {

    private final MutableLiveData<Boolean> connexionLiveData = new MutableLiveData<>();
    private final MutableLiveData<Client> inscriptionLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreurLiveData = new MutableLiveData<>();

    private final ClientDao clientDao;

    public ClientViewModel() {
        this.clientDao = ClientDao.getInstance();
    }

    public LiveData<Boolean> getConnexion() {
        return connexionLiveData;
    }

    public LiveData<Client> getInscription() {
        return inscriptionLiveData;
    }

    public LiveData<String> getErreur() {
        return erreurLiveData;
    }

    public void postConnexion(String email, String mdp) {
        try {
            ClientDao.postConnexion(email, mdp, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    Client client = (Client) data;
                    clientDao.setClient(client);
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
}
