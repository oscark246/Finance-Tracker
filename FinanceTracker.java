package FinanceTrackerApp;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceTracker {
    private List<Transaction> transactions = new ArrayList<>();
    private double balance = 0.0;

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        balance += transaction.getAmount();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public double getBalance() {
        return balance;
    }

    // Method to save data to a file
    public void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("financeData.dat"))) {
            oos.writeObject(transactions);
            oos.writeObject(balance);
        }
    }

    // Method to load data from a file
    @SuppressWarnings("unchecked")
    public void loadData() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("financeData.dat"))) {
            transactions = (List<Transaction>) ois.readObject();
            balance = (double) ois.readObject();
        }
    }
}

