package com.app.suslivtrac;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private Button signupbtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        signupbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                    Utils.SignUpUser(actionEvent, emailField.getText(), usernameField.getText(), passwordField.getText());
            }
        });



    }
}
