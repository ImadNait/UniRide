package Utilisateur;

public class Ats extends Utilisateur {
    private int anneeRecrutement;
    private String service;

    public Ats(String nom, String prenom, double matricule, float repuation,
               int anneeRecrutement, String service) {
        super(nom, prenom, matricule, repuation);
        this.anneeRecrutement = anneeRecrutement;
        this.service = service;
    }
    protected int getAnneeRecrutement() {return this.anneeRecrutement;}
    protected void setAnneeRecrutement(int anneeRecrutement) {this.anneeRecrutement = anneeRecrutement;}
    protected String getService() {return this.service;}
    protected void setService(String service) {this.service = service;}
}
