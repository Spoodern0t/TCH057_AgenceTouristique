package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.EcouteurDeDonnees;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;

import org.json.JSONException;

import java.io.IOException;

public class ClientDao {

    public static void postClient(Client client,
                                  EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        new HttpJsonService().postClient(client, ecouteurDeDonnees);
    }

    public static void postConnexion(String email, String mdp, EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        new HttpJsonService().postConnexion(email, mdp, ecouteurDeDonnees);
    }
}
