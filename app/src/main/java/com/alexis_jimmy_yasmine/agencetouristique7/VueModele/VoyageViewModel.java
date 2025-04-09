package com.alexis_jimmy_yasmine.agencetouristique7.VueModele;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.VoyageDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class VoyageViewModel extends ViewModel {

    private final MutableLiveData<List<Voyage>> voyagesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Voyage> voyageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreurLiveData = new MutableLiveData<>();

    private final VoyageDao voyageDao;

    public VoyageViewModel() {
        this.voyageDao = VoyageDao.getInstance();
    }

    public LiveData<List<Voyage>> getVoyages() {
        return voyagesLiveData;
    }

    public LiveData<Voyage> getVoyage() {
        return voyageLiveData;
    }

    public LiveData<String> getErreur() {
        return erreurLiveData;
    }

    public void chargerVoyages(String filtreType, List<Float> filtreBudget, String filtreDestination, LocalDate filtreDate) {
        try {
            VoyageDao.getVoyages(filtreType, filtreBudget, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    List<Voyage> voyages = (List<Voyage>) data;

                    if (filtreDate == null && filtreDestination == null){
                        voyageDao.setVoyages(voyages);
                        voyagesLiveData.postValue(voyages);
                    } else {
                        //filtrer les voyages selon les paramètres choisis
                        List<Voyage> voyagesFiltres = new ArrayList<>();
                        for (Voyage voyage: voyages){

                            //si une destination est spécifiée, ignorer les autres
                            if (filtreDestination != null && !voyage.getDestination().contains(filtreDestination))
                                continue;

                            //si une date est spécifiée, ignorer les voyages sans vols suivant cette date.
                            if (filtreDate != null){
                                List<Voyage.Trip> trips = voyage.getTrips();
                                boolean tripDispo = false;
                                //regarder les dates de chaque vol du voyage en question
                                for (Voyage.Trip trip : trips) {
                                    if(LocalDate.parse(trip.getDate()).isAfter(filtreDate)){
                                        tripDispo = true;
                                        break;
                                    }
                                }
                                if (!tripDispo){
                                    continue;
                                }
                            }

                            //garder les voyages respectant tous les filtres
                            voyagesFiltres.add(voyage);
                        }
                        voyageDao.setVoyages(voyagesFiltres);
                        voyagesLiveData.postValue(voyagesFiltres);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des comptes");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }

    public void updateTripAvailability(Voyage voyage) {
        try {
            VoyageDao.updateTripAvailability(voyage, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    chargerVoyages(null, null, null, null);
                }

                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des comptes");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }

    public void regexVoyages(Pattern regexPattern) {
        List<Voyage> voyages =  voyagesLiveData.getValue();
        if (regexPattern != null && voyages != null) {
            List<Voyage> voyagesFiltres = new ArrayList<>();
            for (Voyage voyage : voyages) {
                // Tester le regex et ajouter la la liste filtrée si ca renvoie vrai
                String nomVoyage = StringUtils.stripAccents(voyage.getNomVoyage());
                boolean estMatch = regexPattern.matcher(nomVoyage).find();
                if (estMatch) voyagesFiltres.add(voyage);
            }
            voyagesLiveData.postValue(voyagesFiltres);
        } else {
            voyagesLiveData.postValue(voyages);
        }
    }

    public List<String> getVoyagesDestinations() {
        return voyageDao.getVoyagesDestinations();
    }

    public Voyage.Trip getTripOfVoyage(String id, String date) {
        List<Voyage> voyages = VoyageDao.getInstance().getVoyages();
        for (Voyage voyage: voyages) {
            if (voyage.getId().equals(id)) {
                for (Voyage.Trip trip: voyage.getTrips()) {
                    if (trip.getDate().equals(date)) return trip;
                }
            }
        }
        return null;
    }

    public Voyage getVoyageById(String voyageId) {
        List<Voyage> voyages = VoyageDao.getInstance().getVoyages();
        for (Voyage voyage: voyages) {
            if (voyage.getId().equals(voyageId)) return voyage;
        }
        return null;
    }
}
