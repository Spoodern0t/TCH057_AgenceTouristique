package com.alexis_jimmy_yasmine.agencetouristique7.modeles;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import java.util.ArrayList;
import java.util.List;

public class Modele {
    private List<Voyage> voyages = new ArrayList<>();
    public List<Voyage> getvoyages() {
        return voyages;
    }
    public void setVoyages(List<Voyage> voyages) {
        this.voyages = voyages;
    }
}