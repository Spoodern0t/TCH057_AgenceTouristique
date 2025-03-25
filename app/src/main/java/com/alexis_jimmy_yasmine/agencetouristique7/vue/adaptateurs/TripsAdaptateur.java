package com.alexis_jimmy_yasmine.agencetouristique7.vue.adaptateurs;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alexis_jimmy_yasmine.agencetouristique7.R;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;
import com.fasterxml.jackson.databind.ser.Serializers;

public class TripsAdaptateur extends BaseAdapter {

    private Voyage.Trip[] trips;
    private Context contexte;
    private int viewResourceId;
    private Resources resources;
    LayoutInflater inflater;

    public TripsAdaptateur(@NonNull Context context, int viewResourceId, @NonNull Voyage.Trip[] trips) {
        this.contexte = context;
        this.viewResourceId = viewResourceId;
        this.resources = contexte.getResources();
        this.trips = trips;
        inflater = (LayoutInflater.from(context.getApplicationContext()));
    }

    @Override
    public int getCount() {
        return this.trips.length;
    }

    @Override
    public Object getItem(int position) {
        return this.trips[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

        final Voyage.Trip trip = this.trips[position];

        if (trip != null) {
            final TextView dateDepart = (TextView) view.findViewById(R.id.tripDate);

            dateDepart.setText(trip.getDate());
        }

        return view;
    }
}
