package Utilisateur;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;


public class Utilisateur {

    private final String nom;
    private final String prenom;
    private final double matricule;
    private float reputation;
    private static final String fpath = "users.txt";
    BufferedWriter writer = new BufferedWriter(new FileWriter(fpath, true)); // true = append mode
    BufferedReader reader = new BufferedReader(new FileReader(fpath));

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
        if (checkRep(rep)) {
            this.reputation = rep;
        } else {
            System.out.println("Value entered out of range choose a value between 1 and 5");
        }
    }

    public boolean checkRep(float rep) {
        return rep >= 0 && rep <= 5;
    }

    public boolean checkNP(Object temp) {
        if (temp instanceof String) {
            String name = (String) temp;
            return name.matches("^[\\p{L}]+$");
        }
        return false;
    }

    void printUsers() throws IOException {
        String line = reader.readLine();
        String[] user = line.split(",");
        while (line != null) {
            line = reader.readLine();
            user = line.split(",");
            showUser(user);
        }
    }

    void printUsers(int i) throws IOException {
        String[] user = reader.readLine().split(",");
        while (i > 0) {
            user = reader.readLine().split(",");
            showUser(user);
            i--;
        }
    }

    void findUser(double mat) throws IOException {
        String[] fmat = reader.readLine().split(",");
        boolean notfound = false;
        if (String.valueOf(mat) == null) {
            System.out.println("mat should not be null");
        } else {
            while (String.valueOf(mat) != fmat[1]) {
                fmat = reader.readLine().split(",");
                if (reader.readLine() == null) {
                    notfound = true;
                    return;
                }
            }
            showUser(fmat);
        }
    }

    void showUser(String[] fmat) throws IOException {
        System.out.println(fmat[0] + "\n" + fmat[1] + "\n" + fmat[2] + "\n" + fmat[3] + "\n");
    }

    void showUser(String[] fmat, int i) throws IOException {
        if (i == 0) {
            return;
        } else if (i > 3 || i < 0) {
            System.out.println("the number has to be between 1 and 4");
        } else {
            while (i > 0) {
                System.out.println(fmat[i] + "\n");
                i--;
            }
        }
    }

    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Menu ====");
            System.out.println("1. Create new user");
            System.out.println("2. View all users");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createUser(sc);
                    break;
                case 2:
                    viewAllUsers();
                    break;
                case 3:
                    System.out.println("Exiting program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void createUser(Scanner sc) throws IOException {
        System.out.println("\n=== Create New User ===");
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Prenom: ");
        String prenom = sc.nextLine();
        System.out.print("Matricule: ");
        double matricule = sc.nextDouble();
        System.out.print("Reputation: ");
        float reputation = sc.nextFloat();

        try {
            Utilisateur usr = new Utilisateur(name, prenom, matricule, reputation);
            System.out.println(usr.getPrenom() + " has been created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private static void viewAllUsers() {
        System.out.println("\n=== All Users ===");
        try (BufferedReader reader = new BufferedReader(new FileReader(fpath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                System.out.println("Matricule: " + user[0]);
                System.out.println("Nom: " + user[1]);
                System.out.println("Prenom: " + user[2]);
                System.out.println("Reputation: " + user[3]);
                System.out.println("-------------------");
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
    }

}
