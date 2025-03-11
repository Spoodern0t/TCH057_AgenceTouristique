package com.alexis_jimmy_yasmine.agencetouristique7.modeles;
//
import java.util.List;

public class Voyage {
    private int id;
    private String nom_voyage;
    private String description;
    private int prix;
    private String destination;
    private String image_url;
    private int duree_jours;
    private List<Trip> trips;
    private String type_de_voyage;
    private String activites_incluses;

    public static class Trip {
        private String date;
        private int nb_places_disponibles;

        public Trip() {
         }

        public Trip(String date, int nb_places_disponibles) {
            this.date = date;
            this.nb_places_disponibles = nb_places_disponibles;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getNb_places_disponibles() {
            return nb_places_disponibles;
        }

        public void setNb_places_disponibles(int nb_places_disponibles) {
            this.nb_places_disponibles = nb_places_disponibles;
        }
    }


    public Voyage() {
     }

    public Voyage(int id, String nom_voyage, String description, int prix, String destination, String image_url, int duree_jours, List<Trip> trips, String type_de_voyage, String activites_incluses) {
        this.id = id;
        this.nom_voyage = nom_voyage;
        this.description = description;
        this.prix = prix;
        this.destination = destination;
        this.image_url = image_url;
        this.duree_jours = duree_jours;
        this.trips = trips;
        this.type_de_voyage = type_de_voyage;
        this.activites_incluses = activites_incluses;
    }

    // Getters and Setters for Voyage
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_voyage() {
        return nom_voyage;
    }

    public void setNom_voyage(String nom_voyage) {
        this.nom_voyage = nom_voyage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getDuree_jours() {
        return duree_jours;
    }

    public void setDuree_jours(int duree_jours) {
        this.duree_jours = duree_jours;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public String getType_de_voyage() {
        return type_de_voyage;
    }

    public void setType_de_voyage(String type_de_voyage) {
        this.type_de_voyage = type_de_voyage;
    }

    public String getActivites_incluses() {
        return activites_incluses;
    }

    public void setActivites_incluses(String activites_incluses) {
        this.activites_incluses = activites_incluses;
    }
}