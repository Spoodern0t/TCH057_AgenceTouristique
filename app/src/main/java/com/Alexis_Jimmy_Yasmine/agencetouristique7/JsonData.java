package com.Alexis_Jimmy_Yasmine.agencetouristique7;
//

import com.Alexis_Jimmy_Yasmine.agencetouristique7.entitee.Client;
import com.Alexis_Jimmy_Yasmine.agencetouristique7.entitee.Voyage;

import java.util.List;

public class JsonData {
    private List<Voyage> voyages;
    private List<Client> clients;

    public JsonData() {
        // Default constructor
    }

    public List<Voyage> getVoyages() {
        return voyages;
    }

    public void setVoyages(List<Voyage> voyages) {
        this.voyages = voyages;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}