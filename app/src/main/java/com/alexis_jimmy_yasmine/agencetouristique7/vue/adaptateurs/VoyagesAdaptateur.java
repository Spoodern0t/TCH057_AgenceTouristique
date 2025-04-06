package com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.*;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.squareup.picasso.Picasso;

public class VoyagesAdaptateur extends ArrayAdapter<Voyage> {

    private Voyage[] voyages;
    private Context contexte;
    private int viewResourceId;
    private Resources resources;

    public VoyagesAdaptateur(@NonNull Context context, int viewResourceId, @NonNull Voyage[] voyages) {
        super(context, viewResourceId, voyages);
        this.contexte = context;
        this.viewResourceId = viewResourceId;
        this.resources = contexte.getResources();
        this.voyages = voyages;
    }

    @Override
    public int getCount() {
        return this.voyages.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) contexte.getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(this.viewResourceId, parent, false);
        }

        final Voyage voyage = this.voyages[position];

        if (voyage != null) {
            final ImageView tvImage = (ImageView) view.findViewById(R.id.lvVoyageImage);
            final TextView tvNomVoyage = (TextView) view.findViewById(R.id.lvNomVoyage);
            final TextView tvDestination = (TextView) view.findViewById(R.id.lvVoyageDestination);
            final TextView tvResume = (TextView) view.findViewById(R.id.lvVoyageDescription);
            final TextView tvPrix = (TextView) view.findViewById(R.id.lvVoyagePrix);

            String imageUrlToLoad = voyage.getImageUrls().get(0);

            if (voyage.getImageUrls() != null && !voyage.getImageUrls().isEmpty()) {
                imageUrlToLoad = voyage.getImageUrls().get(0);
            }

            Picasso.get().load(imageUrlToLoad).into(tvImage);

            // Picasso set l'image
            Picasso.get().load(imageUrlToLoad).into(tvImage);
            tvNomVoyage.setText(voyage.getNomVoyage());
            tvDestination.setText(voyage.getDestination().replace("Destination : ", ""));
            tvResume.setText(voyage.getDescription());
            tvPrix.setText(String.format("%.2f", voyage.getPrix())+" $");
        }

        return view;
    }
}
