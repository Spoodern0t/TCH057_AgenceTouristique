package com.alexis_jimmy_yasmine.agencetouristique7.modeles;

public class ModeleManager {

    private static Modele modele = null;

    /**
     * Créer, s'il ne l'est pas, et retourne le modele
     * @return Le modele
     */
    public static Modele getModele() {
        if (modele == null)
            modele = new Modele();
        return modele;
    }
    /**
     * Détruit le modele initialisé s'il n'est pas nulle
     * @return  true si le modele a bien été détruit ou false si le modele n'a pas été détruit
     */
    public static boolean detruire() {
        boolean detruit = true;

        if (modele != null)
            modele = null;
        else
            detruit = false;

        return detruit;
    }
}
