package com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;

import java.util.List;

public class ReservationsAdaptateur extends ArrayAdapter<Reservation> {

    private Context contexte;
    private List<Reservation> reservations;

    public ReservationsAdaptateur(@NonNull Context context, int resource, @NonNull List<Reservation> reservations) {
        super(context, resource, reservations);
        this.contexte = context;
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(contexte);
        View view = inflater.inflate(R.layout.layout_reservation_historique_item, parent, false);

        TextView destinationTextView = view.findViewById(R.id.tv_reservation_destination);
        TextView dateTextView = view.findViewById(R.id.tv_reservation_date);
        TextView prixTextView = view.findViewById(R.id.tv_reservation_prix);
        TextView nbPersonnesTextView = view.findViewById(R.id.tv_reservation_nb_personnes);
        TextView statutTextView = view.findViewById(R.id.tv_reservation_statut);

        Reservation reservation = getItem(position);

        if (reservation != null) {
            destinationTextView.setText("Destination: " + reservation.getDestination());
            dateTextView.setText("Date du voyage: " + reservation.getDateVoyage());
            prixTextView.setText("Montant payé: " + String.format("%.2f $", reservation.getMontantPaye()));
            nbPersonnesTextView.setText("Nombre de personnes: " + String.valueOf(reservation.getNbPersonnes()));
            statutTextView.setText("Statut: " + reservation.getStatut());
        }

        return view;
    }
}