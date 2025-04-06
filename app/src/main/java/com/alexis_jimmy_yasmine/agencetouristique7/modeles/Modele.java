package com.alexis_jimmy_yasmine.agencetouristique7.modeles;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import java.util.ArrayList;
import java.util.List;

public class Modele {

    private List<Voyage> voyages;

    public Modele() {
        this.voyages = new ArrayList<>();
    }
    public void setVoyages(List<Voyage> voyages) {
        this.voyages = voyages;
    }


    public Voyage getVoyageById(String id) {
        for (Voyage voyage:voyages) {
            if (id.equals(voyage.getId()))
                return voyage;
        }
        return null;
    }

    public int getVoyagePosition(String id) {
        for (int i=0; i < voyages.size(); i++) {
            if (voyages.get(i).getId().equals(id))
                return i + 1;
        }
        return 0;
    }

    public List<Voyage> getVoyages() {
        return voyages;
    }

    public List<String> getVoyagesDestinations() {
        List<String> voyagesDestinations = new ArrayList<>();
        voyagesDestinations.add("Toutes les destinations");
        for (Voyage voyage: voyages) {
            String[] destinations = voyage.getDestination().split(",");
            String destination = destinations[destinations.length - 1].trim();
            if (!voyagesDestinations.contains(destination)) {
                voyagesDestinations.add(destination);
            }
        }
        return voyagesDestinations;
    }
}