package com.alexis_jimmy_yasmine.agencetouristique7.VueModele;

public interface EcouteurDeDonnees {
    void onDataLoaded(Object data);
    void onError(String errorMessage);
}
