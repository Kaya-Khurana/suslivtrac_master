package com.app.suslivtrac;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signinbtn;

    @FXML
    private Button signupbtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set button actions
        signinbtn.setOnAction(this::handleSignIn);
        signupbtn.setOnAction(this::handleSignUp);
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        if (Utils.SignInUser(event, usernameField.getText(), passwordField.getText())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                // Try getting stage from event source
                Stage stage = null;
                if (event.getSource() instanceof Node) {
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                }

                // If stage is still null, try getting active stage
                if (stage == null) {
                    stage = (Stage) Stage.getWindows().stream()
                            .filter(Window::isShowing)
                            .findFirst()
                            .orElse(null);
                }

                if (stage != null) {
                    stage.setScene(scene);
                    stage.setTitle("Dashboard");
                    stage.show();
                } else {
                    System.out.println("Error: Unable to retrieve Stage.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sign Up");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
