package atmsystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/*
 * ATMSystem - Main class
 * This is the entry point of the ATM Simulation System.
 * It handles user interaction through console menus and
 * uses FileManager class for storing/reading data from files.
 */
public class ATMSystem {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   WELCOME TO ATM SIMULATION SYSTEM    ");
        System.out.println("========================================");

        boolean running = true;

        while (running) {
            System.out.println("\n-------- MAIN MENU --------");
            System.out.println("1. Create New Account");
            System.out.println("2. Login to Account");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            if (choice == -1) continue; // invalid input, ask again

            switch (choice) {
                case 1:
                    signup();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("\nThank you for using ATM System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    // ---------- ACCOUNT CREATION ----------

    public static void signup() {
        System.out.println("\n--- CREATE NEW ACCOUNT ---");

        System.out.print("Enter your full name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        if (name.contains("|")) {
            System.out.println("Name cannot contain the '|' character.");
            return;
        }

        System.out.print("Enter your father's name: ");
        String fatherName = sc.nextLine().trim();
        if (fatherName.contains("|")) {
            System.out.println("Input cannot contain the '|' character.");
            return;
        }

        System.out.print("Enter your email: ");
        String email = sc.nextLine().trim();
        if (email.contains("|")) {
            System.out.println("Email cannot contain the '|' character.");
            return;
        }

        System.out.print("Enter your city: ");
        String city = sc.nextLine().trim();
        if (city.contains("|")) {
            System.out.println("City cannot contain the '|' character.");
            return;
        }

        System.out.print("Enter your state: ");
        String state = sc.nextLine().trim();
        if (state.contains("|")) {
            System.out.println("State cannot contain the '|' character.");
            return;
        }

        // get and validate the PIN
        System.out.print("Set a 4-digit PIN: ");
        String pin = sc.nextLine().trim();

        if (pin.length() != 4 || !isAllDigits(pin)) {
            System.out.println("PIN must be exactly 4 digits (numbers only).");
            return;
        }

        System.out.print("Re-enter PIN to confirm: ");
        String confirmPin = sc.nextLine().trim();
        if (!pin.equals(confirmPin)) {
            System.out.println("PINs do not match. Account not created.");
            return;
        }

        // get initial deposit
        System.out.print("Enter initial deposit amount (minimum Rs 500): ");
        int deposit = getIntInput();
        if (deposit == -1) return;

        if (deposit < 500) {
            System.out.println("Minimum initial deposit is Rs 500.");
            return;
        }
        if (deposit > 1000000) {
            System.out.println("Initial deposit cannot exceed Rs 10,00,000.");
            return;
        }

        // generate a unique random 8-digit account number
        ArrayList<String[]> existingUsers = FileManager.loadUsers();
        Random ran = new Random();
        String accountNo;
        do {
            int num = 10000000 + ran.nextInt(90000000);
            accountNo = String.valueOf(num);
        } while (isAccountTaken(accountNo, existingUsers));

        // save user details to file
        FileManager.saveUser(accountNo, name, pin);

        // save initial deposit as first transaction
        String date = new Date().toString();
        FileManager.saveTransaction(accountNo, date, "Deposit", String.valueOf(deposit));

        // show success message with account details
        System.out.println("\n===================================");
        System.out.println("  Account Created Successfully!");
        System.out.println("===================================");
        System.out.println("  Account Number : " + accountNo);
        System.out.println("  Name           : " + name);
        System.out.println("  Initial Balance: Rs " + deposit);
        System.out.println("===================================");
        System.out.println("  IMPORTANT: Remember your Account");
        System.out.println("  Number and PIN to login!");
        System.out.println("===================================");
    }

    // ---------- LOGIN ----------

    public static void login() {
        System.out.println("\n--- LOGIN ---");

        ArrayList<String[]> users = FileManager.loadUsers();
        if (users.isEmpty()) {
            System.out.println("No registered accounts found. Please create an account first.");
            return;
        }

        System.out.print("Enter Account Number: ");
        String accountNo = sc.nextLine().trim();

        System.out.print("Enter PIN: ");
        String pin = sc.nextLine().trim();

        // search for user in the file
        boolean found = false;
        String userName = "";

        for (int i = 0; i < users.size(); i++) {
            String[] user = users.get(i);
            // user[0] = accountNo, user[1] = name, user[2] = pin
            if (user[0].equals(accountNo) && user[2].equals(pin)) {
                found = true;
                userName = user[1];
                break;
            }
        }

        if (found) {
            System.out.println("\nLogin successful! Welcome, " + userName);
            transactionMenu(accountNo, userName);
        } else {
            System.out.println("Invalid Account Number or PIN.");
        }
    }

    // ---------- TRANSACTION MENU ----------

    public static void transactionMenu(String accountNo, String name) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n-------- TRANSACTION MENU --------");
            System.out.println("  Account: " + accountNo);
            System.out.println("  Name   : " + name);
            System.out.println("----------------------------------");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Fast Cash");
            System.out.println("4. Balance Enquiry");
            System.out.println("5. Mini Statement");
            System.out.println("6. Change PIN");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            if (choice == -1) continue;

            switch (choice) {
                case 1:
                    deposit(accountNo);
                    break;
                case 2:
                    withdraw(accountNo);
                    break;
                case 3:
                    fastCash(accountNo);
                    break;
                case 4:
                    checkBalance(accountNo);
                    break;
                case 5:
                    miniStatement(accountNo);
                    break;
                case 6:
                    changePin(accountNo);
                    break;
                case 7:
                    System.out.println("Logged out successfully.");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ---------- DEPOSIT ----------

    public static void deposit(String accountNo) {
        System.out.print("\nEnter amount to deposit: Rs ");
        int amount = getIntInput();
        if (amount == -1) return;

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        if (amount > 100000) {
            System.out.println("Maximum deposit limit per transaction is Rs 1,00,000.");
            return;
        }

        // prevent balance overflow
        long currentBalance = getBalance(accountNo);
        if (currentBalance + amount > 2000000000L) {
            System.out.println("Deposit exceeds maximum allowed account balance.");
            return;
        }

        String date = new Date().toString();
        FileManager.saveTransaction(accountNo, date, "Deposit", String.valueOf(amount));
        System.out.println("Rs " + amount + " deposited successfully!");
    }

    // ---------- WITHDRAW ----------

    public static void withdraw(String accountNo) {
        System.out.print("\nEnter amount to withdraw: Rs ");
        int amount = getIntInput();
        if (amount == -1) return;

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        // ATM denomination check (multiples of 100)
        if (amount % 100 != 0) {
            System.out.println("Please enter amount in multiples of 100 (e.g. 500, 1000, 2000).");
            return;
        }

        // per-transaction withdrawal limit
        if (amount > 20000) {
            System.out.println("Maximum withdrawal limit per transaction is Rs 20,000.");
            return;
        }

        // check if balance is enough
        int balance = getBalance(accountNo);
        if (amount > balance) {
            System.out.println("Insufficient balance! Your balance is Rs " + balance);
            return;
        }

        String date = new Date().toString();
        FileManager.saveTransaction(accountNo, date, "Withdrawal", String.valueOf(amount));
        System.out.println("Rs " + amount + " withdrawn successfully!");
    }

    // ---------- FAST CASH ----------

    public static void fastCash(String accountNo) {
        System.out.println("\n--- FAST CASH ---");
        System.out.println("1. Rs 100");
        System.out.println("2. Rs 500");
        System.out.println("3. Rs 1000");
        System.out.println("4. Rs 2000");
        System.out.println("5. Rs 5000");
        System.out.println("6. Back");
        System.out.print("Select option: ");

        int choice = getIntInput();
        if (choice == -1) return;

        int amount = 0;
        switch (choice) {
            case 1: amount = 100; break;
            case 2: amount = 500; break;
            case 3: amount = 1000; break;
            case 4: amount = 2000; break;
            case 5: amount = 5000; break;
            case 6: return; // go back
            default:
                System.out.println("Invalid option.");
                return;
        }

        // check if balance is enough
        int balance = getBalance(accountNo);
        if (amount > balance) {
            System.out.println("Insufficient balance! Your balance is Rs " + balance);
            return;
        }

        String date = new Date().toString();
        FileManager.saveTransaction(accountNo, date, "Withdrawal", String.valueOf(amount));
        System.out.println("Rs " + amount + " withdrawn successfully!");
    }

    // ---------- BALANCE ENQUIRY ----------

    public static void checkBalance(String accountNo) {
        int balance = getBalance(accountNo);
        System.out.println("\n----------------------------------");
        System.out.println("  Your Current Balance: Rs " + balance);
        System.out.println("----------------------------------");
    }

    // calculates total balance by going through all transactions
    // deposits are added, withdrawals are subtracted
    public static int getBalance(String accountNo) {
        ArrayList<String[]> transactions = FileManager.loadTransactions(accountNo);
        int balance = 0;

        for (int i = 0; i < transactions.size(); i++) {
            String[] t = transactions.get(i);
            try {
                int amount = Integer.parseInt(t[3].trim());
                if (t[2].equals("Deposit")) {
                    balance = balance + amount;
                } else {
                    balance = balance - amount;
                }
            } catch (NumberFormatException e) {
                // skip any corrupted transaction amount cleanly
            }
        }
        return balance;
    }

    // ---------- MINI STATEMENT ----------

    public static void miniStatement(String accountNo) {
        ArrayList<String[]> transactions = FileManager.loadTransactions(accountNo);

        if (transactions.size() == 0) {
            System.out.println("\nNo transactions found.");
            return;
        }

        System.out.println("\n========== MINI STATEMENT ==========");
        System.out.println("Account Number: " + accountNo);
        System.out.println("------------------------------------");

        // show the last 5 transactions (or all if less than 5)
        int start = 0;
        if (transactions.size() > 5) {
            start = transactions.size() - 5;
        }

        for (int i = start; i < transactions.size(); i++) {
            String[] t = transactions.get(i);
            // t[0]=accountNo, t[1]=date, t[2]=type, t[3]=amount
            System.out.println(t[1]);
            System.out.println("  " + t[2] + " : Rs " + t[3]);
            System.out.println();
        }

        System.out.println("------------------------------------");
        System.out.println("Current Balance: Rs " + getBalance(accountNo));
        System.out.println("====================================");
    }

    // ---------- CHANGE PIN ----------

    public static void changePin(String accountNo) {
        System.out.print("\nEnter your current 4-digit PIN: ");
        String currentPin = sc.nextLine().trim();

        // verify current PIN against stored user record
        ArrayList<String[]> users = FileManager.loadUsers();
        String actualPin = "";
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i)[0].equals(accountNo)) {
                actualPin = users.get(i)[2];
                break;
            }
        }

        if (!currentPin.equals(actualPin)) {
            System.out.println("Incorrect current PIN. PIN not changed.");
            return;
        }

        System.out.print("Enter new 4-digit PIN: ");
        String newPin = sc.nextLine().trim();

        if (newPin.length() != 4 || !isAllDigits(newPin)) {
            System.out.println("PIN must be exactly 4 digits (numbers only).");
            return;
        }

        if (newPin.equals(currentPin)) {
            System.out.println("New PIN cannot be the same as your current PIN.");
            return;
        }

        System.out.print("Re-enter new PIN to confirm: ");
        String confirmPin = sc.nextLine().trim();

        if (!newPin.equals(confirmPin)) {
            System.out.println("PINs do not match. PIN not changed.");
            return;
        }

        FileManager.updatePin(accountNo, newPin);
        System.out.println("PIN changed successfully!");
    }

    // ---------- HELPER METHODS ----------

    // reads an integer from user input, returns -1 if input is invalid
    public static int getIntInput() {
        try {
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.");
                return -1;
            }
            int num = Integer.parseInt(input);
            return num;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid whole number.");
            return -1;
        }
    }

    // checks if a string contains only digit characters
    public static boolean isAllDigits(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    // checks if an account number is already assigned to an existing user
    public static boolean isAccountTaken(String accountNo, ArrayList<String[]> users) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i)[0].equals(accountNo)) {
                return true;
            }
        }
        return false;
    }
}
