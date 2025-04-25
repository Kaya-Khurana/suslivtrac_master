package com.app.suslivtrac;
import com.app.suslivtrac.Session;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HouseholdController {

    @FXML
    private TextField txtElectricity;

    @FXML
    private TextField txtLPG;

    @FXML
    private Label lblResult;

    @FXML
    private Button btnCalculate;

    private int currentUserId = Session.getCurrentUserId();

    @FXML
    private void calculateFootprint() {
        try {
            double electricity = Double.parseDouble(txtElectricity.getText());
            double lpg = Double.parseDouble(txtLPG.getText());

            double factorElectricity = 1.1405; // kgCO2e per kWh
            double factorLPG = 1.55; // kgCO2e per litre

            double footprint = (electricity * factorElectricity) + (lpg * factorLPG);
            lblResult.setText("Total House Footprint = " + String.format("%.2f", footprint) + " kgCO2e");

            // Insert data into DB
            Utils.insertHouseholdData(currentUserId, electricity, lpg, footprint);

        } catch (NumberFormatException e) {
            lblResult.setText("Invalid input! Please enter numbers.");
        } catch (Exception e) {
            lblResult.setText("Error storing data.");
            e.printStackTrace();
        }
    }
}
