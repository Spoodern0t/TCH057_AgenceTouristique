package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class ClientRepository {
    private static String URL_POINT_ENTREE = "http://10.0.2.2:3000/clients";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MutableLiveData<String> connexionLiveData = new MutableLiveData<>();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final ObjectMapper mapper = new ObjectMapper();

    public LiveData<String> getConnexion() {
        return connexionLiveData;
    }

    // Ajouter un nouveau client à la base de données JSON
    public void postNouveauClient(Client client) throws JSONException {

        // Envoyer la requete dans un sidethread
        (new Thread() {
            @Override
            public void run() {

                // Initier la variable de stockage de l'ID
                long newId = 0;

                // Requete GET pour avoir la valeur du dernier ID utilisé
                Request requete = new Request.Builder().url(URL_POINT_ENTREE + "-id").build();
                try (Response reponse = okHttpClient.newCall(requete).execute()) {
                    ResponseBody corpsReponse = reponse.body();
                    if(corpsReponse != null) {
                        // Incrémenter la valeur du dernier ID de client
                        newId = new JSONObject(corpsReponse.string()).getLong("id") + 1;
                    }

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }


                try {
                    // Construire le corps de la requete POST
                    JSONObject postObj = new JSONObject();
                    postObj.put("id", newId);
                    postObj.put("nom", client.getNom());
                    postObj.put("prenom", client.getPrenom());
                    postObj.put("email", client.getEmail());
                    postObj.put("mdp", client.getMdp());
                    postObj.put("age", client.getAge());
                    postObj.put("telephone", client.getTelephone());
                    postObj.put("adresse", client.getAdresse());

                    // Requete POST pour ajouter un nouveau client
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);
                    Request postRequete = new Request.Builder().url(URL_POINT_ENTREE).post(corpsPostRequete).build();
                    Response putReponse = okHttpClient.newCall(postRequete).execute();
                    putReponse.close();

                    // Construire le corps de la requete POST
                    JSONObject putObj = new JSONObject();
                    putObj.put("id", newId);

                    // Requete PUT pour modifier la valeur du dernier ID utilisé
                    RequestBody corpsPutRequete = RequestBody.create(putObj.toString(), JSON);
                    Request putRequete = new Request.Builder().url(URL_POINT_ENTREE + "-id").put(corpsPutRequete).build();
                    Response postReponse =  okHttpClient.newCall(putRequete).execute();
                    postReponse.close();

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    // ATTENTION ! Utiliser une requete GET pour une connexion n'est pas sécuritaire.
    public void connexion(String email, String mdp) throws JSONException {
        // Envoyer la requete dans un sidethread
        (new Thread() {
            @Override
            public void run() {

                // Requete GET pour récupérer la liste des clients
                Request requete = new Request.Builder().url(URL_POINT_ENTREE).build();
                try (Response reponse = okHttpClient.newCall(requete).execute()) {

                    // Si aucune connexion match, ce boolean reste true
                    boolean connexionFail = true;

                    ResponseBody corpsReponse = reponse.body();
                    if(corpsReponse != null) {

                        // Enregistrer les informations dans une liste de client
                        Client[] clients = mapper.readValue(corpsReponse.string(), Client[].class);
                        for (Client client : clients) {
                            if (client.getEmail().equals(email) && client.getMdp().equals(mdp)) {

                                // Envoyer un message de connexion
                                connexionLiveData.postValue("Connexion en cours");
                                connexionFail = false;
                                break;
                            }
                        }

                        // Envoyer un message d'erreur car aucune connexion match
                        if (connexionFail) {
                            connexionLiveData.postValue("Les informations entrées sont incorrects");
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
