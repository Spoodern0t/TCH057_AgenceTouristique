package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import android.content.Context;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.SQLite.ReservationDatabaseHelper;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;

import java.util.List;

public class ReservationDao {

    private final ReservationDatabaseHelper dbHelper;

    public ReservationDao(Context context) {
        dbHelper = new ReservationDatabaseHelper(context);
    }

    public List<Reservation> getAllReservations() {
        return dbHelper.getAllReservations();
    }

    public boolean ajouterReservation(Reservation reservation) {
        return dbHelper.ajouterReservation(reservation);
    }

    public int supprimerReservation(int reservationId) {
        return dbHelper.supprimerReservation(reservationId);
    }
}