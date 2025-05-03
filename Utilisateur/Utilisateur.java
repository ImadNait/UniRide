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
    BufferedWriter writer = new BufferedWriter(new FileWriter(fpath));
    BufferedReader reader = new BufferedReader(new FileReader(fpath));


    // change encapsulation accordingly and without altering the means of security
    Utilisateur(String nom, String prenom, double matricule, float rep) throws IOException { // throws IOException is used to be able to use the fileWriter

        if (!checkNP(nom) || !checkNP(prenom)) {
            throw new IllegalArgumentException("the name should contain letters only"); // to get rid of the might not be init for final variables
        }
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        setReputation(rep);
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
        String line = reader.readLine();
        String[] user = line.split(",");
        while(line != null) {
            line = reader.readLine();
            user = line.split(",");
            showUser(user);
        }
    }
    /// overloaded the printUsers method to display a certain amount of users
    void printUsers(int i) throws IOException {
        String[] user = reader.readLine().split(",");
        while(i>0) {
            user = reader.readLine().split(",");
            showUser(user);
            i--;
        }
    }
    void findUser(double mat) throws IOException {
        String[] fmat = reader.readLine().split(",");
        boolean notfound = false;
        if (String.valueOf(mat) == null){System.out.println("mat should not be null");}
            else {
            while (String.valueOf(mat) != fmat[1]) {
                fmat = reader.readLine().split(",");
                    if (reader.readLine() == null){notfound = true; return;}
            }
            showUser(fmat);
        }
    }
    void showUser(String[] fmat) throws IOException {System.out.println(fmat[0]+"\n"+fmat[1]+"\n"+fmat[2]+"\n"+fmat[3]+"\n");}
    void showUser(String[] fmat,int i) throws IOException { /// overiding showUser to show a certain number of users passed through the variable i
        if (i==0){return;} else if (i>3 || i<0) {
            System.out.println("the number has to be between 1 and 4");
        }else{
            while(i>0){System.out.println(fmat[i]+"\n"); i--;}}
    }

}
