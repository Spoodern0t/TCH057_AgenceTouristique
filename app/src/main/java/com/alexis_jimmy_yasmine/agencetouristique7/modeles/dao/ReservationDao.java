package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import android.content.Context;

import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.EcouteurDeDonnees;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.SQLite.ReservationDatabaseHelper;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

public class ReservationDao {

    private static ReservationDao instance = null;

    public static ReservationDao getInstance() {
        if (instance == null)
            instance = new ReservationDao();
        return instance;
    }

    private static ReservationDatabaseHelper dbHelper;

    public static void connexionBD(Context context) {
        dbHelper = new ReservationDatabaseHelper(context);
    }

    public void chargerReservations(String userId, EcouteurDeDonnees ecouteurDeDonnees) {
        dbHelper.chargerReservations(userId, ecouteurDeDonnees);
    }

    public void ajouterReservation(Voyage voyage, Voyage.Trip tripChoisi, int nbPlacesReservees, EcouteurDeDonnees ecouteurDeDonnees) {
        Reservation reservation = new Reservation(ClientDao.getInstance().getClient().getId(),
                voyage.getId(), voyage.getDestination(),
                tripChoisi.getDate(), voyage.getPrix() * nbPlacesReservees,
                "Confirmée", nbPlacesReservees, voyage.getImageUrls().get(0), voyage, tripChoisi);
        dbHelper.ajouterReservation(reservation, ecouteurDeDonnees);
    }

    public void supprimerReservation(Reservation reservation, EcouteurDeDonnees ecouteurDeDonnees) {
        dbHelper.annulerReservation(reservation, ecouteurDeDonnees);
    }
}