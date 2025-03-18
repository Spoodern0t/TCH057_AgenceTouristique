package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import androidx.annotation.NonNull;
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

    public void chargerVoyage(String url) {

        Request requete = new Request.Builder().url(URL_POINT_ENTREE + url).build();

        // Envoyer la requete en queue avec un callback lorsqu'il recoit une réponse
        okHttpClient.newCall(requete).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.body() != null) {
                    Voyage[] voyages = mapper.readValue(response.body().string(), Voyage[].class);
                    voyagesLiveData.postValue(voyages);
                }
            }
        });
    }
}
