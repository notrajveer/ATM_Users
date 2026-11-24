# ATM Simulation System

A console-based ATM banking simulator built in Java. It allows users to register an account, securely log in, and perform banking transactions like deposits, withdrawals, balance checks, mini statements, and PIN changes. All persistent data is managed through text files using standard Java File I/O.

---

## 📂 File Breakdown (What Each File Does)

Before diving into features and execution, here is a breakdown of every file in the project:

### 1. `ATMSystem.java` (Application Logic & User Interface)
* **Role:** The main driver class and user-facing controller.
* **Responsibilities:**
  * Contains the `main(String[] args)` method which starts the application.
  * Handles all console I/O using `Scanner` (displaying menus, collecting user inputs).
  * Implements the **Main Menu** (Sign Up, Login, Exit) and the post-login **Transaction Menu** (Deposit, Withdraw, Fast Cash, Balance Enquiry, Mini Statement, PIN Change, Logout).
  * Validates inputs (e.g., verifying 4-digit numeric PINs, non-empty names, minimum deposit requirements, and sufficient funds before withdrawals).
  * Computes the current balance dynamically in `getBalance()` by calculating the difference between total deposits and total withdrawals.

### 2. `FileManager.java` (Data Persistence & File I/O)
* **Role:** The data access helper class.
* **Responsibilities:**
  * Separates file read/write operations from the user interface logic.
  * **`saveUser()`**: Appends a newly registered user record to `users.txt` using `FileWriter(..., true)` and `PrintWriter`.
  * **`loadUsers()`**: Reads all user records line-by-line from `users.txt` using `BufferedReader` and parses them using `.split("\\|")`.
  * **`updatePin()`**: Updates a user's PIN by loading all records into an `ArrayList`, modifying the targeted user's PIN, and rewriting `users.txt`.
  * **`saveTransaction()`**: Appends a transaction record (date, type, amount) to `transactions.txt`.
  * **`loadTransactions()`**: Reads and filters transactions from `transactions.txt` for a specific account number.

### 3. `users.txt` (User Database)
* **Role:** Plain text file acting as the user database.
* **Format:** `accountNumber|name|pin`
* **Lifecycle:** Created automatically upon the first account registration. Reads occur during login checks, and rewrites occur during PIN updates.

### 4. `transactions.txt` (Transaction Ledger)
* **Role:** Plain text file storing the transaction ledger.
* **Format:** `accountNumber|date|type|amount`
* **Lifecycle:** Created automatically when initial deposits or any subsequent transactions occur. Read whenever a balance check or mini-statement is requested.

---

## 🌟 Key Features

* **Account Registration:** Generates a unique 8-digit account number, sets a 4-digit security PIN, and records an initial deposit (min. Rs. 500).
* **PIN Authentication:** Validates both account number and PIN before granting access to transactions.
* **Cash Deposit:** Adds funds and logs the transaction with a timestamp.
* **Cash Withdrawal:** Verifies available funds before debiting to prevent overdrafts.
* **Fast Cash:** One-click withdrawals for fixed amounts (Rs. 100, 500, 1000, 2000, 5000).
* **Real-time Balance Enquiry:** Calculates balance on-the-fly from the transaction ledger.
* **Mini Statement:** Formats and displays up to the last 5 transactions for the logged-in account.
* **PIN Management:** Allows users to securely change their PIN with confirmation validation.

---

## 💾 Data Storage Specification

Data is stored without external database drivers using pipe-separated values (`|`):

### `users.txt`
```text
48271093|Rajesh Kumar|1234
91537628|Priya Sharma|5678
```

### `transactions.txt`
```text
48271093|Sat Nov 21 12:30:00 IST 2026|Deposit|5000
48271093|Sat Nov 21 12:35:10 IST 2026|Withdrawal|1000
48271093|Sat Nov 21 12:40:22 IST 2026|Deposit|2000
```

---

## 🔄 Program Flow

```
                     ┌───────────────────────┐
                     │     Application Start │
                     └──────────┬────────────┘
                                │
                                ▼
                     ┌───────────────────────┐
                     │       Main Menu       │
                     │  1. Create Account    │
                     │  2. Login             │
                     │  3. Exit              │
                     └──────────┬────────────┘
                                │
          ┌─────────────────────┴─────────────────────┐
          ▼                                           ▼
 ┌─────────────────┐                        ┌───────────────────┐
 │   signup()      │                        │     login()       │
 │ - Collect Info  │                        │ - Check Account   │
 │ - Validate PIN  │                        │   and PIN in file │
 │ - Save to file  │                        └─────────┬─────────┘
 └─────────────────┘                                  │ (If valid)
                                                      ▼
                                           ┌──────────────────────┐
                                           │   Transaction Menu   │
                                           │ 1. Deposit           │
                                           │ 2. Withdraw          │
                                           │ 3. Fast Cash         │
                                           │ 4. Balance Enquiry   │
                                           │ 5. Mini Statement    │
                                           │ 6. Change PIN        │
                                           │ 7. Logout            │
                                           └──────────────────────┘
```

---

## 🚀 How to Run

### Prerequisites
* Java Development Kit (JDK 8 or above).
* `javac` and `java` commands accessible in terminal.

### Steps

1. Open your terminal or Command Prompt.
2. Navigate to the `src` folder:
   ```bash
   cd miniproject-main/src
   ```
3. Compile both Java source files:
   ```bash
   javac atmsystem/ATMSystem.java atmsystem/FileManager.java
   ```
4. Run the application:
   ```bash
   java atmsystem.ATMSystem
   ```

---

## 🧠 Core Java Concepts Demonstrated

| Concept | Implementation Details |
|---|---|
| **Separation of Concerns** | UI and logic in `ATMSystem.java`; Data access in `FileManager.java`. |
| **Control Structures** | `switch` blocks for menus; `while` loops for session control; `if-else` for input checks. |
| **Collections (`ArrayList`)** | Dynamically holding records read from files during runtime. |
| **String Manipulation** | `.split("\\|")` for parsing records; `.equals()` for string matching; `.length()` for PIN length check. |
| **File I/O Streams** | `BufferedReader` and `FileReader` for reading; `FileWriter` and `PrintWriter` for appending and rewriting. |
| **Exception Handling** | `try-catch` blocks handling `IOException` and `NumberFormatException` to prevent crashes. |
| **Type Conversion** | `Integer.parseInt()` and `String.valueOf()` for converting between text records and numbers. |

---

## 💻 Sample Terminal Output

```text
========================================
   WELCOME TO ATM SIMULATION SYSTEM    
========================================

-------- MAIN MENU --------
1. Create New Account
2. Login to Account
3. Exit
Enter your choice: 1

--- CREATE NEW ACCOUNT ---
Enter your full name: Rajesh Kumar
Enter your father's name: Suresh Kumar
Enter your email: rajesh@email.com
Enter your city: Chennai
Enter your state: Tamil Nadu
Set a 4-digit PIN: 1234
Re-enter PIN to confirm: 1234
Enter initial deposit amount (minimum Rs 500): 5000

===================================
  Account Created Successfully!
===================================
  Account Number : 48271093
  Name           : Rajesh Kumar
  Initial Balance: Rs 5000
===================================
  IMPORTANT: Remember your Account
  Number and PIN to login!
===================================
```
