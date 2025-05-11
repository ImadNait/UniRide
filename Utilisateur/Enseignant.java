package Utilisateur;

import java.io.IOException;

public class Enseignant extends Utilisateur {
    private int anneerecrutement;
    private String faculte;

    public Enseignant(String nom, String prenom, double matricule, float repuation,
                      int anneerecrutement, String faculte) throws IOException {
        super(nom, prenom, matricule, repuation);
        this.anneerecrutement = anneerecrutement;
        this.faculte = faculte;
    }
    protected int getAnneeRecrutement() {return this.anneerecrutement;}
    protected void setAnneeRecrutement(int anneerecrutement) {this.anneerecrutement = anneerecrutement;}
    protected String getFaculte() {return this.faculte;}
    protected void setFaculte(String faculte) {this.faculte = faculte;}
}
