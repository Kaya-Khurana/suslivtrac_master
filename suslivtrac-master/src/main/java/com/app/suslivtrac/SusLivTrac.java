


//package com.app.suslivtrac;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class HelloApplication extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
package com.app.suslivtrac;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SusLivTrac extends Application {
    private static Stage primaryStage; // Store primary stage for navigation

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage; // Store the main stage

        // Load Sign-in Page Initially
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Sign In");
        stage.setScene(scene);
        stage.setX(650);
        stage.setY(300);
        stage.show();
    }

    // Method to switch scenes (used by controllers)
    public static void switchScene(String fxmlFile, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SusLivTrac.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(); // Launch JavaFX application
    }
}
