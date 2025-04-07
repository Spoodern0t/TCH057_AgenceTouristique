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
        super(context, ReservationContract.DATABASE_NAME, null, ReservationContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESERVATIONS_TABLE = "CREATE TABLE " + ReservationContract.TABLE_NAME + " (" +
                        ReservationContract.Colonnes.USER_ID + " text," +
                        ReservationContract.Colonnes.VOYAGE_ID + " text," +
                        ReservationContract.Colonnes.DESTINATION + " text," +
                        ReservationContract.Colonnes.DATE_VOYAGE + " text," +
                        ReservationContract.Colonnes.MONTANT_PAYE + " double," +
                        ReservationContract.Colonnes.STATUT + " text," +
                        ReservationContract.Colonnes.NB_PERSONNES + " text," +
                        ReservationContract.Colonnes.IMAGE_URL + " integer," +
                        String.format("PRIMARY KEY (%s, %s, %s))",
                                ReservationContract.Colonnes.USER_ID,
                                ReservationContract.Colonnes.VOYAGE_ID,
                                ReservationContract.Colonnes.DATE_VOYAGE);
        db.execSQL(CREATE_RESERVATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String requeteSuppressionTable = String.format("drop table if exists %s ",
                ReservationContract.TABLE_NAME);
        db.execSQL(requeteSuppressionTable);
        onCreate(db);
    }

    // Méthode pour ajouter une nouvelle réservation à la base de données locale
    public long ajouterReservation(Reservation reservation) {
        long resultat = 0;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ReservationContract.Colonnes.USER_ID, reservation.getUserId());
            values.put(ReservationContract.Colonnes.VOYAGE_ID, reservation.getVoyageId());
            values.put(ReservationContract.Colonnes.DESTINATION, reservation.getDestination());
            values.put(ReservationContract.Colonnes.DATE_VOYAGE, reservation.getDateVoyage());
            values.put(ReservationContract.Colonnes.MONTANT_PAYE, reservation.getMontantPaye());
            values.put(ReservationContract.Colonnes.STATUT, reservation.getStatut());
            values.put(ReservationContract.Colonnes.NB_PERSONNES, reservation.getNbPersonnes());
            values.put(ReservationContract.Colonnes.IMAGE_URL, reservation.getImageUrl());

            return db.insert(ReservationContract.TABLE_NAME, null, values);
        } catch (Exception e) {
            return resultat;
        }
    }

    // Méthode pour récupérer toutes les réservations de la base de données locale
    public List<Reservation> chargerReservations(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Reservation> listeReservations = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + ReservationContract.TABLE_NAME + " WHERE " +
                ReservationContract.Colonnes.USER_ID + " = " + userId +
                " ORDER BY " + ReservationContract.Colonnes.DATE_VOYAGE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation();
                reservation.setUserId(cursor.getString(0));
                reservation.setVoyageId(cursor.getString(1));
                reservation.setDestination(cursor.getString(2));
                reservation.setDateVoyage(cursor.getString(3));
                reservation.setMontantPaye(Double.parseDouble(cursor.getString(4)));
                reservation.setStatut(cursor.getString(5));
                reservation.setNbPersonnes(Integer.parseInt(cursor.getString(6)));
                reservation.setImageUrl(cursor.getString(7));
                listeReservations.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listeReservations;
    }

    public int annulerReservation(String userId, String voyageId) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Empecher l'utilisateur de supprimé une réservation après la date du voyage
            ContentValues donnees = new ContentValues();
            donnees.put(ReservationContract.Colonnes.STATUT, "Annulée");
            String selection = ReservationContract.Colonnes.USER_ID + " = ? AND " +
                    ReservationContract.Colonnes.VOYAGE_ID + " = ? AND " +
                    ReservationContract.Colonnes.STATUT + " = ?";
            String[] selectionArgs = {userId, voyageId, "Confirmée"};
            return db.updateWithOnConflict(ReservationContract.TABLE_NAME, donnees, selection, selectionArgs, SQLiteDatabase.CONFLICT_ABORT);
        } catch (Exception e) {
            return -1;
        }
    }
}