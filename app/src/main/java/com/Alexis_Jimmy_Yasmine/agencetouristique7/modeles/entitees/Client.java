package com.alexis_jimmy_yasmine.agencetouristique7.modeles.entitees;

public class Client {

    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private int age;
    private String telephone;
    private String adresse;

    public Client() {}

     public Client(String id, String nom, String prenom, String email, String mdp, int age, String telephone, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.age = age;
        this.telephone = telephone;
        this.adresse = adresse;
    }

     public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return mdp;
    }

    public int getAge() {
        return age;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAdresse() {
        return adresse;
    }
}