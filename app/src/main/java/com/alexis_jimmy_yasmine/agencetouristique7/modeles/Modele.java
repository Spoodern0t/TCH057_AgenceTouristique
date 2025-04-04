package com.alexis_jimmy_yasmine.agencetouristique7.modeles;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import java.util.ArrayList;
import java.util.List;

public class Modele {

    private List<Voyage> voyages = new ArrayList<>();
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
}