package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.EcouteurDeDonnees;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class VoyageDao {
    public static void getVoyages(String filtreType, List<Float> filtreBudget,
                                 EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        new HttpJsonService().getVoyages(filtreType, filtreBudget, ecouteurDeDonnees);
    }

    public static void updateTripAvailability(Voyage voyage, int position,
                                              EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        new HttpJsonService().updateTripAvailability(voyage, position, ecouteurDeDonnees);
    }
}
