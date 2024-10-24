package FinanceTrackerApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;


public class App extends Application {
    private FinanceTracker tracker = new FinanceTracker();

    @Override
    public void start(Stage primaryStage) {
        TextField descriptionField = new TextField();
        TextField amountField = new TextField();
        TextField categoryField = new TextField();
        DatePicker datePicker = new DatePicker();
        Label balanceLabel = new Label("Balance: $0.0");
        ListView<String> transactionListView = new ListView<>();
        Button addButton = new Button("Add Transaction");
        Button saveButton = new Button("Save Data");
        Button loadButton = new Button("Load Data");

        addButton.setOnAction(event -> {
            try {
                String description = descriptionField.getText();
                if (description.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Description cannot be empty.");
                    return;
                }

                double amt;
                try {
                    amt = Double.parseDouble(amountField.getText());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Amount must be a valid number.");
                    return;
                }

                String category = categoryField.getText();
                if (category.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Category cannot be empty.");
                    return;
                }

                LocalDate date = datePicker.getValue();
                if (date == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid date.");
                    return;
                }

                Transaction newTransaction = new Transaction(description, amt, category, date);
                tracker.addTransaction(newTransaction);
                updateTransactionList(transactionListView);
                balanceLabel.setText("Balance: $" + tracker.getBalance());

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + ex.getMessage());
            }
        });

        saveButton.setOnAction(event -> {
            try {
                tracker.saveData();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data saved successfully!");
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save data: " + ex.getMessage());
            }
        });

        loadButton.setOnAction(event -> {
            try {
                tracker.loadData();
                updateTransactionList(transactionListView);
                balanceLabel.setText("Balance: $" + tracker.getBalance());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data loaded successfully!");
            } catch (IOException | ClassNotFoundException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data: " + ex.getMessage());
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            new Label("Description"), descriptionField,
            new Label("Amount"), amountField,
            new Label("Category"), categoryField,
            new Label("Date"), datePicker,
            addButton,
            saveButton,
            loadButton,
            balanceLabel,
            transactionListView
        );

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Personal Finance Tracker");
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateTransactionList(ListView<String> listView) {
        listView.getItems().clear();
        for (Transaction transaction : tracker.getTransactions()) {
            listView.getItems().add(transaction.toString());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


