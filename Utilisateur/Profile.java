package Utilisateur;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Profile extends Utilisateur{
    public static void refreshMoyenne(Profile chauffeur, Profile passager, int noteChauff, int notePass) {
    }

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
    private final String role;
    private static final String FICHIER_DEMANDES = "demands.txt";
    private static final String FICHIER_PROFILES = "profiles.txt";

    // Map pour stocker tous les profils en mémoire (pour un accès rapide)
    private static Map<Double, Profile> profilesMap = new HashMap<>();

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

    public Type getType() {
        return type;
    }

    public void changeType(Type type) {
        this.type = type;
    }

    public String getRole() { return this.role; }

    public void switchStatus() {
        this.status = (this.status == status.Passager) ? status.Chauffeur : status.Passager;
    }

    public float calculMoyenne() {
        if (nbPass == 0 && nbChauff == 0) return 0;
        if (status == status.Passager) return moyPass / nbPass;  // Fixed: was moyPass / nbChauff
        if (status == status.Chauffeur) return moyChauff / nbChauff;  // Fixed: was moyChauff / nbPass
        return (moyPass + moyChauff) / (nbPass + nbChauff);
    }

    /**
     * Met à jour la moyenne des notes pour l'utilisateur en fonction des évaluations reçues
     * @param rating La note reçue
     */
    public void refreshMoyenne(float rating) {
        if (status == status.Passager) {
            moyPass += rating;
            nbPass++;
        } else {
            moyChauff += rating;
            nbChauff++;
        }
        setReputation(calculMoyenne());

        // Mettre à jour le profil dans le fichier
        try {
            mettreAJourProfil();
        } catch (IOException e) {
            System.out.println("Erreur lors de la mise à jour du profil: " + e.getMessage());
        }
    }

    /**
     * Met à jour les moyennes des notes pour le chauffeur et le passager impliqués dans une course
     * @param matChauffeur Le matricule du chauffeur
     * @param matPassager Le matricule du passager
     * @param noteChauffeur La note attribuée au chauffeur
     * @param notePassager La note attribuée au passager
     * @return true si la mise à jour a réussi, false sinon
     */
    public static boolean refreshMoyenne(double matChauffeur, double matPassager, float noteChauffeur, float notePassager) {
        // Récupérer les profils par matricule
        Profile chauffeur = getProfileByMatricule(matChauffeur);
        Profile passager = getProfileByMatricule(matPassager);

        if (chauffeur == null || passager == null) {
            System.out.println("Erreur: Un ou plusieurs profils non trouvés.");
            return false;
        }

        // Mise à jour pour le chauffeur
        chauffeur.moyChauff += noteChauffeur;
        chauffeur.nbChauff++;
        chauffeur.setReputation(chauffeur.calculMoyenne());

        // Mise à jour pour le passager
        passager.moyPass += notePassager;
        passager.nbPass++;
        passager.setReputation(passager.calculMoyenne());

        // Sauvegarder les modifications
        try {
            chauffeur.mettreAJourProfil();
            passager.mettreAJourProfil();
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de la mise à jour des profils: " + e.getMessage());
            return false;
        }
    }

    public Profile(String nom, String prenom, double matricule, float rep, status status, List<String> itineraire, List<String> preferences, Horaire horaire, Type type) throws IOException {
        super(nom, prenom, matricule, rep);
        this.role = checkTypeUser();  // Input from user
        this.status = status;
        this.itineraire = itineraire;
        this.preferences = preferences;
        this.horaire = horaire;
        this.type = type;

        profilesMap.put(matricule, this);
        sauvegarderProfil(); // save into users.txt
    }

    /**
     * Sauvegarde le profil dans le fichier des profils
     */
    private void sauvegarderProfil() throws IOException {
        String profileData = getNom() + "," +
                getPrenom() + "," +
                getMatricule() + "," +
                getReputation() + "," +
                status + "," +
                String.join("|", itineraire) + "," +
                String.join("|", preferences) + "," +
                horaire + "," +
                type + "," +
                moyPass + "," +
                moyChauff + "," +
                nbPass + "," +
                nbChauff + "\n";

        Files.write(Paths.get(FICHIER_PROFILES),
                profileData.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    /**
     * Met à jour le profil dans le fichier des profils
     */
    public void mettreAJourProfil() throws IOException {
        if (!Files.exists(Paths.get(FICHIER_PROFILES))) {
            sauvegarderProfil();
            return;
        }

        List<String> lines = Files.readAllLines(Paths.get(FICHIER_PROFILES));
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length >= 3 && Double.parseDouble(parts[2]) == getMatricule()) {
                String updatedLine = getNom() + "," +
                        getPrenom() + "," +
                        getMatricule() + "," +
                        getReputation() + "," +
                        status + "," +
                        String.join("|", itineraire) + "," +
                        String.join("|", preferences) + "," +
                        horaire + "," +
                        type + "," +
                        moyPass + "," +
                        moyChauff + "," +
                        nbPass + "," +
                        nbChauff;

                lines.set(i, updatedLine);
                found = true;
                break;
            }
        }

        if (!found) {
            sauvegarderProfil();
        } else {
            Files.write(Paths.get(FICHIER_PROFILES), lines);
        }
    }

    /**
     * Récupère un profil par son matricule
     * @param matricule Le matricule du profil à récupérer
     * @return Le profil correspondant ou null si non trouvé
     */
    public static Profile getProfileByMatricule(double matricule) {
        // Si le profil est déjà en mémoire, le retourner
        if (profilesMap.containsKey(matricule)) {
            return profilesMap.get(matricule);
        }

        // Sinon, essayer de le charger depuis le fichier
        try {
            if (!Files.exists(Paths.get(FICHIER_PROFILES))) {
                return null;
            }

            List<String> lines = Files.readAllLines(Paths.get(FICHIER_PROFILES));

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 13 && Double.parseDouble(parts[2]) == matricule) {
                    // Reconstruire le profil
                    String nom = parts[0];
                    String prenom = parts[1];
                    float reputation = Float.parseFloat(parts[3]);
                    status stat = status.valueOf(parts[4]);

                    List<String> itineraire = Arrays.asList(parts[5].split("\\|"));
                    List<String> preferences = Arrays.asList(parts[6].split("\\|"));

                    Horaire hor = Horaire.valueOf(parts[7]);
                    Type typ = Type.valueOf(parts[8]);

                    // Créer le profil sans le sauvegarder à nouveau
                    Profile profile = new Profile(nom, prenom, matricule, reputation, stat, itineraire, preferences, hor, typ);

                    // Mettre à jour les moyennes et compteurs
                    profile.moyPass = Float.parseFloat(parts[9]);
                    profile.moyChauff = Float.parseFloat(parts[10]);
                    profile.nbPass = Integer.parseInt(parts[11]);
                    profile.nbChauff = Integer.parseInt(parts[12]);

                    // Ajouter à la map et retourner
                    profilesMap.put(matricule, profile);
                    return profile;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erreur lors de la récupération du profil: " + e.getMessage());
        }

        return null;
    }

    /**
     * Charge tous les profils depuis le fichier
     */
    public static List<Profile> chargerTousProfils() {
        List<Profile> profiles = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(FICHIER_PROFILES))) {
                return profiles;
            }

            List<String> lines = Files.readAllLines(Paths.get(FICHIER_PROFILES));

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 13) {
                    double matricule = Double.parseDouble(parts[2]);

                    // Si le profil n'est pas déjà chargé, le charger
                    if (!profilesMap.containsKey(matricule)) {
                        getProfileByMatricule(matricule);
                    }

                    profiles.add(profilesMap.get(matricule));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erreur lors du chargement des profils: " + e.getMessage());
        }

        return profiles;
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

        String demande = "Demande de: " + getNom() + " " + getPrenom() +
                " (Mat: " + getMatricule() + ")\n" +
                "Statut: " + status.toString() + "\n" +
                "Itinéraire: " + depart + " -> " + arrivee + "\n" +
                "Préférences: " + String.join(", ", preferences) + "\n" +
                "Disponibilité: " + horaire.toString() + "\n" +
                "Type: " + type.toString() + "\n" +
                "Heure demandée: " + heure + "\n" +
                "Réputation: " + String.format("%.1f", getReputation()) + "\n" +
                "----------------------------\n";

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
            e.printStackTrace();
        }
    }
}