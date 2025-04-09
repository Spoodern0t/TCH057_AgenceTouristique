package com.alexis_jimmy_yasmine.agencetouristique7.modeles.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexis_jimmy_yasmine.agencetouristique7.VueModele.EcouteurDeDonnees;
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
    public void ajouterReservation(Reservation reservation, EcouteurDeDonnees ecouteurDeDonnees) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ReservationContract.Colonnes.USER_ID, "s" + reservation.getUserId());
            values.put(ReservationContract.Colonnes.VOYAGE_ID, "s" + reservation.getVoyageId());
            values.put(ReservationContract.Colonnes.DESTINATION, reservation.getDestination());
            values.put(ReservationContract.Colonnes.DATE_VOYAGE, reservation.getDateVoyage());
            values.put(ReservationContract.Colonnes.MONTANT_PAYE, reservation.getMontantPaye());
            values.put(ReservationContract.Colonnes.STATUT, reservation.getStatut());
            values.put(ReservationContract.Colonnes.NB_PERSONNES, reservation.getNbPersonnes());
            values.put(ReservationContract.Colonnes.IMAGE_URL, reservation.getImageUrl());

            long resultat = db.insert(ReservationContract.TABLE_NAME, null, values);
            if (resultat > 0) {
                ecouteurDeDonnees.onDataLoaded(reservation);
            } else {
                ecouteurDeDonnees.onError("Vous avez déjà réservé ce voyage.");
            }
        } catch (Exception e) {
            ecouteurDeDonnees.onError("Une erreur s'est produite lors de la réservation");
        }
    }

    // Méthode pour récupérer toutes les réservations de la base de données locale
    public void chargerReservations(String userId, EcouteurDeDonnees ecouteurDeDonnees) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            List<Reservation> listeReservations = new ArrayList<>();
            String selectQuery = "SELECT  * FROM " + ReservationContract.TABLE_NAME + " WHERE " +
                    ReservationContract.Colonnes.USER_ID + " = ? " +
                    "ORDER BY " + ReservationContract.Colonnes.DATE_VOYAGE;
            String[] selectionArgs = {"s" + userId};
            Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

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
            ecouteurDeDonnees.onDataLoaded(listeReservations);
        } catch (Exception e) {
            ecouteurDeDonnees.onError("Une erreur s'est produite pendant le chargement des réservations.");
        }
    }

    public void annulerReservation(Reservation reservation, EcouteurDeDonnees ecouteurDeDonnees) {
        String userId = reservation.getUserId();
        String voyageId = reservation.getVoyageId();
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Empecher l'utilisateur de supprimé une réservation après la date du voyage
            ContentValues donnees = new ContentValues();
            donnees.put(ReservationContract.Colonnes.STATUT, "Annulée");
            String selection = ReservationContract.Colonnes.USER_ID + " = ? AND " +
                    ReservationContract.Colonnes.VOYAGE_ID + " = ? AND " +
                    ReservationContract.Colonnes.STATUT + " = ?";
            String[] selectionArgs = {"s" + userId, "s" + voyageId, "Confirmée"};
            long resultat = db.updateWithOnConflict(ReservationContract.TABLE_NAME, donnees, selection, selectionArgs, SQLiteDatabase.CONFLICT_ABORT);
            if (resultat > 0) {
                ecouteurDeDonnees.onDataLoaded(reservation);
            } else {
                ecouteurDeDonnees.onError("Vous avez déjà annulé ce voyage.");
            }
        } catch (Exception e) {
            ecouteurDeDonnees.onError("Une erreur s'est produite lors de l'annulation");
        }
    }
}