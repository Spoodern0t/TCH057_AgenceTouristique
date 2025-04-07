package com.alexis_jimmy_yasmine.agencetouristique7.VueModele;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis_jimmy_yasmine.agencetouristique7.modeles.Modele;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.ModeleManager;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ClientDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.ReservationDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.dao.VoyageDao;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Client;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Reservation;
import com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees.Voyage;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AgenceViewModel extends ViewModel {

    private final MutableLiveData<Boolean> connexionLiveData = new MutableLiveData<>();
    private final MutableLiveData<Client> inscriptionLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Voyage>> voyagesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Reservation>> reservationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreurLiveData = new MutableLiveData<>();

    private final Modele modele;

    public AgenceViewModel() {
        this.modele = ModeleManager.getModele();
    }

    public LiveData<Boolean> getConnexion() {
        return connexionLiveData;
    }

    public LiveData<Client> getInscription() {
        return inscriptionLiveData;
    }

    public LiveData<List<Voyage>> getVoyages() {
        return voyagesLiveData;
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservationsLiveData;
    }

    public LiveData<String> getErreur() {
        return erreurLiveData;
    }

    public void postConnexion(String email, String mdp) {
        try {
            ClientDao.postConnexion(email, mdp, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    modele.setClient((Client) data);
                    connexionLiveData.postValue(true);
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

    public void postClient(Client client) {
        try {
            ClientDao.postClient(client, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    inscriptionLiveData.postValue(client);
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

    public void chargerVoyages(String filtreType, List<Float> filtreBudget, String filtreDestination, LocalDate filtreDate) {
        try {
            //FIXME: Pourquoi ne pas filtrer destination et date dans VoyageDAO, comme pour type et budget? ~AF
            VoyageDao.getVoyages(filtreType, filtreBudget, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    List<Voyage> voyages = (List<Voyage>) data;

                    if (filtreDate == null && filtreDestination == null){
                        modele.setVoyages(voyages);
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
                        modele.setVoyages(voyagesFiltres);
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

    public Voyage getVoyageById(String id) {
        return modele.getVoyageById(id);
    }

    public void updateTripAvailability(Voyage voyage) {

        int position = modele.getVoyagePosition(voyage.getId());
        try {
            VoyageDao.updateTripAvailability(voyage, position, new EcouteurDeDonnees() {
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

    public void chargerReservations(Context contexte) {
        ReservationDao reservationDao = new ReservationDao(contexte);
        List<Reservation> reservations = reservationDao.chargerReservations(modele.getClient().getId());
        reservationsLiveData.postValue(reservations);
    }

    public long ajouterReservation(Context contexte, Voyage voyage, Voyage.Trip tripChoisi, int nbPlacesReservees) {

        Reservation reservation = new Reservation(modele.getClient().getId(),
                voyage.getId(), voyage.getDestination(),
                tripChoisi.getDate(), voyage.getPrix() * nbPlacesReservees,
                "Confirmée", nbPlacesReservees, voyage.getImageUrls().get(0));

        ReservationDao reservationDao = new ReservationDao(contexte);
        return reservationDao.ajouterReservation(reservation);
    }

    public int annulerReservation(Context contexte, Reservation reservationToDelete) {
        ReservationDao reservationDao = new ReservationDao(contexte);
        String userId = reservationToDelete.getUserId();
        String voyageId = reservationToDelete.getVoyageId();
        int resultat = reservationDao.supprimerReservation(userId, voyageId);

        String voyageDate = reservationToDelete.getDateVoyage();
        int nbPlacesCancelees = reservationToDelete.getNbPersonnes();

        if (resultat >= 1) {
            Voyage voyage = getVoyageById(voyageId);
            for (Voyage.Trip trip: voyage.getTrips()) {
                if (trip.getDate().equals(voyageDate)) {
                    trip.augmenterNbPlaces(nbPlacesCancelees);
                    break;
                }
            }
            updateTripAvailability(voyage);
        }
        return resultat;
    }

    public void regexVoyages(Pattern regexPattern) {
        List<Voyage> voyages = modele.getVoyages();
        if (!(regexPattern == null)) {
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
        return modele.getVoyagesDestinations();
    }
}
