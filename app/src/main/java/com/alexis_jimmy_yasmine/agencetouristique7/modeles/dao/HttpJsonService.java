package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import androidx.annotation.NonNull;

import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.EcouteurDeDonnees;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpJsonService {

    private static final String URL_POINT_ENTREE = "http://10.0.2.2:3000";
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final MediaType JSON_PARSE = MediaType.parse("application/json");

    public void postConnexion(String email, String mdp, EcouteurDeDonnees ecouteurDeDonnees)
            throws IOException, JSONException {

        String path = "";

        if (!email.isEmpty())
            path+="email="+email;

        if (!path.isEmpty())
            path = "?"+path;

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/clients/"+path)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String jsonStr;
                if (response.body() != null) {
                    jsonStr = response.body().string();

                    //Traitement de la réponse ici
                    if (!jsonStr.equals("[]")) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            List<Client> clients = Arrays.asList(mapper.readValue(jsonStr, Client[].class));
                            Client client = clients.get(0);
                            if (Objects.equals(mdp, client.getMdp())) {
                                ecouteurDeDonnees.onDataLoaded(client);
                            } else {
                                ecouteurDeDonnees.onError("Le mot de passe est incorrect !");
                                call.cancel();
                            }
                        } catch (JsonProcessingException e) {
                            ecouteurDeDonnees.onError("Problème de JSON dans le client reçu !");
                            call.cancel();
                        }
                    } else {
                        ecouteurDeDonnees.onError("L'email est incorrect !");
                        call.cancel();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                ecouteurDeDonnees.onError("Problème de connexion au serveur !");
                call.cancel();
            }
        });
    }


    public void postClient(Client client, EcouteurDeDonnees ecouteurDeDonnees) throws JSONException {

        OkHttpClient okHttpClient = new OkHttpClient();

        Request getRequest = new Request.Builder()
                .url(URL_POINT_ENTREE + "/clients/?email=" + client.getEmail())
                .build();

        okHttpClient.newCall(getRequest).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String jsonStr;
                if (response.body() != null) {
                    jsonStr = response.body().string();

                    //Traitement de la réponse ici
                    if (!jsonStr.equals("[]")) {
                        ecouteurDeDonnees.onError("Un compte est déjà inscrit avec cet email !");
                        call.cancel();
                    } else {

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

                            Request postRequete = new Request.Builder()
                                    .url(URL_POINT_ENTREE + "/clients/")
                                    .post(corpsPostRequete)
                                    .build();

                            okHttpClient.newCall(postRequete).enqueue(new Callback() {
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) {
                                    ecouteurDeDonnees.onDataLoaded(client);
                                }

                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    ecouteurDeDonnees.onError("Problème de connexion au serveur !");
                                    call.cancel();
                                }
                            });
                        } catch (JSONException e) {
                            ecouteurDeDonnees.onError("Problème de JSON Object !");
                            call.cancel();
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                ecouteurDeDonnees.onError("Problème de connexion au serveur !");
                call.cancel();
            }
        });
    }


    public void getVoyages(String filtreType, List<Float> filtreBudget, EcouteurDeDonnees ecouteurDeDonnees)
            throws IOException, JSONException {

        String path = "?";

        if (!(filtreType == null))
            path += "type_de_voyage=" + filtreType + "&";

        if (!(filtreBudget == null)) {
            path += "prix_gte=" + filtreBudget.get(0) + "&";
            path += "prix_lte=" + filtreBudget.get(1) + "&";
        }

        // Enlève le symbole dernier symbole de path (soit "&" ou "?")
        path = path.substring(0, path.length() - 1);


        OkHttpClient okHttpClient = new OkHttpClient();


        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/voyages/"+path)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String jsonStr;
                if (response.body() != null) {
                    jsonStr = response.body().string();

                    //Traitement de la réponse ici
                    if (!jsonStr.isEmpty()) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {

                            List<Voyage> voyages = Arrays.asList(mapper.readValue(jsonStr, Voyage[].class));
                            ecouteurDeDonnees.onDataLoaded(voyages);
                        } catch (JsonProcessingException e) {
                            ecouteurDeDonnees.onError("Problème du JSON dans les voyages reçus");
                            call.cancel();
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                ecouteurDeDonnees.onError("Problème de connexion au serveur !");
                call.cancel();
            }
        });
    }


    public void getVoyages(EcouteurDeDonnees ecouteurDeDonnees)
            throws IOException, JSONException {

        OkHttpClient okHttpClient = new OkHttpClient();


        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/voyages/")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String jsonStr;
                if (response.body() != null) {
                    jsonStr = response.body().string();

                    //Traitement de la réponse ici
                    if (!jsonStr.isEmpty()) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {

                            List<Voyage> voyages = Arrays.asList(mapper.readValue(jsonStr, Voyage[].class));
                            ecouteurDeDonnees.onDataLoaded(voyages);
                        } catch (JsonProcessingException e) {
                            ecouteurDeDonnees.onError("Problème du JSON dans les voyages reçus");
                            call.cancel();
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                ecouteurDeDonnees.onError("Problème de connexion au serveur !");
                call.cancel();
            }
        });
    }


    public void getVoyageById(String voyageId, EcouteurDeDonnees ecouteurDeDonnees)
            throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();


        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/voyages/"+ voyageId)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String jsonStr;
                if (response.body() != null) {
                    jsonStr = response.body().string();

                    //Traitement de la réponse ici
                    if (!jsonStr.isEmpty()) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {

                            Voyage voyages = mapper.readValue(jsonStr, Voyage.class);
                            ecouteurDeDonnees.onDataLoaded(voyages);
                        } catch (JsonProcessingException e) {
                            ecouteurDeDonnees.onError("Problème du JSON dans le voyage reçu");
                            call.cancel();
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                ecouteurDeDonnees.onError("Problème de connexion au serveur !");
                call.cancel();
            }
        });
    }

    public void updateTripAvailability(Voyage voyage, EcouteurDeDonnees ecouteurDeDonnees)
        throws IOException, JSONException {

        OkHttpClient okHttpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String jsonObj;
        try {
            jsonObj = mapper.writeValueAsString(voyage);
        } catch (JsonProcessingException e) {
            ecouteurDeDonnees.onError("Problème pour Mapper le voyage");
            return;
        }

        // Requete PUT pour modifier le nombre de place d'un voyage
        RequestBody corpsPutRequete = RequestBody.create(jsonObj, JSON_PARSE);

        Request putRequest = new Request.Builder()
                .url(URL_POINT_ENTREE + "/voyages/" + voyage.getId())
                .put(corpsPutRequete)
                .build();

        okHttpClient.newCall(putRequest).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {

                ecouteurDeDonnees.onDataLoaded(null);
            }

            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                ecouteurDeDonnees.onError("Problème de connexion au serveur !");
                call.cancel();
            }
        });
    }
}
