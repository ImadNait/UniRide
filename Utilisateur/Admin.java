package Utilisateur;

import java.io.*;
import java.util.Scanner;

public class Admin {
    private static String password = "pass123";
    private static final String blackPath = "blacklist.txt";
    private static final String usersPath = "users.txt";

    public Admin() {
        // Default constructor
    }

    public void changePass() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter current password:");
        String currentPass = sc.nextLine();

        if (currentPass.equals(password)) {
            while (true) {
                System.out.println("Enter new password:");
                String newpass = sc.nextLine();
                if (!isValidPass(newpass)) {System.out.println("Password must be at least 8 characters and contain at least one number and one symbol");}
                else {
                    password = newpass;
                    System.out.println("Password changed");
                    break;
                }
            }
        } else {System.out.println("Incorrect password");}
    }

    private boolean isValidPass(String password) {
        boolean digit = false;
        boolean symbol = false;

        if (password.length() < 8) {return false;}
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {digit = true;} else if (!Character.isLetterOrDigit(c)) {symbol = true;}
            if (digit && symbol) {return true;}
        }
        return false;
    }

    public void banUser(double matUser) throws IOException {
        if (Utilisateur.findUser(matUser)) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(blackPath, true));
            writer.write(String.format("%.0f", matUser) + "\n");
            writer.close();
            System.out.println("Banned user with matricule " + matUser);
        } else {
            System.out.println("User not found.");
        }
    }

    public boolean isUserBanned(double matricule) throws IOException {
        File file = new File(blackPath);
        if (!file.exists()) {return false;}
        BufferedReader reader = new BufferedReader(new FileReader(blackPath));
        Object line;

        while ((line = reader.readLine()) != null) {
                if ((Double)line == matricule) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
            return false;
        }

        public void unbanUser(double matricule) throws IOException {
            File blackFile = new File(blackPath);
            if (!blackFile.exists()) {
                System.out.println("Blacklist file does not exist.");
                return;
            }
            File tblackFile = new File("temp_blacklist.txt");
            BufferedReader reader = new BufferedReader(new FileReader(blackFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tblackFile));
            Object line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                    if ((Double) line != matricule) {
                        writer.write(line + "\n");
                    } else {
                        found = true;
                        System.out.println("Unbanned user with matricule " + matricule);
                        return;}
                }
                reader.close();
                writer.close();
                if (!found){
                    tblackFile.delete();
                    System.out.println("User not found in the blacklist");
                }
            }

            public void deleteUser(double mat) throws IOException {
                File usersFile = new File(usersPath);
                File temp = new File("users_temp.txt");

                if (!usersFile.exists()) {System.out.println("Users file doesn't exist");return ;}
                boolean userExists = Utilisateur.findUser(mat);/// we declare it late so we don't use exessive memory if user file doesn't exist
                if (!userExists) {System.out.println("User doesn't exist");return ;}
                BufferedReader reader = new BufferedReader(new FileReader(usersFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
                String line;
                boolean userDeleted = false;

                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                    if (data.length > 0) {
                        try {
                            double matricule = Double.parseDouble(data[0]);

                            if (matricule != mat) {
                                writer.write(line + "\n");
                            } else {userDeleted = true;}
                        } catch (NumberFormatException e) {writer.write(line + "\n");}
                    }
                }
                reader.close();
                writer.close();
                if (userDeleted) {
                    System.out.println("User deleted.");
                    if (isUserBanned(mat)) {unbanUser(mat);}
                    return ;
                } else {temp.delete();return ;
                }
            }

            public void showBannedUsers() throws IOException {
                File file = new File(blackPath);
                if (!file.exists() || file.length() == 0) {
                    System.out.println("No banned users");
                    return;
                }
                System.out.println("List of banned users :");
                BufferedReader reader = new BufferedReader(new FileReader(blackPath));
                String line;

                while ((line = reader.readLine()) != null) {
                    double matricule = Double.parseDouble(line);
                    System.out.println(String.format("%.0f", matricule));
                }
                System.out.println("--------------------------------");
                reader.close();
            }
}