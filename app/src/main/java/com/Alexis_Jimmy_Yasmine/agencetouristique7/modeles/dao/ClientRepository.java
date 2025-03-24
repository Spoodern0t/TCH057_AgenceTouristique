package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.*;

public class ClientRepository {
    final private static String URL_POINT_ENTREE = "http://10.0.2.2:3000/clients";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MutableLiveData<Integer> connexionLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> inscriptionLiveData = new MutableLiveData<>();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final ObjectMapper mapper = new ObjectMapper();

    public LiveData<Integer> getConnexion() {
        return connexionLiveData;
    }
    public LiveData<Integer> getInscription() { return  inscriptionLiveData; }


    // Ajouter un nouveau client à la base de données JSON
    public void postNouveauClient(Client client) {

        // Envoyer la requete dans un sidethread
        (new Thread() {
            @Override
            public void run() {

                boolean emailNonExistant = true;

                String url = URL_POINT_ENTREE + "/?email=" + client.getEmail();

                // Requete GET pour récupérer la liste des clients
                Request requete = new Request.Builder().url(url).build();
                try (Response reponse = okHttpClient.newCall(requete).execute()) {

                    if (reponse.body() != null) {
                        emailNonExistant = reponse.body().string().equals("[]");
                    }

                } catch (IOException e) {
                    inscriptionLiveData.postValue(404);
                }

                if (emailNonExistant) {
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

                        inscriptionLiveData.postValue(201);

                    } catch (IOException | JSONException e) {
                        inscriptionLiveData.postValue(404);
                    }
                } else {
                    inscriptionLiveData.postValue(401);
                }
            }
        }).start();
    }


    public void getConnexion(String email, String mdp) {
        // Envoyer la requete dans un sidethread
        (new Thread() {
            @Override
            public void run() {

                String url = URL_POINT_ENTREE + "/?email=" + email;

                // Requete GET pour récupérer la liste des clients
                Request requete = new Request.Builder().url(url).build();
                try (Response reponse = okHttpClient.newCall(requete).execute()) {

                    ResponseBody corpsReponse = reponse.body();
                    if (corpsReponse != null) {
                        if (!corpsReponse.string().equals("[]")) {

                            Client[] client = mapper.readValue(corpsReponse.string(), Client[].class);
                            if (client[0].getMdp().equals(mdp)) {
                                connexionLiveData.postValue(201);
                            } else {

                                connexionLiveData.postValue(402);
                            }

                        } else {

                            connexionLiveData.postValue(401);
                        }
                    }
                } catch (IOException e) {

                    inscriptionLiveData.postValue(404);
                }

                /*
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
                                connexionLiveData.postValue(201);
                                connexionFail = false;
                                break;
                            }
                        }

                        // Envoyer un message d'erreur car aucune connexion match
                        if (connexionFail) {
                            connexionLiveData.postValue(false);
                        }
                    }
                } catch (IOException e) {
                    connexionLiveData.postValue(404);
                }

                 */
            }

        }).start();
    }
}
