module com.example.demo3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires org.json;
    requires java.management;


    requires com.google.api.client;


    requires org.testng;
    requires com.google.api.client.json.jackson2;



    opens com.example.demo3 to javafx.fxml, org.mockito, org.testng;
    opens com.example.demo3.Controller to javafx.fxml, org.mockito, org.testng;
    opens com.example.demo3.Model to com.fasterxml.jackson.databind, org.mockito, org.testng;
    opens com.example.demo3.view to javafx.fxml, org.mockito, org.testng;


    exports com.example.demo3;
    exports com.example.demo3.Controller;
    exports com.example.demo3.Model;
    exports com.example.demo3.view;
    exports com.example.demo3.repository;
}
