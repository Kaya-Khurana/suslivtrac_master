package com.app.suslivtrac;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList; // Added import
import java.util.List;

public class Utils {
//hellooo
public static boolean SignInUser(ActionEvent event, String username, String password) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sust", "root", "Kayavp@2106");
        preparedStatement = connection.prepareStatement("SELECT id, password FROM auth WHERE username = ?");
        preparedStatement.setString(1, username);
        resultSet = preparedStatement.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            System.out.println("User not found in the database.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Provided credentials are incorrect.");
            alert.show();
            return false;
        } else {
            while (resultSet.next()) {
                String retrievedPassword = resultSet.getString("password");

                if (retrievedPassword.equals(password)) {
                    int userId = resultSet.getInt("id"); // NEW LINE
                    Session.setCurrentUserId(userId);    // NEW LINE
                    System.out.println("Sign-In Successful. User ID: " + userId);

                    // Load Dashboard
                    try {
                        FXMLLoader loader = new FXMLLoader(Utils.class.getResource("/com/app/suslivtrac/dashboard.fxml"));
                        Parent root = loader.load();

                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.setTitle("Dashboard");
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("ERROR LOADING FXML: " + e.getMessage());
                    }

                    return true;
                }
            }
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    return false;
}


    public static void SignUpUser(ActionEvent event, String email, String username, String password){

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sust", "root", "Kayavp@2106");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM auth WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if(resultSet.isBeforeFirst()){
                System.out.println("User already exists.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username already exists.");
                alert.show();
            }
            else{
                psInsert = connection.prepareStatement("INSERT INTO auth (username, password, email) VALUES (?, ?, ?);");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, email);
                psInsert.executeUpdate();


                FXMLLoader loader = new FXMLLoader(Utils.class.getResource("signin.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Sign In");
                stage.show();

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public static void insertHouseholdData(int userId, double electricity, double lpg, double footprint) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sust", "root", "Kayavp@2106")) {
            String query = "INSERT INTO household_footprint (user_id, electricity_kwh, lpg_litres, footprint) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setDouble(2, electricity);
            statement.setDouble(3, lpg);
            statement.setDouble(4, footprint);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<HouseholdData> getHouseholdData(int userId) {
        List<HouseholdData> dataList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sust", "root", "Kayavp@2106")) {
            String query = "SELECT id, user_id, electricity_kwh, lpg_litres, footprint, created_at FROM household_footprint WHERE user_id = ? ORDER BY created_at DESC";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        HouseholdData data = new HouseholdData(
                                resultSet.getInt("id"),
                                resultSet.getInt("user_id"),
                                resultSet.getDouble("electricity_kwh"),
                                resultSet.getDouble("lpg_litres"),
                                resultSet.getDouble("footprint"),
                                resultSet.getTimestamp("created_at")
                        );
                        dataList.add(data);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while fetching household data: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
        return dataList;
    }



    public static void insertCarData(int userId, double mileage, String vehicleType, String vehicleYear, String vehicleModel, String fuelType, double efficiency, double footprint) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sust", "root", "Kayavp@2106")) {
            String query = "INSERT INTO car_footprint (user_id, mileage, vehicle_type, vehicle_year, vehicle_model, fuel_type, efficiency, footprint) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, userId);
                ps.setDouble(2, mileage);
                ps.setString(3, vehicleType);
                ps.setString(4, vehicleYear);
                ps.setString(5, vehicleModel);
                ps.setString(6, fuelType);
                ps.setDouble(7, efficiency);
                ps.setDouble(8, footprint);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void insertFlightData(int userId, String tripType, String fromLoc, String toLoc, String viaLoc, String flightClass, int trips, boolean radiativeForcing, double footprint) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sust", "root", "Kayavp@2106")) {
            String query = "INSERT INTO flight_footprint (user_id, trip_type, from_location, to_location, via_location, travel_class, trips, radiative_forcing, footprint) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, userId);
                ps.setString(2, tripType);
                ps.setString(3, fromLoc);
                ps.setString(4, toLoc);
                ps.setString(5, viaLoc);
                ps.setString(6, flightClass);
                ps.setInt(7, trips);
                ps.setBoolean(8, radiativeForcing);
                ps.setDouble(9, footprint);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
