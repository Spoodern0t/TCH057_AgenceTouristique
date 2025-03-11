package com.example.agencetouristique7;
//

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