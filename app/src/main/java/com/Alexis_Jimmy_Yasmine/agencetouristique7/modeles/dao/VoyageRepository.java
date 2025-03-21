package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import kotlin.text.Regex;
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
        (new Thread("getVoyage") {
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


    public void chargerVoyages(String url, int[] budgetRange, Pattern regexPattern) {

        // Envoyer la requete dans un sidethread
        (new Thread("filtreVoyages") {
            @Override
            public void run() {

                Request requete = new Request.Builder().url(URL_POINT_ENTREE + url).build();

                try (Response reponse = okHttpClient.newCall(requete).execute()) {

                    ResponseBody corpsReponse = reponse.body();
                    if (corpsReponse != null) {

                        Voyage[] voyages = mapper.readValue(corpsReponse.string(), Voyage[].class);

                        if (regexPattern != null || budgetRange != null) {

                            // Filtrer les voyages qui match le regexPattern
                            List<Voyage> filteredVoyages = new ArrayList<>();
                            for (Voyage voyage : voyages) {

                                // Tester si le prix est dans le budget
                                boolean estDansRange = budgetRange == null || (voyage.getPrix() >= budgetRange[0] && voyage.getPrix() <= budgetRange[1]);

                                // Tester le regex et ajouter la la liste filtrée si ca renvoie vrai
                                boolean estMatch = regexPattern == null || regexPattern.matcher(voyage.getNomVoyage()).find();
                                if (estDansRange && estMatch) {
                                    filteredVoyages.add(voyage);
                                }
                            }

                            voyagesLiveData.postValue(filteredVoyages.toArray(new Voyage[0]));
                        } else {

                            voyagesLiveData.postValue(voyages);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
