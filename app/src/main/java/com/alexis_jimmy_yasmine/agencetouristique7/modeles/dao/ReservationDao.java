package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import android.content.Context;

import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.EcouteurDeDonnees;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.SQLite.ReservationDatabaseHelper;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;

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

    public void ajouterReservation(Reservation reservation, EcouteurDeDonnees ecouteurDeDonnees) {
        dbHelper.ajouterReservation(reservation, ecouteurDeDonnees);
    }

    public void supprimerReservation(Reservation reservation, EcouteurDeDonnees ecouteurDeDonnees) {
        dbHelper.annulerReservation(reservation, ecouteurDeDonnees);
    }
}