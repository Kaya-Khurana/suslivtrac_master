module com.app.suslivtrac {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.app.suslivtrac to javafx.fxml;
    exports com.app.suslivtrac;
}