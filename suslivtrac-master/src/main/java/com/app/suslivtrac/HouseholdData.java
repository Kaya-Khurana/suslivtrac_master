package com.app.suslivtrac;

import java.sql.Timestamp;

public class HouseholdData {
    private int id;
    private int userId;
    private double electricityKwh;
    private double lpgLitres;
    private double footprint;
    private Timestamp createdAt;

    public HouseholdData(int id, int userId, double electricityKwh, double lpgLitres, double footprint, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.electricityKwh = electricityKwh;
        this.lpgLitres = lpgLitres;
        this.footprint = footprint;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getElectricityKwh() {
        return electricityKwh;
    }

    public double getLpgLitres() {
        return lpgLitres;
    }

    public double getFootprint() {
        return footprint;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
