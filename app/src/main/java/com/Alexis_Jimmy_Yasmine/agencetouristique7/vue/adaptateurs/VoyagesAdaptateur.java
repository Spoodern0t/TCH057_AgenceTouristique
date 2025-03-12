package com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.*;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import java.util.ConcurrentModificationException;

public class VoyagesAdaptateur {

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
            final TextView tvImage = (TextView) view.findViewById(R.id.XXX);
            final TextView tvDestination = (TextView) view.findViewById(R.id.XXX);
            final TextView tvResume = (TextView) view.findViewById(R.id.XXX);
            final TextView tvPrix = (TextView) view.findViewById(R.id.XXX);

            tvImage.setText(voyage.getImage_url());
            tvDestination.setText(voyage.getDestination());
            tvResume.setText(voyage.getDescription());
            tvPrix.setText(voyage.getPrix()+" $");
        }
        return view;
    }
}
