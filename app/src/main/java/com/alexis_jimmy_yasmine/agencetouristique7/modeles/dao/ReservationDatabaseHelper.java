package com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AgenceTouristiqueDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RESERVATIONS = "reservations";
    public static final String COLONNE_ID = "id";
    public static final String COLONNE_DESTINATION = "destination";
    public static final String COLONNE_DATE_VOYAGE = "date_voyage";
    public static final String COLONNE_MONTANT_PAYE = "montant_paye";
    public static final String COLONNE_STATUT = "statut";
    public static final String COLONNE_NB_PERSONNES = "nb_personnes";
    public static final String COLONNE_IMAGE_URL = "image_url";

    public ReservationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESERVATIONS_TABLE = "CREATE TABLE " + TABLE_RESERVATIONS + "("
                + COLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLONNE_DESTINATION + " TEXT,"
                + COLONNE_DATE_VOYAGE + " TEXT,"
                + COLONNE_MONTANT_PAYE + " REAL,"
                + COLONNE_STATUT + " TEXT,"
                + COLONNE_NB_PERSONNES + " INTEGER,"
                + COLONNE_IMAGE_URL + " TEXT" + ")";
        db.execSQL(CREATE_RESERVATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }

    // Méthode pour ajouter une nouvelle réservation à la base de données locale
    public long ajouterReservation(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put(COLONNE_DESTINATION, reservation.getDestination());
        values.put(COLONNE_DATE_VOYAGE, reservation.getDateVoyage());
        values.put(COLONNE_MONTANT_PAYE, reservation.getMontantPaye());
        values.put(COLONNE_STATUT, reservation.getStatut());
        values.put(COLONNE_NB_PERSONNES, reservation.getNbPersonnes());
        values.put(COLONNE_IMAGE_URL, reservation.getImageUrl());

        long id = 0;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            id = db.insert(TABLE_RESERVATIONS, null, values);
        }
        return id;
    }

    // Méthode pour récupérer toutes les réservations de la base de données locale
    public List<Reservation> getAllReservations() {
        List<Reservation> listeReservations = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_RESERVATIONS;

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    Reservation reservation = new Reservation();
                    reservation.setId(Integer.parseInt(cursor.getString(0)));
                    reservation.setDestination(cursor.getString(1));
                    reservation.setDateVoyage(cursor.getString(2));
                    reservation.setMontantPaye(Double.parseDouble(cursor.getString(3)));
                    reservation.setStatut(cursor.getString(4));
                    reservation.setNbPersonnes(Integer.parseInt(cursor.getString(5)));
                    reservation.setImageUrl(cursor.getString(6));
                    listeReservations.add(reservation);
                } while (cursor.moveToNext());
            }
        }
        return listeReservations;
    }

    public int supprimerReservation(int reservationId) {
        int deletedRows = 0;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            deletedRows = db.delete(TABLE_RESERVATIONS, COLONNE_ID + " = ?",
                    new String[]{String.valueOf(reservationId)});
        }
        return deletedRows;
    }
}