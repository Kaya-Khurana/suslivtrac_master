package com.app.suslivtrac;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CarbonFootprintEntry {
    private final StringProperty date;
    private final StringProperty activity;
    private final StringProperty emission;

    public CarbonFootprintEntry(String date, String activity, String emission) {
        this.date = new SimpleStringProperty(date);
        this.activity = new SimpleStringProperty(activity);
        this.emission = new SimpleStringProperty(emission);
    }

    public StringProperty dateProperty() { return date; }
    public StringProperty activityProperty() { return activity; }
    public StringProperty emissionProperty() { return emission; }
}
