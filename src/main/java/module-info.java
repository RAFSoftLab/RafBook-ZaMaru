module com.example.demo3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;



    opens com.example.demo3 to javafx.fxml;
    exports com.example.demo3;
}