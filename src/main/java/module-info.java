module com.example.demo3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires org.json;

    // Testing modules
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;
    requires org.mockito.junit.jupiter;
    requires org.testng;
    // Remove the "requires junit;" line since we're using JUnit 5

    // Open packages needed for testing
    opens com.example.demo3 to javafx.fxml, org.mockito, org.testng;
    opens com.example.demo3.Controller to javafx.fxml, org.mockito, org.testng;
    opens com.example.demo3.Model to com.fasterxml.jackson.databind, org.mockito, org.testng;
    opens com.example.demo3.view to javafx.fxml, org.mockito, org.testng;

    // Exports
    exports com.example.demo3;
    exports com.example.demo3.Controller;
    exports com.example.demo3.Model;
    exports com.example.demo3.view;
}