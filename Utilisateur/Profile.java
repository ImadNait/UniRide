package Utilisateur;

import java.util.List;

public class Profile extends Utilisateur{
    private enum status { Passager, Chauffeur }
    private status status;
    private List<String> itineraire;
    private List<String> preferences;
    private enum horaire { Journalier, Hebdomadaire, Quotidien };
    private horaire horaire;
    private enum type { allerRetour, aller, retour };
    private type type;
    private float moyPass = 0;
    private float moyChauff = 0;
    private int nbPass = 0;
    private int nbChauff = 0;

    public status getStatus() {
        return status;
    }
    public void setStatus(status status) {
        this.status = status;
    }
    public List<String> getItineraire() {
        return itineraire;
    }
    public void setItineraire(List<String> itineraire) {
        this.itineraire = itineraire;
    }
    public List<String> getPreferences() {
        return preferences;
    }
    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }
    public horaire getHoraire() {
        return horaire;
    }
    public void setHoraire(horaire horaire) {
        this.horaire = horaire;
    }
    public type  getType() {
        return type;
    }
    public void changeType(type type) {
        this.type = type;
    }
    public void switchStatus() {
        this.status = (this.status == status.Passager) ? status.Chauffeur : status.Passager;
    }











    public Profile(String nom, String prenom, double matricule) {
        super(nom, prenom, matricule);
    }
}
