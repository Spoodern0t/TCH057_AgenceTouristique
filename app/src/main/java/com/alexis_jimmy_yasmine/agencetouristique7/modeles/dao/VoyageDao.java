package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.EcouteurDeDonnees;

import org.json.JSONException;

import java.io.IOException;

public class VoyageDao {
    public static void getVoyages(String filtreType, int[] filtreBudget, String filtreDestination,
                                 EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        new HttpJsonService().getVoyages(filtreType, filtreBudget, filtreDestination, ecouteurDeDonnees);
    }
}
