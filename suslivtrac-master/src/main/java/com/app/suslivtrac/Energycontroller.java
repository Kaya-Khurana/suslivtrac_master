package com.app.suslivtrac;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class Energycontroller {

    @FXML
    private Label totalConsumptionLabel;

    @FXML
    private TableView<DailyConsumption> energyTable;

    @FXML
    private TableColumn<DailyConsumption, String> dayColumn;

    @FXML
    private TableColumn<DailyConsumption, Number> consumptionColumn;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");

    // Class to hold daily consumption data for the TableView
    private static class DailyConsumption {
        private final SimpleStringProperty day;
        private final SimpleDoubleProperty consumption;

        public DailyConsumption(String day, double consumption) {
            this.day = new SimpleStringProperty(day);
            this.consumption = new SimpleDoubleProperty(consumption);
        }

        public String getDay() {
            return day.get();
        }

        public SimpleStringProperty dayProperty() {
            return day;
        }

        public double getConsumption() {
            return consumption.get();
        }

        public SimpleDoubleProperty consumptionProperty() {
            return consumption;
        }
    }

    @FXML
    private void initialize() {
        try {
            // Configure TableView columns with proper Callback
            dayColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DailyConsumption, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<DailyConsumption, String> param) {
                    return param.getValue().dayProperty();
                }
            });

            // Simplified cell value factory for consumptionColumn
            consumptionColumn.setCellValueFactory(cellData -> {
                double value = cellData.getValue().getConsumption();
                return new SimpleObjectProperty<Number>(value);
            });

            // Get current user ID from Session
            int userId = Session.getCurrentUserId();
            System.out.println("Current user ID: " + userId);

            // Fetch household data from database
            List<HouseholdData> dataList = Utils.getHouseholdData(userId);
            System.out.println("Fetched " + dataList.size() + " records");

            // Calculate total electricity consumption
            double totalConsumption = dataList.stream()
                    .mapToDouble(HouseholdData::getElectricityKwh)
                    .sum();
            System.out.println("Total consumption calculated: " + totalConsumption + " kWh");
            totalConsumptionLabel.setText(String.format("Total Electricity Consumption: %.2f kWh", totalConsumption));

            // Prepare data for TableView (daily consumption for last 7 days)
            Map<String, Double> dailyConsumption = new TreeMap<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -6); // Start from 6 days ago

            // Initialize 7 days with 0 consumption
            for (int i = 0; i < 7; i++) {
                String day = dateFormat.format(calendar.getTime());
                dailyConsumption.put(day, 0.0);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Aggregate consumption by day
            for (HouseholdData data : dataList) {
                Timestamp createdAt = data.getCreatedAt();
                if (createdAt != null) {
                    String day = dateFormat.format(createdAt);
                    dailyConsumption.compute(day, (k, v) -> v == null ? data.getElectricityKwh() : v + data.getElectricityKwh());
                }
            }

            // Populate TableView
            ObservableList<DailyConsumption> tableData = FXCollections.observableArrayList();
            dailyConsumption.forEach((day, consumption) -> tableData.add(new DailyConsumption(day, consumption)));
            energyTable.setItems(tableData);
            System.out.println("TableView populated with " + tableData.size() + " entries");

        } catch (Exception e) {
            totalConsumptionLabel.setText("Error fetching data: " + e.getMessage());
            System.out.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
