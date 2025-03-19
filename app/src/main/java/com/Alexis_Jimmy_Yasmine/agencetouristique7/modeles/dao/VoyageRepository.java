package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.*;

public class VoyageRepository {

    private static String URL_POINT_ENTREE = "http://10.0.2.2:3000/voyages";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MutableLiveData<Voyage[]> voyagesLiveData = new MutableLiveData<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public LiveData<Voyage[]> getVoyages() {
        return voyagesLiveData;
    }

    public void chargerVoyages(String url) {

        // Envoyer la requete dans un sidethread
        (new Thread() {
            @Override
            public void run() {

                // Requete GET pour récupérer les voyages
                Request requete = new Request.Builder().url(URL_POINT_ENTREE + url).build();
                try (Response reponse = okHttpClient.newCall(requete).execute()) {

                    ResponseBody corpsReponse = reponse.body();
                    if(corpsReponse != null) {

                        // Enregistrer les informations dans une liste de voyage
                        Voyage[] voyages = mapper.readValue(corpsReponse.string(), Voyage[].class);
                        voyagesLiveData.postValue(voyages);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }
}
