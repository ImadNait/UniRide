package Utilisateur;
public class Utilisateur {

    private final String nom ;
    private final String prenom ;
    private final double matricule ;
    private float reputation ;
// change encapsulation accordingly and without altering the means of security

    Utilisateur(String nom, String prenom, double matricule, float rep) {
       //in main when added to main delete here
        // if (nom instanceof String && nom.matches("^[a-zA-Z]+$")) { // chooses only the values that contain just letters
            this.nom = nom;
        // }else{
         //   System.out.println("Invalid type of input nom has to be a String with only letters");
        // }
       // if (prenom instanceof String && prenom.matches("^[a-zA-Z]+$")) { // chooses only the values that contain just letters
            this.prenom = prenom;
        // }else{
         //   System.out.println("Invalid type of input prenom has to be a String with only letters");
        //}

        this.matricule = matricule;
        if (rep >= 0 && rep <= 6) {this.reputation = rep;}
        else{ System.out.println("Value entered out of range choose a value between 1 and 5");}

    }

    protected String getNom() {
        return this.nom;
    }
    protected String getPrenom() {
        return this.prenom;
    }
    protected double getMatricule() {
        return this.matricule;
    }
    protected float getReputation() {
        return this.reputation;
    }

    protected void setReputation(float rep) {
        if (rep >= 0 && rep <= 5) {this.reputation = rep;}
        else{ System.out.println("Value entered out of range choose a value between 1 and 5");}
    }



}
