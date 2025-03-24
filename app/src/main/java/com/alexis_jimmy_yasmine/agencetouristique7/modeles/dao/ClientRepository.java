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
    final private static String URL_POINT_ENTREE = "http://10.0.2.2:3000/clients";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MutableLiveData<String> connexionLiveData = new MutableLiveData<>();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final ObjectMapper mapper = new ObjectMapper();

    public LiveData<String> getConnexion() {
        return connexionLiveData;
    }

    // Ajouter un nouveau client à la base de données JSON
    public void postNouveauClient(Client client) {

        // Envoyer la requete dans un sidethread
        (new Thread() {
            @Override
            public void run() {

                try {
                    // Construire le corps de la requete POST
                    JSONObject postObj = new JSONObject();
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
                    Response postReponse = okHttpClient.newCall(postRequete).execute();
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
                Request requete = new Request.Builder().url(URL_POINT_ENTREE + "/?email=wd").build();
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
