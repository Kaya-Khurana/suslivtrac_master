package com.app.suslivtrac;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.io.IOException;

public class DashboardController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab householdTab;

    @FXML
    private Tab carTab;

    @FXML
    private Tab flightTab;

    @FXML
    private Tab energyTab;

    @FXML
    private Tab resultTab;


    @FXML
    public void initialize() {
        loadTabContent(householdTab, "/com/app/suslivtrac/household.fxml");
        loadTabContent(flightTab, "/com/app/suslivtrac/flight.fxml");
        loadTabContent(carTab, "/com/app/suslivtrac/car.fxml");
        loadTabContent(energyTab, "/com/app/suslivtrac/energy.fxml");
        loadTabContent(resultTab, "/com/app/suslivtrac/result.fxml");

    }

    private void loadTabContent(Tab tab, String fxmlFile) {
        if (tab == null || fxmlFile == null || fxmlFile.isEmpty()) {
            System.out.println("Error: Tab or FXML file is null/empty!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();
            tab.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
