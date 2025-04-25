package com.app.suslivtrac;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    @FXML
    private Label householdLabel;

    @FXML
    private Label carLabel;

    @FXML
    private Label flightLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Button recommendationButton;

    @FXML
    private TextArea recommendationTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String dbUrl = "jdbc:mysql://localhost:3306/sust";
        String dbUser = "root";
        String dbPassword = "Kayavp@2106";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            int userId = Session.getCurrentUserId();

            double household = getFootprintFromTable(conn, "household_footprint", userId);
            double car = getFootprintFromTable(conn, "car_footprint", userId);
            double flight = getFootprintFromTable(conn, "flight_footprint", userId);

            double total = household + car + flight;

            householdLabel.setText(String.format("Household: %.2f tonnes CO₂e", household));
            carLabel.setText(String.format("Car: %.2f tonnes CO₂e", car));
            flightLabel.setText(String.format("Flight: %.2f tonnes CO₂e", flight));
            totalLabel.setText(String.format("Total Footprint: %.2f tonnes CO₂e", total));

            // Insert or update results table
            String checkQuery = "SELECT COUNT(*) FROM total_footprint WHERE user_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Update existing row
                        String updateQuery = "UPDATE total_footprint SET household_fp = ?, car_fp = ?, flight_fp = ?, total_fp = ? WHERE user_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setDouble(1, household);
                            updateStmt.setDouble(2, car);
                            updateStmt.setDouble(3, flight);
                            updateStmt.setDouble(4, total);
                            updateStmt.setInt(5, userId);
                            updateStmt.executeUpdate();
                        }
                    } else {
                        // Insert new row
                        String insertQuery = "INSERT INTO total_footprint (user_id, household_fp, car_fp, flight_fp, total_fp) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, userId);
                            insertStmt.setDouble(2, household);
                            insertStmt.setDouble(3, car);
                            insertStmt.setDouble(4, flight);
                            insertStmt.setDouble(5, total);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRecommendationButtonClick() {
        String recommendations = getRecommendations(); // Make sure this method returns a String
        recommendationTextArea.setText(recommendations);
        recommendationTextArea.setVisible(true);
    }

    private double getFootprintFromTable(Connection conn, String tableName, int userId) throws SQLException {
        String query = "SELECT footprint FROM " + tableName + " WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                double total = 0.0;
                while (rs.next()) {
                    total += rs.getDouble("footprint");
                }
                return total;
            }
        }
    }

    private String getRecommendations() {
        StringBuilder recommendations = new StringBuilder();

        // Get the total footprint values from the labels
        double household = Double.parseDouble(householdLabel.getText().split(":")[1].trim().split(" ")[0]);
        double car = Double.parseDouble(carLabel.getText().split(":")[1].trim().split(" ")[0]);
        double flight = Double.parseDouble(flightLabel.getText().split(":")[1].trim().split(" ")[0]);
        double total = household + car + flight;

        // Household tips
        recommendations.append("1. Household Footprint:\n");
        if (household < 2.0) {
            recommendations.append("  - Your household footprint is low! Consider using energy-efficient appliances to further reduce your footprint.\n");
        } else if (household > 5.0) {
            recommendations.append("  - Your household footprint is high. Try to reduce energy consumption by using energy-saving appliances.\n");
        }

        // Car tips
        recommendations.append("\n2. Car Footprint:\n");
        if (car < 1.0) {
            recommendations.append("  - Your car usage is quite low. Great job! Consider using public transportation or electric vehicles for further reduction.\n");
        } else if (car > 3.0) {
            recommendations.append("  - Your car footprint is high. Consider carpooling, using public transport, or switching to an electric vehicle.\n");
        }

        // Flight tips
        recommendations.append("\n3. Flight Footprint:\n");
        if (flight < 1.0) {
            recommendations.append("  - Your flight footprint is minimal. To reduce further, consider alternative modes of travel for short distances.\n");
        } else if (flight > 5.0) {
            recommendations.append("  - Your flight footprint is high. Consider flying less or using carbon offset programs to reduce the impact.\n");
        }

        // Total footprint tips
        recommendations.append("\n4. Overall Footprint:\n");
        if (total < 5.0) {
            recommendations.append("  - Your carbon footprint is low. Keep up the good work! Continue adopting sustainable practices.\n");
        } else if (total > 10.0) {
            recommendations.append("  - Your total footprint is quite high. You could reduce it by adopting sustainable practices in all areas.\n");
        }

        // General tips
        recommendations.append("\n5. General Recommendations:\n");
        recommendations.append("  - Consider tracking your progress regularly to measure the impact of your actions.\n");
        recommendations.append("  - Invest in sustainable energy solutions like solar panels for your home.\n");
        recommendations.append("  - Encourage others to follow sustainable practices and help raise awareness.\n");

        return recommendations.toString(); // Ensure this line is included, this is the return statement
    }
}
