package Utilisateur;
public class Utilisateur {

    private final String nom ;
    private final String prenom ;
    private final double matricule ;
    private float reputation ;
// change encapsulation accordingly and without altering the means of security

    Utilisateur(String nom, String prenom, double matricule, float rep) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        setReputation(rep);
    }
    protected String getNom() {return this.nom;}
    protected String getPrenom() {return this.prenom;}
    protected double getMatricule() {return this.matricule;}
    protected float getReputation() {return this.reputation;}
    protected void setReputation(float rep) {
        if(checkRep(rep)){this.reputation = rep;}
        else{ System.out.println("Value entered out of range choose a value between 1 and 5");}
    }
    public boolean checkRep(float rep){
        if (rep >= 0 && rep <= 5) {return true;}else{return false;}
    }
    public boolean checkNP(Object temp) {
// i did object temp to implement oop by checking its type with instanceof it will be dealt with if it causes any problems
        if (temp instanceof String) {
            String name = (String) temp;
            return name.matches("^[\\p{L}]+$");}//check if name is in any language ^[\p{L}]+$ <- this means all letters in all languages upper & lower case
        return false;
    }
}
