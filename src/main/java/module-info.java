module com.example.sqlselector {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sqlselector to javafx.fxml;
    exports com.example.sqlselector;
}