package com.app.suslivtrac;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class homescreen {





    @FXML
    private Label welcomeLabel;

    // Example setter to pass username or other data
    public void setWelcomeMessage(String username) {
        welcomeLabel.setText("Welcome, " + username);
    }

}
