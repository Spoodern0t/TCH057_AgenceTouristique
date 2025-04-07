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
                        TauxContract.Colonnes.USER_ID + " text," +
                        TauxContract.Colonnes.VOYAGE_ID + " text," +
                        TauxContract.Colonnes.DESTINATION + " text," +
                        TauxContract.Colonnes.DATE_VOYAGE + " text," +
                        TauxContract.Colonnes.MONTANT_PAYE + " double," +
                        TauxContract.Colonnes.STATUT + " text," +
                        TauxContract.Colonnes.NB_PERSONNES + " text," +
                        TauxContract.Colonnes.IMAGE_URL + " integer," +
                        String.format("PRIMARY KEY (%s, %s, %s))",
                                TauxContract.Colonnes.USER_ID,
                                TauxContract.Colonnes.VOYAGE_ID,
                                TauxContract.Colonnes.DATE_VOYAGE);
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
    public long ajouterReservation(Reservation reservation) {
        long resultat = 0;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(TauxContract.Colonnes.USER_ID, reservation.getUserId());
            values.put(TauxContract.Colonnes.VOYAGE_ID, reservation.getVoyageId());
            values.put(TauxContract.Colonnes.DESTINATION, reservation.getDestination());
            values.put(TauxContract.Colonnes.DATE_VOYAGE, reservation.getDateVoyage());
            values.put(TauxContract.Colonnes.MONTANT_PAYE, reservation.getMontantPaye());
            values.put(TauxContract.Colonnes.STATUT, reservation.getStatut());
            values.put(TauxContract.Colonnes.NB_PERSONNES, reservation.getNbPersonnes());
            values.put(TauxContract.Colonnes.IMAGE_URL, reservation.getImageUrl());

            return db.insert(TauxContract.TABLE_NAME, null, values);
        } catch (Exception e) {
            return resultat;
        }
    }

    // Méthode pour récupérer toutes les réservations de la base de données locale
    public List<Reservation> chargerReservations(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Reservation> listeReservations = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TauxContract.TABLE_NAME + " WHERE " +
                TauxContract.Colonnes.USER_ID + " = " + userId +
                " ORDER BY " + TauxContract.Colonnes.DATE_VOYAGE;

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
        return listeReservations;
    }

    public int annulerReservation(String userId, String voyageId) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Empecher l'utilisateur de supprimé une réservation après la date du voyage
            ContentValues donnees = new ContentValues();
            donnees.put(TauxContract.Colonnes.STATUT, "Annulée");
            String selection = TauxContract.Colonnes.USER_ID + " = ? AND " +
                    TauxContract.Colonnes.VOYAGE_ID + " = ? AND " +
                    TauxContract.Colonnes.STATUT + " = ?";
            String[] selectionArgs = {userId, voyageId, "Confirmée"};
            return db.updateWithOnConflict(TauxContract.TABLE_NAME, donnees, selection, selectionArgs, SQLiteDatabase.CONFLICT_ABORT);
        } catch (Exception e) {
            return -1;
        }
    }
}