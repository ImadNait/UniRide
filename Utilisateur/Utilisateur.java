package Utilisateur;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;


public class Utilisateur {

    private final String nom ;
    private final String prenom ;
    private final double matricule ;
    private float reputation ;
    private static final String fpath = "users.txt";




    // change encapsulation accordingly and without altering the means of security
    Utilisateur(String nom, String prenom, double matricule, float rep) throws IOException { // throws IOException is used to be able to use the fileWriter

        if (!checkNP(nom) || !checkNP(prenom)) {
            throw new IllegalArgumentException("the name should contain letters only"); // to get rid of the might not be init for final variables
        }
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        setReputation(rep);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fpath,true));
        writer.write(matricule + "," + nom + "," + prenom + "," + rep + "\n");
        writer.close();
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
        return rep >= 0 && rep <= 5;
    }
    public boolean checkNP(Object temp) {
// i did object temp to implement oop by checking its type with instanceof it will be dealt with if it causes any problems
        if (temp instanceof String) {
            String name = (String) temp;
            return name.matches("^[\\p{L}]+$");}//check if name is in any language ^[\p{L}]+$ <- this means all letters in all languages upper & lower case
        return false;
    }
    void printUsers() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fpath));
        String line = reader.readLine();
        String[] user = line.split(",");
        while(line != null) {
            line = reader.readLine();
            user = line.split(",");
            showUser(user);
        }
        reader.close();
    }
    /// overloaded the printUsers method to display a certain amount of users
    void printUsers(int i) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fpath));
        String[] user = reader.readLine().split(",");
        while(i>0) {
            user = reader.readLine().split(",");
            showUser(user);
            i--;
        }
        reader.close();
    }
    void findUser(double mat) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fpath));
        String user = reader.readLine();
        boolean found = false;

        while (user != null) {
            String[] fmat = user.split(",");
            if (fmat.length > 0 && Double.parseDouble(fmat[0]) == mat) {
                showUser(fmat);
                break;
            }
        }
        if (!found) {
            System.out.println("User with matricule " + mat + " not found.");
        }
        reader.close();
    }
    /// overloading findUser to find up to four users
    void findUser(double mat1, double mat2) throws IOException {findUser(mat1);findUser(mat2);}
    void findUser(double mat1, double mat2 , double mat3) throws IOException {findUser(mat1);findUser(mat2);findUser(mat3);}
    void findUser(double mat1, double mat2 , double mat3, double mat4) throws IOException {findUser(mat1);findUser(mat2);findUser(mat3);findUser(mat4);}

    void showUser(String[] fmat) throws IOException {System.out.println("Matricule: " + fmat[0] + "\nNom: " + fmat[1] + "\nPrenom: " + fmat[2] + "\nReputation: " + fmat[3] + "\n-------------------------------------");}
    void showUser(String[] fmat,int i) throws IOException { /// overloaded showUser to show a certain number of users which is contained in the variable i
        if (i==0){return;} else if (i>3 || i<0) {
            System.out.println("the number has to be between 1 and 4"); return;
        }else{
            while(i>0){System.out.println(fmat[i]+"\n"); i--;}}
    }

}
