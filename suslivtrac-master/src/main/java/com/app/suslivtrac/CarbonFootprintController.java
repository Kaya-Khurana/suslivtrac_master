package com.app.suslivtrac;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;

public class CarbonFootprintController {

    @FXML private ComboBox<String> activityComboBox;
    @FXML private TextField amountField;
    @FXML private TableView<CarbonFootprintEntry> carbonTable;
    @FXML private TableColumn<CarbonFootprintEntry, String> dateColumn;
    @FXML private TableColumn<CarbonFootprintEntry, String> activityColumn;
    @FXML private TableColumn<CarbonFootprintEntry, String> emissionColumn;

    private ObservableList<CarbonFootprintEntry> dataList = FXCollections.observableArrayList();
    private Connection connection;

    public void initialize() {
        connectDatabase();
        loadData();
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        activityColumn.setCellValueFactory(cellData -> cellData.getValue().activityProperty());
        emissionColumn.setCellValueFactory(cellData -> cellData.getValue().emissionProperty());
        carbonTable.setItems(dataList);
    }

    private void connectDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/suslivtrack", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCalculate() {
        String activity = activityComboBox.getValue();
        String amountText = amountField.getText();

        if (activity == null || amountText.isEmpty()) {
            showAlert("Error", "Please enter activity and amount.");
            return;
        }

        double amount = Double.parseDouble(amountText);
        double emission = calculateEmissions(activity, amount);
        saveToDatabase(activity, emission);
        loadData();
    }

    private double calculateEmissions(String activity, double amount) {
        switch (activity) {
            case "Car Travel": return amount * 0.2; // 0.2 kg CO2 per km
            case "Electricity Usage": return amount * 0.5; // 0.5 kg CO2 per kWh
            case "Meat Consumption": return amount * 1.5; // 1.5 kg CO2 per kg meat
            case "Public Transport": return amount * 0.1; // 0.1 kg CO2 per km
            default: return amount * 0.3; // Default multiplier for "Other"
        }
    }

    private void saveToDatabase(String activity, double emission) {
        try {
            String query = "INSERT INTO carbon_footprint (user_id, date, activity, emissions) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, 1); // Replace with actual user_id
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setString(3, activity);
            pstmt.setDouble(4, emission);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        dataList.clear();
        try {
            String query = "SELECT date, activity, emissions FROM carbon_footprint";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                dataList.add(new CarbonFootprintEntry(
                        rs.getString("date"),
                        rs.getString("activity"),
                        String.format("%.2f", rs.getDouble("emissions"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
