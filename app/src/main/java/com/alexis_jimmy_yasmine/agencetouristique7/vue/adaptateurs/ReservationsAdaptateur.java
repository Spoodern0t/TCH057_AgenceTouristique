package com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;
import com.alexis_jimmy_yasmine.agencetouristique7.vue.activites.HistoriqueReservationsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReservationsAdaptateur extends ArrayAdapter<Reservation> {

    private Context contexte;
    private List<Reservation> reservations;
    private HistoriqueReservationsActivity historiqueActivity;

    public ReservationsAdaptateur(@NonNull Context context, int resource, @NonNull List<Reservation> reservations, HistoriqueReservationsActivity activity) {
        super(context, resource, reservations);
        this.contexte = context;
        this.reservations = reservations;
        this.historiqueActivity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(contexte);
        View view = inflater.inflate(R.layout.layout_reservation_historique_item, parent, false);

        ImageView reservationImageView = view.findViewById(R.id.iv_reservation_image);
        TextView destinationTextView = view.findViewById(R.id.tv_reservation_destination);
        TextView dateTextView = view.findViewById(R.id.tv_reservation_date);
        TextView prixTextView = view.findViewById(R.id.tv_reservation_prix);
        TextView nbPersonnesTextView = view.findViewById(R.id.tv_reservation_nb_personnes);
        TextView statutTextView = view.findViewById(R.id.tv_reservation_statut);
        ImageButton deleteButton = view.findViewById(R.id.button_delete_reservation);

        Reservation reservation = getItem(position);

        if (reservation != null) {
            Picasso.get().load(reservation.getImageUrl()).placeholder(R.drawable.logo).error(R.drawable.logo).into(reservationImageView);
            destinationTextView.setText("Destination: " + reservation.getDestination());
            dateTextView.setText("Date du voyage: " + reservation.getDateVoyage());
            prixTextView.setText("Montant payé: " + String.format("%.2f $", reservation.getMontantPaye()));
            nbPersonnesTextView.setText("Nombre de personnes: " + reservation.getNbPersonnes());
            statutTextView.setText("Statut: " + reservation.getStatut());

            deleteButton.setOnClickListener(v -> {
                historiqueActivity.annulerReservation(reservation);
            });
        }

        return view;
    }
}