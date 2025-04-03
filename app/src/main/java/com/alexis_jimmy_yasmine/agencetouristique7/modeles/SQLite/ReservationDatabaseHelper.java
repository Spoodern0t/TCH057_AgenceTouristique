package com.alexis_jimmy_yasmine.agencetouristique7.modeles.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationDatabaseHelper extends SQLiteOpenHelper {

    public ReservationDatabaseHelper(Context context) {
        super(context, TauxContract.DATABASE_NAME, null, TauxContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESERVATIONS_TABLE = "CREATE TABLE " +TauxContract.TABLE_NAME + " (" +
                        TauxContract.Colonnes.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        TauxContract.Colonnes.USER_ID + " text," +
                        TauxContract.Colonnes.VOYAGE_ID + " text," +
                        TauxContract.Colonnes.DESTINATION + " text," +
                        TauxContract.Colonnes.DATE_VOYAGE + " text," +
                        TauxContract.Colonnes.MONTANT_PAYE + " double," +
                        TauxContract.Colonnes.STATUT + " text," +
                        TauxContract.Colonnes.NB_PERSONNES + " text," +
                        TauxContract.Colonnes.IMAGE_URL + " integer)";
        db.execSQL(CREATE_RESERVATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String requeteSuppressionTable = String.format("drop table if exists %s ",
                TauxContract.TABLE_NAME);
        db.execSQL(requeteSuppressionTable);
        onCreate(db);
    }

    // Méthode pour ajouter une nouvelle réservation à la base de données locale
    public boolean ajouterReservation(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put(TauxContract.Colonnes.USER_ID, reservation.getUserId());
        values.put(TauxContract.Colonnes.VOYAGE_ID, reservation.getVoyageId());
        values.put(TauxContract.Colonnes.DESTINATION, reservation.getDestination());
        values.put(TauxContract.Colonnes.DATE_VOYAGE, reservation.getDateVoyage());
        values.put(TauxContract.Colonnes.MONTANT_PAYE, reservation.getMontantPaye());
        values.put(TauxContract.Colonnes.STATUT, reservation.getStatut());
        values.put(TauxContract.Colonnes.NB_PERSONNES, reservation.getNbPersonnes());
        values.put(TauxContract.Colonnes.IMAGE_URL, reservation.getImageUrl());


        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.insert(TauxContract.TABLE_NAME, null, values);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // Méthode pour récupérer toutes les réservations de la base de données locale
    public List<Reservation> getAllReservations() {
        List<Reservation> listeReservations = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TauxContract.TABLE_NAME;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation();
                reservation.setId(Integer.parseInt(cursor.getString(0)));
                reservation.setUserId(cursor.getString(1));
                reservation.setVoyageId(cursor.getString(2));
                reservation.setDestination(cursor.getString(3));
                reservation.setDateVoyage(cursor.getString(4));
                reservation.setMontantPaye(Double.parseDouble(cursor.getString(5)));
                reservation.setStatut(cursor.getString(6));
                reservation.setNbPersonnes(Integer.parseInt(cursor.getString(7)));
                reservation.setImageUrl(cursor.getString(8));
                listeReservations.add(reservation);
            } while (cursor.moveToNext());
        }
        return listeReservations;
    }

    public boolean supprimerReservation(int reservationId) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TauxContract.TABLE_NAME,
                    TauxContract.Colonnes.ID + " = ?",
                    new String[]{String.valueOf(reservationId)});
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}