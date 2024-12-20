module com.example.demo3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires org.json;



    opens com.example.demo3 to javafx.fxml;
    exports com.example.demo3;
    exports com.example.demo3.Controller;
    opens com.example.demo3.Controller to javafx.fxml;

    exports com.example.demo3.Model;
    opens com.example.demo3.Model to com.fasterxml.jackson.databind;
    exports com.example.demo3.view;
    opens com.example.demo3.view to javafx.fxml;

}