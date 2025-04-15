package com.alexis_jimmy_yasmine.agencetouristique7.modeles.SQLite;

public class ReservationContract {
    public static final String DATABASE_NAME = "AgenceTouristiqueDB";
    public static final int DATABASE_VERSION = 4;
    public static final String TABLE_NAME = "reservations";

    public static class Colonnes {
        public static final String USER_ID = "user_id";
        public static final String VOYAGE_ID = "voyage_id";
        public static final String DESTINATION = "destination";
        public static final String DATE_VOYAGE = "date_voyage";
        public static final String MONTANT_PAYE = "montant_paye";
        public static final String STATUT = "statut";
        public static final String NB_PERSONNES = "nb_personnes";
        public static final String IMAGE_URL = "image_url";
    }
}
