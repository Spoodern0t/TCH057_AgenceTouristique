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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class VoyageRepository {

    private static String URL_POINT_ENTREE = "http://10.0.2.2:3000";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MutableLiveData<Voyage[]> voyagesLiveData = new MutableLiveData<>();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public LiveData<Voyage[]> getVoyages() {
        return voyagesLiveData;
    }

    public void chargerVoyage() {

        Request requete = new Request.Builder().url(URL_POINT_ENTREE + "/voyages").build();

        // Envoyer la requete en queue avec un callback lorsqu'il recoit une réponse
        okHttpClient.newCall(requete).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.body() != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    Voyage[] voyages = mapper.readValue(response.body().string(), Voyage[].class);
                    voyagesLiveData.postValue(voyages);
                }
            }
        });
    }

    /* TODO: Requete POST inscription
    public int postNouveauClient(Client client) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("id", client.getId());
        obj.put("nom", client.getNom());
        obj.put("prenom", client.getPrenom());
        obj.put("email", client.getEmail());
        obj.put("mdp", client.getMdp());
        obj.put("age", client.getAge());
        obj.put("telephone", client.getTelephone());
        obj.put("adresse", client.getAdresse());

        RequestBody corpsRequete = RequestBody.create(obj.toString(), JSON);
        Request requete = new Request.Builder().url(URL_POINT_ENTREE + "/comptes").post(corpsRequete).build();

        (new Thread() {
            @Override
            public void run() {
                try (Response response = okHttpClient.newCall(requete).execute()) {
                    return response.code();
                } catch (IOException e) {
                    return 400;
                }
            }
        }
    }
     */
}
