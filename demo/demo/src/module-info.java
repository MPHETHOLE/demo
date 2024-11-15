module main.java.com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens main.java.com.example.demo to javafx.fxml;
    exports main.java.com.example.demo;
}