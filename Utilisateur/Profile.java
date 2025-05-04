package Utilisateur;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Profile extends Utilisateur{
    private enum status { Passager, Chauffeur }
    private static status status;
    private List<String> itineraire;
    private List<String> preferences;
    private enum Horaire { Journalier, Hebdomadaire, Quotidien }
    private static Horaire horaire;
    private enum Type { allerRetour, aller, retour }
    private static Type type;
    private float moyPass = 0;
    private float moyChauff = 0;
    private int nbPass = 0;
    private int nbChauff = 0;
    private static final String FICHIER_DEMANDES = "demands.txt";

    public status getStatus() {
        return status;
    }

    public void setStatus(status status) {
        this.status = status;
    }

    public List<String> getItineraire() {
        return itineraire;
    }

    public void setItineraire(List<String> itineraire) {
        this.itineraire = itineraire;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public Horaire getHoraire() {
        return horaire;
    }

    public void changeHoraire(Horaire horaire) {
        this.horaire = horaire;
    }

    public Type  getType() {
        return type;
    }

    public void changeType(Type type) {
        this.type = type;
    }

    public void switchStatus() {
        this.status = (this.status == status.Passager) ? status.Chauffeur : status.Passager;
    }

    public float calculMoyenne() {
        if (nbPass == 0 && nbChauff == 0) return 0;
        if (status == status.Passager) return moyPass / nbChauff;
        if (status == status.Chauffeur) return moyChauff / nbPass;
        return (moyPass + moyChauff) / (nbPass + nbChauff);
    }

    public void refreshMoyenne(float rating) {
        if (status == status.Passager) {
            moyPass += rating;
            nbPass++;
        } else {
            moyChauff += rating;
            nbChauff++;
        }
        setReputation(calculMoyenne());
    }

    public Profile(String nom, String prenom, double matricule, float rep, status status, List<String> itineraire, List<String> preferences, Horaire horaire, Type type) throws IOException {
        super(nom, prenom, matricule, rep);
        this.status = status;
        this.itineraire = itineraire;
        this.preferences = preferences;
        this.horaire = horaire;
        this.type = type;
    }

    public void ajouterDemande() throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== Nouvelle demande de course ===");
        System.out.print("Point de départ: ");
        String depart = sc.nextLine();
        System.out.print("Point d'arrivée: ");
        String arrivee = sc.nextLine();
        System.out.print("Heure de départ (HH:MM): ");
        String heure = sc.nextLine();

        String demande = String.format(
                "Demande de: %s %s (Mat: %.0f)\n" +
                        "Statut: %s\n" +
                        "Itinéraire: %s -> %s\n" +
                        "Préférences: %s\n" +
                        "Disponibilité: %s\n" +
                        "Type: %s\n" +
                        "Heure demandée: %s\n" +
                        "Réputation: %.1f\n" +
                        "----------------------------\n",
                getNom(), getPrenom(), getMatricule(),
                status.toString(),
                depart, arrivee,
                String.join(", ", preferences),
                horaire.toString(),
                type.toString(),
                heure,
                getReputation()
        );

        Files.write(Paths.get(FICHIER_DEMANDES),
                demande.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);

        System.out.println("\nDemande enregistrée avec succès !");
    }

    public static void afficherDemandes() {
        try {
            System.out.println("\n=== Liste des demandes ===");

            if (!Files.exists(Paths.get(FICHIER_DEMANDES))) {
                System.out.println("Aucune demande pour le moment.");
                return;
            }

            List<String> lignes = Files.readAllLines(Paths.get(FICHIER_DEMANDES));

            if (lignes.isEmpty()) {
                System.out.println("Aucune demande pour le moment.");
            } else {
                for (String ligne : lignes) {
                    System.out.println(ligne);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("=== Création de votre profil ===");
            System.out.print("Nom: ");
            String nom = sc.nextLine();
            System.out.print("Prénom: ");
            String prenom = sc.nextLine();
            System.out.print("Matricule: ");
            double matricule = sc.nextDouble();
            System.out.print("Réputation initiale (1-5): ");
            float reputation = sc.nextFloat();
            sc.nextLine();

            System.out.print("Statut (1-Passager, 2-Chauffeur): ");
            status statut = (sc.nextInt() == 1) ? status.Passager : status.Chauffeur;
            sc.nextLine();

            System.out.println("Itinéraire habituel (séparé par des virgules): ");
            List<String> itineraire = Arrays.asList(sc.nextLine().split(","));

            System.out.println("Préférences (séparées par des virgules): ");
            List<String> preferences = Arrays.asList(sc.nextLine().split(","));

            System.out.print("Disponibilité (1-Journalier, 2-Hebdomadaire, 3-Quotidien): ");
            Horaire horaire = Horaire.values()[sc.nextInt()-1];

            System.out.print("Type de course (1-Aller-retour, 2-Aller simple, 3-Retour simple): ");
            Type type = Type.values()[sc.nextInt()-1];
            sc.nextLine();

            Profile profil = new Profile(nom, prenom, matricule, reputation,
                    statut, itineraire, preferences, horaire, type);

            System.out.println("\nProfil créé avec succès !");


            while (true) {
                System.out.println("\n=== Menu Principal ===");
                System.out.println("1. Faire une demande de course");
                System.out.println("2. Afficher toutes les demandes");
                System.out.println("3. Changer mon statut");
                System.out.println("4. Quitter");
                System.out.print("Choix: ");

                int choix = sc.nextInt();
                sc.nextLine();

                switch (choix) {
                    case 1:
                        if (status == status.Passager) {
                            profil.ajouterDemande();
                        } else {
                            System.out.println("Seuls les passagers peuvent faire des demandes.");
                        }
                        break;
                    case 2:
                        afficherDemandes();
                        break;
                    case 3:
                        profil.switchStatus();
                        System.out.println("Nouveau statut: " + status);
                        break;
                    case 4:
                        System.out.println("Au revoir !");
                        System.exit(0);
                    default:
                        System.out.println("Choix invalide !");
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}
