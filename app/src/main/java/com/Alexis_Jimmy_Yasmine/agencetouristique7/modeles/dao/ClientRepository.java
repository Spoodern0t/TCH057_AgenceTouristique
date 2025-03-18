package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
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

    public void postNouveauClient(Client client) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("nom", client.getNom());
        obj.put("prenom", client.getPrenom());
        obj.put("email", client.getEmail());
        obj.put("mdp", client.getMdp());
        obj.put("age", client.getAge());
        obj.put("telephone", client.getTelephone());
        obj.put("adresse", client.getAdresse());

        RequestBody corpsRequete = RequestBody.create(obj.toString(), JSON);
        Request requete = new Request.Builder().url(URL_POINT_ENTREE).post(corpsRequete).build();

        // Envoyer la requete en queue avec un callback lorsqu'il recoit une réponse
        (new Thread() {
            @Override
            public void run() {
                try (Response response = okHttpClient.newCall(requete).execute()) {
                    //
                } catch (IOException e) {
                    //
                }
            }
        }).start();
    }

    // FIXME: Un problème est dû à l'incrémentation automatique de l'id chez JSON.
    // FIXME: Il génère des string, mais on veut un int.
    // Utiliser une requete GET pour une connexion n'est pas sécuritaire.
    public void connexion(String email, String mdp) throws JSONException {

        Request requete = new Request.Builder().url(URL_POINT_ENTREE).build();

        // Envoyer la requete en queue avec un callback lorsqu'il recoit une réponse
        okHttpClient.newCall(requete).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                boolean connexionFail = true;
                if(response.body() != null) {
                    Client[] clients = mapper.readValue(response.body().string(), Client[].class);
                    for (Client client : clients) {
                        if (client.getEmail().equals(email) && client.getMdp().equals(mdp)) {
                            connexionLiveData.postValue("Connexion en cours");
                            connexionFail = false;
                            break;
                        }
                    }

                    if (connexionFail) {
                        connexionLiveData.postValue("Les informations entrées sont incorrects");
                    }
                }
            }
        });
    }
}
