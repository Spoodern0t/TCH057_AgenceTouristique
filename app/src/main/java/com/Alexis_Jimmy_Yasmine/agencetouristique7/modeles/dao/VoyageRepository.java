package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.*;
public class VoyageRepository {

    private static String URL_POINT_ENTREE = "http://10.0.2.2:3000/voyages";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MutableLiveData<Voyage[]> voyagesLiveData = new MutableLiveData<>();
    private final ObjectMapper mapper = new ObjectMapper();

    private final MutableLiveData<String> reservationResultLiveData = new MutableLiveData<>("Error: Network error");

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public LiveData<Voyage[]> getVoyages() {
        return voyagesLiveData;
    }

    public LiveData<String> getReservationResultLiveData() {
        return reservationResultLiveData;
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
                    if (corpsReponse != null) {

                        // Enregistrer les informations dans une liste de voyage
                        Voyage[] voyages = mapper.readValue(corpsReponse.string(), Voyage[].class);
                        voyagesLiveData.postValue(voyages);
                    } else {
                        voyagesLiveData.postValue(null);
                    }

                } catch (IOException e) {
                    voyagesLiveData.postValue(null);
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
                    voyagesLiveData.postValue(null);
                }
            }
        }).start();
    }

    public void reserverVoyage(int voyageId, String tripDate, int nbPlaces) {
        // Envoyer la requete dans un sidethread
        new Thread(() -> {
            try {
                // Creer le corps de la requete en JSON
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nbPlaces", nbPlaces);

                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

                // Requete POST pour reserver un voyage
                Request request = new Request.Builder()
                        .url(URL_POINT_ENTREE + "/" + voyageId + "/trips/" + tripDate + "/reserve")
                        .post(body)
                        .build();

                // Executer la requete
                try (Response response = okHttpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        // Gérer la réponse en cas d'erreur
                        String errorBody = response.body().string();
                        try {
                            JSONObject errorJson = new JSONObject(errorBody);
                            String errorMessage = errorJson.optString("error", "Reservation failed :(");
                            reservationResultLiveData.postValue("Error: " + errorMessage);
                        } catch (JSONException e) {
                            reservationResultLiveData.postValue("Error: " + errorBody);
                        }
                    } else {
                        // En cas de succès, recharger les détails du voyage et signaler le succès
                        chargerVoyages("/?id=" + voyageId);
                        reservationResultLiveData.postValue("Success: Reservation successful :)");
                    }
                }
            } catch (IOException e) {
                reservationResultLiveData.postValue("Error: Network error");
            } catch (JSONException e) {
                reservationResultLiveData.postValue("Error: JSON error");
            }
        }).start();
    }

    public void mettreAJourDisponibilite(int voyageId, String tripDate, int nouveauNbPlacesDisponibles) {
        new Thread(() -> {
            try {
                 Request getVoyageRequest = new Request.Builder()
                        .url(URL_POINT_ENTREE + "/" + voyageId)
                        .get()
                        .build();

                Voyage voyageToUpdate = null;
                try (Response getVoyageResponse = okHttpClient.newCall(getVoyageRequest).execute()) {
                    if (getVoyageResponse.isSuccessful() && getVoyageResponse.body() != null) {
                        voyageToUpdate = mapper.readValue(getVoyageResponse.body().string(), Voyage.class);
                    } else {
                        Log.e("VoyageRepository", "Erreur lors de la récupération du voyage pour mise à jour: " + getVoyageResponse.code());
                        return;
                    }
                }

                if (voyageToUpdate == null || voyageToUpdate.getTrips() == null) {
                    Log.e("VoyageRepository", "Voyage ou trips introuvables pour mise à jour");
                    return;
                }

                 boolean tripUpdated = false;
                for (Voyage.Trip trip : voyageToUpdate.getTrips()) {
                    if (trip.getDate().equals(tripDate)) {
                        trip.setNbPlacesDisponibles(nouveauNbPlacesDisponibles);
                        tripUpdated = true;
                        break;
                    }
                }

                if (!tripUpdated) {
                    Log.e("VoyageRepository", "Trip avec la date " + tripDate + " non trouvé pour mise à jour");
                    return;
                }

                // Convert Voyage object back to JSON
                JSONObject voyageJsonObject = new JSONObject(mapper.writeValueAsString(voyageToUpdate));
                RequestBody body = RequestBody.create(voyageJsonObject.toString(), JSON);

                 String url = URL_POINT_ENTREE + "/" + voyageId;
                Request request = new Request.Builder()
                        .url(url)
                        .put(body)
                        .build();

                 try (Response response = okHttpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        Log.e("VoyageRepository", "Erreur lors de la mise à jour du serveur: " + response.code());
                    } else {
                        Log.d("VoyageRepository", "Serveur mis à jour avec succès pour la réservation");
                        chargerVoyages("/?id=" + voyageId);
                    }
                }
            } catch (IOException e) {
                Log.e("VoyageRepository", "Erreur réseau lors de la mise à jour du serveur", e);
            } catch (JSONException e) {
                Log.e("VoyageRepository", "Erreur JSON lors de la mise à jour du serveur", e);
            }
        }).start();
    }
}
