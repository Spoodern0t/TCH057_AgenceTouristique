package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.EcouteurDeDonnees;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VoyageDao {

    private static VoyageDao instance = null;
    private List<Voyage> voyages;

    public static VoyageDao getInstance() {
        if (instance == null)
            instance = new VoyageDao();
        return instance;
    }

    private VoyageDao() {

    }
    public static void getVoyages(String filtreType, List<Float> filtreBudget,
                                 EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        new HttpJsonService().getVoyages(filtreType, filtreBudget, ecouteurDeDonnees);
    }

    public static void updateTripAvailability(Voyage voyage,
                                              EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        new HttpJsonService().updateTripAvailability(voyage, ecouteurDeDonnees);
    }

    public void setVoyages(List<Voyage> voyages) {
        this.voyages = voyages;
    }

    public List<Voyage> getVoyages() {
        return voyages;
    }

    public List<String> getVoyagesDestinations() {
        List<String> voyagesDestinations = new ArrayList<>();
        voyagesDestinations.add("Toutes les destinations");
        if (voyages != null) {
            for (Voyage voyage : voyages) {
                String[] destinations = voyage.getDestination().split(",");
                String destination = destinations[destinations.length - 1].trim();
                if (!voyagesDestinations.contains(destination)) {
                    voyagesDestinations.add(destination);
                }
            }
        }
        return voyagesDestinations;
    }
}
