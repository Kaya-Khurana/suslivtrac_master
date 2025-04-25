package com.app.suslivtrac;
import com.app.suslivtrac.Session;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CarController {

    @FXML
    private TextField mileageField;

    @FXML
    private ComboBox<String> unitComboBox;

    @FXML
    private ComboBox<String> vehicleTypeComboBox;

    @FXML
    private ComboBox<String> vehicleYearComboBox;

    @FXML
    private ComboBox<String> vehicleModelComboBox;

    @FXML
    private TextField efficiencyField;

    @FXML
    private ComboBox<String> efficiencyUnitComboBox;

    @FXML
    private ComboBox<String> fuelTypeComboBox;

    @FXML
    private Label resultLabel;

    @FXML
    private Button calculateButton;

    @FXML
    public void initialize() {
        unitComboBox.setItems(FXCollections.observableArrayList("km", "miles"));
        vehicleTypeComboBox.setItems(FXCollections.observableArrayList(
                "Average van, motorbike & car database", "SUV", "Sedan", "Hatchback", "Truck"
        ));
        vehicleYearComboBox.setItems(FXCollections.observableArrayList("2023", "2022", "2021", "2020", "2019"));
        fuelTypeComboBox.setItems(FXCollections.observableArrayList("Petrol", "Diesel", "Electric", "Hybrid"));
        efficiencyUnitComboBox.setItems(FXCollections.observableArrayList("L/100km", "MPG"));

        // ✅ Adding example vehicle models
        vehicleModelComboBox.setItems(FXCollections.observableArrayList(
                "Toyota Corolla", "Honda Civic", "Hyundai i20", "Tesla Model 3", "Ford F-150", "Mahindra Scorpio"
        ));

        unitComboBox.getSelectionModel().select(0);
        efficiencyUnitComboBox.getSelectionModel().select(0);
        fuelTypeComboBox.getSelectionModel().select(0);
        vehicleModelComboBox.getSelectionModel().select(0); // Optional: pre-select a default model

        calculateButton.setOnAction(event -> calculateFootprint());
    }


    @FXML
    private void calculateFootprint() {
        try {
            double mileage = Double.parseDouble(mileageField.getText());
            double efficiency = Double.parseDouble(efficiencyField.getText());

            String vehicle = vehicleTypeComboBox.getValue();
            String year = vehicleYearComboBox.getValue();
            String model = vehicleModelComboBox.getValue();
            String fuelType = fuelTypeComboBox.getValue();

            double carbonFootprint = (mileage / efficiency) * 2.31;

            resultLabel.setText(String.format("Total Car Footprint = %.2f tonnes of CO2e", carbonFootprint));

            int currentUserId = Session.getCurrentUserId();

            Utils.insertCarData(currentUserId, mileage, vehicle, year, model, fuelType, efficiency, carbonFootprint);

        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter valid mileage and efficiency values.");
        } catch (Exception e) {
            resultLabel.setText("Error saving data. Please try again.");
            e.printStackTrace();
        }
    }
}
