package Utilisateur;

public class Etudiant extends Utilisateur   {
    private String specialite;
    private int anneeadmission;
    private String faculte;

    public Etudiant(String nom, String prenom, double matricule,float repuation, int anneeadmission, String faculte, String specialite) {
        super(nom, prenom, matricule, repuation);
        this.anneeadmission = anneeadmission;
        this.faculte = faculte;
        this.specialite = specialite;
    }
    protected String getSpecialite() {return this.specialite;}
    protected void setSpecialite(String specialite) {this.specialite = specialite;}
    protected void setAnneeAdmission(int anneeadmission) {this.anneeadmission = anneeadmission;}
    protected int getAnneeAdmission() {return this.anneeadmission;}
    protected void setFaculte(String faculte) {this.faculte = faculte;}
    protected String getFaculte() {return this.faculte;}


}
