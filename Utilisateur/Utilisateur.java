package Utilisateur;
public class Utilisateur {

    private final String[] nom ;
    private final String[] prenom ;
    private final double matricule ;
    private float reputation ;
// change encapsulation accordingly and without altering the means of security

    Utilisateur(String [] nom, String[] prenom, double matricule) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.reputation = 0;

    }
    private String typeUser;

    protected String[] getNom() {
        return this.nom;
    }
    protected String[] getPrenom() {
        return this.prenom;
    }
    protected double getMatricule() {
        return this.matricule;
    }
    protected float getReputation() {
        return this.reputation;
    }

    protected void setReputation(float rep) {
        this.reputation = rep;
    }



}
