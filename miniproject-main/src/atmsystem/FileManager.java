package atmsystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * FileManager class
 * Handles all file reading and writing for user data and transactions.
 * Data is stored in two text files: users.txt and transactions.txt
 */
public class FileManager {

    // file names where data is stored
    static String USERS_FILE = "users.txt";
    static String TRANSACTIONS_FILE = "transactions.txt";

    // ---- USER FILE OPERATIONS ----

    // saves a new user to the users.txt file (append mode)
    public static void saveUser(String accountNo, String name, String pin) {
        try {
            FileWriter fw = new FileWriter(USERS_FILE, true); // true means append
            PrintWriter pw = new PrintWriter(fw);
            pw.println(accountNo + "|" + name + "|" + pin);
            pw.close();
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // reads all users from users.txt and returns them as a list of String arrays
    // each String array has 3 elements: [accountNo, name, pin]
    public static ArrayList<String[]> loadUsers() {
        ArrayList<String[]> users = new ArrayList<String[]>();
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return users; // return empty list if file not created yet
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // skip blank lines
                String[] parts = line.split("\\|"); // split by | character
                if (parts.length == 3) {
                    users.add(parts);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
        return users;
    }

    // updates a user's PIN by rewriting the entire users.txt file
    public static void updatePin(String accountNo, String newPin) {
        ArrayList<String[]> users = loadUsers();
        try {
            // overwrite mode (no true flag = overwrite)
            PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE));
            for (int i = 0; i < users.size(); i++) {
                String[] user = users.get(i);
                if (user[0].equals(accountNo)) {
                    user[2] = newPin; // change the pin
                }
                pw.println(user[0] + "|" + user[1] + "|" + user[2]);
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error updating PIN: " + e.getMessage());
        }
    }

    // ---- TRANSACTION FILE OPERATIONS ----

    // saves a single transaction to transactions.txt (append mode)
    public static void saveTransaction(String accountNo, String date, String type, String amount) {
        try {
            FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(accountNo + "|" + date + "|" + type + "|" + amount);
            pw.close();
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    // reads all transactions for a specific account number
    // each String array has 4 elements: [accountNo, date, type, amount]
    public static ArrayList<String[]> loadTransactions(String accountNo) {
        ArrayList<String[]> transactions = new ArrayList<String[]>();
        try {
            File file = new File(TRANSACTIONS_FILE);
            if (!file.exists()) {
                return transactions;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // skip blank lines
                String[] parts = line.split("\\|");
                // only add transactions that belong to this account
                if (parts.length == 4 && parts[0].equals(accountNo)) {
                    transactions.add(parts);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
        return transactions;
    }
}
