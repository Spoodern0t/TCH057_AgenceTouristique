package com.alexis_jimmy_yasmine.agencetouristique7.VueModele;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ClientDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ReservationDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import java.util.List;

public class ReservationViewModel extends ViewModel {

    private final MutableLiveData<List<Reservation>> reservationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Reservation> reservationLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreurLiveData = new MutableLiveData<>();

    private final ReservationDao reservationDao;

    public ReservationViewModel() {
        this.reservationDao = ReservationDao.getInstance();
    }

    public void setConnexionBD(Context contexte) {
        ReservationDao.connexionBD(contexte);
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservationsLiveData;
    }

    // Une seule instance
    public LiveData<Reservation> getReservation() {
        return reservationLiveData;
    }

    public LiveData<String> getErreur() {
        return erreurLiveData;
    }

    public void chargerReservations() {
        reservationDao.chargerReservations(ClientDao.getInstance().getClient().getId(), new EcouteurDeDonnees() {
            @Override
            public void onDataLoaded(Object data) {
                reservationsLiveData.postValue((List<Reservation>) data);
            }

            @Override
            public void onError(String errorMessage) {
                erreurLiveData.postValue(errorMessage);
            }
        });
    }

    public void ajouterReservation(Voyage voyage, Voyage.Trip tripChoisi, int nbPlacesReservees) {
        reservationDao.ajouterReservation(voyage, tripChoisi, nbPlacesReservees, new EcouteurDeDonnees() {
            @Override
            public void onDataLoaded(Object data) {
                reservationLiveData.postValue((Reservation) data);
            }

            @Override
            public void onError(String errorMessage) {
                erreurLiveData.postValue(errorMessage);
            }
        });
    }

    public void annulerReservation(Reservation reservation) {
        reservationDao.supprimerReservation(reservation, new EcouteurDeDonnees() {
            @Override
            public void onDataLoaded(Object data) {
                chargerReservations();
                reservationLiveData.postValue((Reservation) data);
            }

            @Override
            public void onError(String errorMessage) {
                erreurLiveData.postValue(errorMessage);
            }
        });
    }
}
