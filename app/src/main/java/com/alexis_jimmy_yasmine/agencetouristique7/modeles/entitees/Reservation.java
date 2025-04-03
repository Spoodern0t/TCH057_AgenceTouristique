package com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees;

public class Reservation {

    private int id;
    private String userId;
    private String voyageId;
    private String destination;
    private String dateVoyage;
    private double montantPaye;
    private String statut;
    private int nbPersonnes;
    private String imageUrl;

    public Reservation() {
    }

    public Reservation(int id, String userId, String voyagId, String destination, String dateVoyage, double montantPaye, String statut, int nbPersonnes, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.voyageId = voyagId;
        this.destination = destination;
        this.dateVoyage = dateVoyage;
        this.montantPaye = montantPaye;
        this.statut = statut;
        this.nbPersonnes = nbPersonnes;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateVoyage() {
        return dateVoyage;
    }

    public void setDateVoyage(String dateVoyage) {
        this.dateVoyage = dateVoyage;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(int nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(String voyageId) {
        this.voyageId = voyageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}