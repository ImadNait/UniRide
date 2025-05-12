package Utilisateur;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final String USERS_FILE = "users.txt";
    private static final String COURSES_FILE = "courses.txt";
    private static final String DEMANDS_FILE = "demands.txt";
    private static final String FICHIER_PROFILES = "profiles.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Bienvenue dans l'application de covoiturage universitaire ===");

        initializeFiles();

        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Créer un compte");
            System.out.println("2. Se connecter");
            System.out.println("3. Accès administrateur");
            System.out.println("4. Quitter");
            System.out.print("Votre choix: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        creerCompte();
                        break;
                    case 2:
                        seConnecter();
                        break;
                    case 3:
                        accesAdmin();
                        break;
                    case 4:
                        System.out.println("Merci d'avoir utilisé notre application. Au revoir !");
                        System.exit(0);
                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Initialise les fichiers nécessaires s'ils n'existent pas
 
    private static void initializeFiles() {
        try {
            // Créer les fichiers s'ils n'existent pas
            createFileIfNotExists(USERS_FILE);
            createFileIfNotExists(COURSES_FILE);
            createFileIfNotExists(DEMANDS_FILE);
            createFileIfNotExists(FICHIER_PROFILES);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'initialisation des fichiers: " + e.getMessage());
        }
    }

    // Crée un fichier s'il n'existe pas

    private static void createFileIfNotExists(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("Fichier créé: " + fileName);
        }
    }

    // Interface pour créer un compte utilisateur

    private static void creerCompte() throws IOException {
        System.out.println("\n=== Création de compte ===");

        System.out.print("Nom: ");
        String nom = scanner.nextLine();

        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();

        double matricule;
        while (true) {
            System.out.print("Matricule (format YYYYXXXX): ");
            try {
                matricule = Double.parseDouble(scanner.nextLine());

                // Vérifier si l'utilisateur existe déjà
                if (userExists(matricule)) {
                    System.out.println("Un utilisateur avec ce matricule existe déjà.");
                    continue;
                }

                // Vérifier le format du matricule
                String matStr = String.format("%.0f", matricule);
                if (matStr.length() != 8) {
                    System.out.println("Le matricule doit être au format YYYYXXXX (8 chiffres).");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }

        float reputation = 3.0f; // Réputation par défaut pour les nouveaux utilisateurs

        // Créer un utilisateur de base
        Utilisateur utilisateur = null;

        // Demander le type d'utilisateur
        System.out.println("\nType d'utilisateur:");
        System.out.println("1. Étudiant");
        System.out.println("2. Enseignant");
        System.out.println("3. ATS (Administration, Technique, Service)");
        System.out.print("Votre choix: ");
        int typeChoix = Integer.parseInt(scanner.nextLine());

        switch (typeChoix) {
            case 1: // Étudiant
                System.out.print("Année d'admission: ");
                int anneeAdmission = Integer.parseInt(scanner.nextLine());

                System.out.print("Faculté: ");
                String faculteEtudiant = scanner.nextLine();

                System.out.print("Spécialité: ");
                String specialite = scanner.nextLine();

                utilisateur = new Etudiant(nom, prenom, matricule, reputation, anneeAdmission, faculteEtudiant, specialite);
                break;

            case 2: // Enseignant
                System.out.print("Année de recrutement: ");
                int anneeRecrutementEnseignant = Integer.parseInt(scanner.nextLine());

                System.out.print("Faculté: ");
                String faculteEnseignant = scanner.nextLine();

                utilisateur = new Enseignant(nom, prenom, matricule, reputation, anneeRecrutementEnseignant, faculteEnseignant);
                break;

            case 3: // ATS
                System.out.print("Année de recrutement: ");
                int anneeRecrutementATS = Integer.parseInt(scanner.nextLine());

                System.out.print("Service: ");
                String service = scanner.nextLine();

                utilisateur = new Ats(nom, prenom, matricule, reputation, anneeRecrutementATS, service);
                break;

            default:
                System.out.println("Type d'utilisateur invalide.");
                return;
        }

        // Créer le profil utilisateur
        createUserProfile(utilisateur, matricule);

        System.out.println("\nCompte créé avec succès ! Vous pouvez maintenant vous connecter.");
    }

    // Crée un profil pour l'utilisateur

    private static void createUserProfile(Utilisateur utilisateur, double matricule) throws IOException {
        System.out.println("\n=== Configuration du profil ===");

        System.out.print("Statut (1-Passager, 2-Chauffeur): ");
        Profile.status statut = (Integer.parseInt(scanner.nextLine()) == 1) ?
                Profile.status.valueOf("Passager") :
                Profile.status.valueOf("Chauffeur");

        System.out.println("Itinéraire habituel (points séparés par des virgules): ");
        List<String> itineraire = Arrays.asList(scanner.nextLine().split(","));

        System.out.println("Préférences (séparées par des virgules, ex: musique, non-fumeur): ");
        List<String> preferences = Arrays.asList(scanner.nextLine().split(","));

        System.out.print("Disponibilité (1-Journalier, 2-Hebdomadaire, 3-Quotidien): ");
        int horChoix = Integer.parseInt(scanner.nextLine());
        Profile.Horaire horaire = null;
        switch (horChoix) {
            case 1: horaire = Profile.Horaire.valueOf("Journalier"); break;
            case 2: horaire = Profile.Horaire.valueOf("Hebdomadaire"); break;
            case 3: horaire = Profile.Horaire.valueOf("Quotidien"); break;
            default: horaire = Profile.Horaire.valueOf("Quotidien");
        }

        System.out.print("Type de course (1-AllerRetour, 2-Aller, 3-Retour): ");
        int typeChoix = Integer.parseInt(scanner.nextLine());
        Profile.Type type = null;
        switch (typeChoix) {
            case 1: type = Profile.Type.valueOf("allerRetour"); break;
            case 2: type = Profile.Type.valueOf("aller"); break;
            case 3: type = Profile.Type.valueOf("retour"); break;
            default: type = Profile.Type.valueOf("allerRetour");
        }

        // Créer le profil
        Profile profil = new Profile(
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                matricule,
                utilisateur.getReputation(),
                statut,
                itineraire,
                preferences,
                horaire,
                type
        );
    }

    // Interface de connexion utilisateur

    private static void seConnecter() throws IOException {
        System.out.println("\n=== Connexion ===");

        System.out.print("Matricule: ");
        double matricule;
        try {
            matricule = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Matricule invalide.");
            return;
        }

        // Vérifier si l'utilisateur est banni
        if (isUserBanned(matricule)) {
            System.out.println("Votre compte a été banni par l'administrateur. Contactez le support pour plus d'informations.");
            return;
        }

        // Vérifier si l'utilisateur existe
        if (!userExists(matricule)) {
            System.out.println("Aucun utilisateur trouvé avec ce matricule.");
            return;
        }

        // Charger le profil de l'utilisateur
        Profile profil = Profile.getProfileByMatricule(matricule);
        if (profil == null) {
            System.out.println("Erreur lors du chargement du profil.");
            return;
        }

        System.out.println("\nBienvenue, " + profil.getPrenom() + " " + profil.getNom() + " !");

        // Afficher le menu utilisateur
        afficherMenuUtilisateur(profil);
    }

    // Vérifie si un utilisateur est banni

    private static boolean isUserBanned(double matricule) {
        try {
            File file = new File("blacklist.txt");
            if (!file.exists()) {
                return false;
            }

            BufferedReader reader = new BufferedReader(new FileReader("blacklist.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                if (Double.parseDouble(line) == matricule) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la vérification de la liste noire: " + e.getMessage());
        }

        return false;
    }

    // Vérifie si un utilisateur existe dans le système

    private static boolean userExists(double matricule) {
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return false;
            }

            BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && Double.parseDouble(parts[0]) == matricule) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la vérification de l'existence de l'utilisateur: " + e.getMessage());
        }

        return false;
    }

    // Affiche le menu utilisateur après connexion

    private static void afficherMenuUtilisateur(Profile profil) throws IOException {
        while (true) {
            boolean isPassager = profil.getStatus().toString().equals("Passager");

            System.out.println("\n=== Menu Utilisateur ===");
            System.out.println("Statut actuel: " + profil.getStatus());
            System.out.println("Réputation: " + String.format("%.1f", profil.getReputation()));

            if (isPassager) {
                System.out.println("1. Faire une demande de course");
                System.out.println("2. Voir l'historique de mes courses");
                System.out.println("3. Changer mon statut (devenir chauffeur)");
            } else {
                System.out.println("1. Voir les demandes de course disponibles");
                System.out.println("2. Voir l'historique de mes courses");
                System.out.println("3. Changer mon statut (devenir passager)");
            }

            System.out.println("4. Modifier mon profil");
            System.out.println("5. Se déconnecter");
            System.out.print("Votre choix: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        if (isPassager) {
                            faireDemandeCoursePourPassager(profil);
                        } else {
                            voirDemandesDisponibles(profil);
                        }
                        break;
                    case 2:
                        voirHistoriqueCourses(profil);
                        break;
                    case 3:
                        profil.switchStatus();
                        System.out.println("Statut changé avec succès !");
                        break;
                    case 4:
                        modifierProfil(profil);
                        break;
                    case 5:
                        System.out.println("Déconnexion...");
                        return;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    // Permet à un passager de faire une demande de course

    private static void faireDemandeCoursePourPassager(Profile profil) throws IOException {
        System.out.println("\n=== Faire une demande de course ===");

        System.out.print("Point de départ: ");
        String depart = scanner.nextLine();

        System.out.print("Point d'arrivée: ");
        String arrivee = scanner.nextLine();

        System.out.print("Heure de départ (HH:MM): ");
        String heure = scanner.nextLine();

        String demande = "Demande de: " + profil.getNom() + " " + profil.getPrenom() +
                " (Mat: " + profil.getMatricule() + ")\n" +
                "Statut: " + profil.getStatus().toString() + "\n" +
                "Itinéraire: " + depart + " -> " + arrivee + "\n" +
                "Préférences: " + String.join(", ", profil.getPreferences()) + "\n" +
                "Disponibilité: " + profil.getHoraire().toString() + "\n" +
                "Type: " + profil.getType().toString() + "\n" +
                "Heure demandée: " + heure + "\n" +
                "Réputation: " + String.format("%.1f", profil.getReputation()) + "\n" +
                "----------------------------\n";

        Files.write(Paths.get(DEMANDS_FILE),
                demande.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);

        System.out.println("Demande de course enregistrée avec succès !");
    }

    // Affiche les demandes de course disponibles pour les chauffeurs

    private static void voirDemandesDisponibles(Profile profil) throws IOException {
        System.out.println("\n=== Demandes de course disponibles ===");

        File file = new File(DEMANDS_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Aucune demande de course disponible pour le moment.");
            return;
        }

        List<String> demandes = new ArrayList<>();
        List<String> demandesCompletes = Files.readAllLines(Paths.get(DEMANDS_FILE));

        // Extraire les demandes individuelles
        StringBuilder currentDemande = new StringBuilder();
        int index = 1;

        for (String line : demandesCompletes) {
            if (line.equals("----------------------------")) {
                currentDemande.append(line).append("\n");
                demandes.add(currentDemande.toString());
                currentDemande = new StringBuilder();
            } else {
                currentDemande.append(line).append("\n");
            }
        }

        // Afficher les demandes
        if (demandes.isEmpty()) {
            System.out.println("Aucune demande de course disponible pour le moment.");
            return;
        }

        for (int i = 0; i < demandes.size(); i++) {
            System.out.println("\nDemande #" + (i + 1));
            System.out.print(demandes.get(i));
        }

        // Demander au chauffeur s'il veut accepter une demande
        System.out.print("\nVoulez-vous accepter une demande? (O/N): ");
        String reponse = scanner.nextLine();

        if (reponse.equalsIgnoreCase("O")) {
            System.out.print("Entrez le numéro de la demande que vous souhaitez accepter: ");
            int choix = Integer.parseInt(scanner.nextLine()) - 1;

            if (choix >= 0 && choix < demandes.size()) {
                accepterDemande(demandes.get(choix), profil);
            } else {
                System.out.println("Numéro de demande invalide.");
            }
        }
    }

    // Permet à un chauffeur d'accepter une demande de course

    private static void accepterDemande(String demande, Profile chauffeur) throws IOException {
        // Extraire les informations de la demande
        String[] lignes = demande.split("\n");

        // Extraire le matricule du passager
        String ligneMat = lignes[0];
        double matPassager = Double.parseDouble(ligneMat.substring(ligneMat.indexOf("Mat: ") + 5, ligneMat.indexOf(")")));

        // Créer une nouvelle course
        Course course = new Course(chauffeur.getMatricule(), matPassager);
        course.addCourse();
        course.startCourse();

        // Supprimer la demande du fichier
        supprimerDemande(demande);

        System.out.println("\nVous avez accepté la demande de course !");
        System.out.println("La course a été créée et est maintenant en cours.");

        // Demander au chauffeur s'il veut terminer la course
        System.out.print("\nVoulez-vous terminer la course maintenant? (O/N): ");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("O")) {
            terminerCourse(course);
        }
    }

    // Supprime une demande de course du fichier

    private static void supprimerDemande(String demande) throws IOException {
        List<String> toutesLignes = Files.readAllLines(Paths.get(DEMANDS_FILE));
        String contenuFichier = String.join("\n", toutesLignes);

        // Remplacer la demande par une chaîne vide
        contenuFichier = contenuFichier.replace(demande, "");

        // Nettoyer les lignes vides
        contenuFichier = contenuFichier.replaceAll("(?m)^\\s*$\\n", "");

        // Réécrire le fichier
        Files.write(Paths.get(DEMANDS_FILE), contenuFichier.getBytes());
    }

    // Permet de terminer une course

    private static void terminerCourse(Course course) throws IOException {
        System.out.println("\n=== Terminer la course ===");

        // Note pour le chauffeur
        int noteChauffeur;
        while (true) {
            System.out.print("Note pour le chauffeur (1-5): ");
            try {
                noteChauffeur = Integer.parseInt(scanner.nextLine());
                if (noteChauffeur >= 1 && noteChauffeur <= 5) {
                    break;
                } else {
                    System.out.println("La note doit être entre 1 et 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }

        // Note pour le passager
        int notePassager;
        while (true) {
            System.out.print("Note pour le passager (1-5): ");
            try {
                notePassager = Integer.parseInt(scanner.nextLine());
                if (notePassager >= 1 && notePassager <= 5) {
                    break;
                } else {
                    System.out.println("La note doit être entre 1 et 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }

        // Commentaires
        System.out.print("Commentaire pour le chauffeur: ");
        String commentChauffeur = scanner.nextLine();

        System.out.print("Commentaire pour le passager: ");
        String commentPassager = scanner.nextLine();

        // Terminer la course
        course.endCourse(noteChauffeur, notePassager, commentChauffeur, commentPassager);

        System.out.println("\nCourse terminée avec succès !");
    }

    // Affiche l'historique des courses d'un utilisateur

    private static void voirHistoriqueCourses(Profile profil) throws IOException {
        System.out.println("\n=== Historique de mes courses ===");

        File file = new File(COURSES_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Aucune course enregistrée.");
            return;
        }

        List<String> lines = Files.readAllLines(Paths.get(COURSES_FILE));
        boolean coursesFound = false;

        System.out.println("Chauffeur\tPassager\tHoraire\t\t\tStatut\tNoteCh\tNotePass\tCommentaires");

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                double matChauffeur = Double.parseDouble(parts[0]);
                double matPassager = Double.parseDouble(parts[1]);

                if (matChauffeur == profil.getMatricule() || matPassager == profil.getMatricule()) {
                    // Afficher les détails de la course
                    String roleUtilisateur = (matChauffeur == profil.getMatricule()) ? "Chauffeur" : "Passager";
                    String statut = parts[3];
                    String noteChauffeur = (parts.length > 6) ? parts[6] : "N/A";
                    String notePassager = (parts.length > 7) ? parts[7] : "N/A";
                    String commentaires = (parts.length > 8) ? parts[8] + " / " + ((parts.length > 9) ? parts[9] : "") : "Aucun";

                    System.out.println(parts[0] + "\t" + parts[1] + "\t" + parts[2] + "\t" +
                            statut + "\t" + noteChauffeur + "\t" + notePassager + "\t" + commentaires);

                    coursesFound = true;
                }
            }
        }

        if (!coursesFound) {
            System.out.println("Aucune course trouvée pour votre compte.");
        }
    }

    // Permet de modifier le profil utilisateur
    private static void modifierProfil(Profile profil) throws IOException {
        System.out.println("\n=== Modifier mon profil ===");

        System.out.println("1. Modifier mes préférences");
        System.out.println("2. Modifier mon itinéraire habituel");
        System.out.println("3. Modifier ma disponibilité");
        System.out.println("4. Modifier mon type de course");
        System.out.println("5. Retour");
        System.out.print("Votre choix: ");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                System.out.println("Nouvelles préférences (séparées par des virgules): ");
                List<String> preferences = Arrays.asList(scanner.nextLine().split(","));
                profil.setPreferences(preferences);
                break;
            case 2:
                System.out.println("Nouvel itinéraire habituel (points séparés par des virgules): ");
                List<String> itineraire = Arrays.asList(scanner.nextLine().split(","));
                profil.setItineraire(itineraire);
                break;
            case 3:
                System.out.print("Disponibilité (1-Journalier, 2-Hebdomadaire, 3-Quotidien): ");
                int horChoix = Integer.parseInt(scanner.nextLine());
                Profile.Horaire horaire = null;
                switch (horChoix) {
                    case 1: horaire = Profile.Horaire.valueOf("Journalier"); break;
                    case 2: horaire = Profile.Horaire.valueOf("Hebdomadaire"); break;
                    case 3: horaire = Profile.Horaire.valueOf("Quotidien"); break;
                    default: horaire = Profile.Horaire.valueOf("Quotidien");
                }
                profil.changeHoraire(horaire);
                break;
            case 4:
                System.out.print("Type de course (1-AllerRetour, 2-Aller, 3-Retour): ");
                int typeChoix = Integer.parseInt(scanner.nextLine());
                Profile.Type type = null;
                switch (typeChoix) {
                    case 1: type = Profile.Type.valueOf("allerRetour"); break;
                    case 2: type = Profile.Type.valueOf("aller"); break;
                    case 3: type = Profile.Type.valueOf("retour"); break;
                    default: type = Profile.Type.valueOf("allerRetour");
                }
                profil.changeType(type);
                break;
            case 5:
                return;
            default:
                System.out.println("Choix invalide.");
        }

        profil.mettreAJourProfil();
        System.out.println("Profil mis à jour avec succès !");
    }

    // Interface d'accès administrateur

    private static void accesAdmin() {
        System.out.println("\n=== Accès Administrateur ===");

        System.out.print("Mot de passe administrateur: ");
        String password = scanner.nextLine();

        try {
            Admin admin = new Admin();
            // Vérification du mot de passe
            if (password.equals("pass123")) { // Mot de passe par défaut défini dans Admin.java
                System.out.println("Connexion réussie !");
                admin.showAdminMenu();
            } else {
                System.out.println("Mot de passe incorrect. Accès refusé.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'accès administrateur: " + e.getMessage());
        }
    }
}
