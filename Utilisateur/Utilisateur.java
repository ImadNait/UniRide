package Utilisateur;

import java.io.*;
import java.util.Scanner;

public class Utilisateur {

    private final String nom ;
    private final String prenom ;
    private final double matricule ;
    private float reputation ;
    protected static final String fpath = "users.txt";
    protected final String typeUser;
    // change encapsulation accordingly and without altering the means of security
    Utilisateur(String nom, String prenom, double matricule, float rep) throws IOException { // throws IOException is used to be able to use the fileWriter
            BufferedWriter writer = new BufferedWriter(new FileWriter(fpath,true));

        if (!checkNP(nom) || !checkNP(prenom)) {throw new IllegalArgumentException("The name should contain letters only");} // to get rid of the might not be init for final variables problem fixed
        if (checkDate(matricule)) {this.matricule = matricule;}
        else{throw new IllegalArgumentException("Invalid matricule year, Try again");}  // to get rid of the might not be init for final variables problem fixed
        this.nom = nom;
        this.prenom = prenom;
        this.typeUser = checkTypeUser();

        setReputation(rep);
        writer.write(String.format("%.0f",matricule) + "," + nom + "," + prenom + "," + rep + "\n");
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
    public boolean checkRep(float rep){return rep >= 0 && rep <= 5;}

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
    public static boolean findUser(double mat) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fpath));
        String user = reader.readLine();
        boolean found = false;

        while (user != null) {
            String[] fmat = user.split(",");
            if (fmat.length > 0 && Double.parseDouble(fmat[0]) == mat) {
                System.out.println("User with matricule " + mat + " found.");
                showUser(fmat);
                return true ;
            }
            fmat = user.split(",");
        }
        if (!found) {
            System.out.println("User with matricule " + mat + " not found.");
        }
        reader.close();
        return found;
    }
    /// overloading findUser to find up to four users
    void findUser(double mat1, double mat2) throws IOException {findUser(mat1);findUser(mat2);}
    void findUser(double mat1, double mat2 , double mat3) throws IOException {findUser(mat1);findUser(mat2);findUser(mat3);}
    void findUser(double mat1, double mat2 , double mat3, double mat4) throws IOException {findUser(mat1);findUser(mat2);findUser(mat3);findUser(mat4);}

    static void showUser(String[] fmat) throws IOException {System.out.println("Matricule: " + fmat[0] + "\nNom: " + fmat[1] + "\nPrenom: " + fmat[2] + "\nReputation: " + fmat[3] + "\n-------------------------------------");}
    void showUser(String[] fmat,int i) throws IOException { /// overloaded showUser to show a certain number of users which is contained in the variable i
        if (i==0){return;} else if (i>3 || i<0) {
            System.out.println("The number has to be between 1 and 4"); return;
        }else{
            while(i>0){System.out.println(fmat[i]+"\n"); i--;}}
    }

    boolean checkDate(double mat ){
        String matString = String.format("%.0f", mat);
        String year1 = matString.substring(0,2);
        String year2 = matString.substring(2,4);
        int y1 = Integer.parseInt(year1);
        int y2 = Integer.parseInt(year2);
        int ymat = Integer.parseInt(matString);

        if (matString.length() < 4) {
            return false;
        }
        return y1 > ymat || y2 > ymat;
    }

    String checkTypeUser(){
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Choose user type :\n");
            System.out.println("1 - ETUDIANT\n2 - ENSEIGNANT\n3 - ATS");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    return "ETUDIANT";
                case 2:
                    return "ENSEIGNANT";
                case 3:
                    return "ATS";
                default:
                    System.out.println("invalid input");
            }
        }
    }
}