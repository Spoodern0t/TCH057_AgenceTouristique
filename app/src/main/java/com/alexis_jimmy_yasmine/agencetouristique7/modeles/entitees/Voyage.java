package com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class Voyage {
    @JsonProperty("id")
    private int id;

    @JsonProperty("nom_voyage")
    private String nomVoyage;

    @JsonProperty("description")
    private String description;

    @JsonProperty("prix")
    private double prix;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("image_urls")
    private List<String> imageUrls;

    @JsonProperty("duree_jours")
    private int dureeJours;

    @JsonProperty("trips")
    private Trip[] trips;

    @JsonProperty("type_de_voyage")
    private String typeDeVoyage;

    @JsonProperty("activites_incluses")
    private String activitesIncluses;

    // Inner class Trip (no changes needed)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Trip {
        @JsonProperty("date")
        private String date;

        @JsonProperty("nb_places_disponibles")
        private int nbPlacesDisponibles;

        public String getDate() {
            return date;
        }

        public String getStrNbPlacesDisponibles() {
            return "Places disponibles : " + nbPlacesDisponibles;
        }

        public int getNbPlacesDisponibles() {
            return nbPlacesDisponibles;
        }

        public void setNbPlacesDisponibles(int nbPlacesDisponibles) {
            this.nbPlacesDisponibles = nbPlacesDisponibles;
        }
    }

    public Voyage() {
    }

    public Voyage(int id, String nomVoyage, String description, int prix, String destination, List<String> imageUrls, int dureeJours, Trip[] trips, String typeDeVoyage, String activitesIncluses) {
        this.id = id;
        this.nomVoyage = nomVoyage;
        this.description = description;
        this.prix = prix;
        this.destination = destination;
        this.imageUrls = imageUrls;
        this.dureeJours = dureeJours;
        this.trips = trips;
        this.typeDeVoyage = typeDeVoyage;
        this.activitesIncluses = activitesIncluses;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getDureeJours() {
        return dureeJours;
    }
    public void setDureeJours(int dureeJours) {
        this.dureeJours = dureeJours;
    }

    public String getNomVoyage() {
        return nomVoyage;
    }

    public void setNomVoyage(String nomVoyage) {
        this.nomVoyage = nomVoyage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getTypeDeVoyage() {
        return typeDeVoyage;
    }

    public void setTypeDeVoyage(String typeDeVoyage) {
        this.typeDeVoyage = typeDeVoyage;
    }

    public String getActivitesIncluses() {
        return activitesIncluses;
    }
    public void setActivitesIncluses(String activitesIncluses) {
        this.activitesIncluses = activitesIncluses;
    }

    public Trip[] getTrips() {
        return trips;
    }

    public void setTrips(Trip[] trips) {
        this.trips = trips;
    }
}