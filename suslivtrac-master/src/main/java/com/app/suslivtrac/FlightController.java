package com.app.suslivtrac;
import com.app.suslivtrac.Session;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FlightController {

    @FXML private RadioButton returnTripRadio;
    @FXML private RadioButton oneWayTripRadio;
    @FXML private ComboBox<String> fromComboBox;
    @FXML private ComboBox<String> toComboBox;
    @FXML private ComboBox<String> viaComboBox;
    @FXML private ComboBox<String> classComboBox;
    @FXML private TextField tripsField;
    @FXML private CheckBox radiativeForcingCheckbox;
    @FXML private Button calculateButton;
    @FXML private Label resultText;

    private ToggleGroup tripTypeGroup;

    private int currentUserId = Session.getCurrentUserId();

    @FXML
    public void initialize() {
        tripTypeGroup = new ToggleGroup();
        returnTripRadio.setToggleGroup(tripTypeGroup);
        oneWayTripRadio.setToggleGroup(tripTypeGroup);
        returnTripRadio.setSelected(true);

        fromComboBox.setItems(FXCollections.observableArrayList("New York", "London", "Tokyo", "Dubai"));
        toComboBox.setItems(FXCollections.observableArrayList("Paris", "Berlin", "Sydney", "Mumbai"));
        viaComboBox.setItems(FXCollections.observableArrayList("None", "Amsterdam", "Hong Kong", "Singapore"));
        viaComboBox.getSelectionModel().select("None");

        classComboBox.setItems(FXCollections.observableArrayList("Economy Class", "Business Class", "First Class"));
        classComboBox.getSelectionModel().selectFirst();

        tripsField.setText("1");

        calculateButton.setOnAction(e -> calculateCarbonFootprint());
    }

    private void calculateCarbonFootprint() {
        try {
            int trips = Integer.parseInt(tripsField.getText());
            String tripType = returnTripRadio.isSelected() ? "Return" : "One-Way";
            String from = fromComboBox.getValue();
            String to = toComboBox.getValue();
            String via = viaComboBox.getValue();
            String flightClass = classComboBox.getValue();
            boolean includeRadiativeForcing = radiativeForcingCheckbox.isSelected();

            // Dummy calculation
            double baseEmission = 0.2 * trips;
            if (flightClass.equals("Business Class")) baseEmission *= 1.5;
            if (flightClass.equals("First Class")) baseEmission *= 2;
            if (tripType.equals("Return")) baseEmission *= 2;
            if (includeRadiativeForcing) baseEmission *= 1.3;

            resultText.setText("Total Flights Footprint = " + String.format("%.2f", baseEmission) + " tonnes of CO2e");

            // Store in DB
            Utils.insertFlightData(
                    currentUserId,
                    tripType,
                    from,
                    to,
                    via.equals("None") ? null : via,
                    flightClass,
                    trips,
                    includeRadiativeForcing,
                    baseEmission
            );

        } catch (NumberFormatException e) {
            resultText.setText("Invalid number of trips!");
        }
    }
}
