package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.EcouteurDeDonnees;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import org.json.JSONException;

import java.io.IOException;

public class ClientDao {

    private static ClientDao instance = null;
    private Client client;

    public static ClientDao getInstance() {
        if (instance == null)
            instance = new ClientDao();
        return instance;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static void postClient(Client client,
                                  EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        new HttpJsonService().postClient(client, ecouteurDeDonnees);
    }

    public static void postConnexion(String email, String mdp, EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        new HttpJsonService().postConnexion(email, mdp, ecouteurDeDonnees);
    }

    public Client getClient() {
        return client;
    }
}
