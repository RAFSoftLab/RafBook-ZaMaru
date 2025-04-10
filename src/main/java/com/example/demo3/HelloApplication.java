package com.example.demo3;

import com.example.demo3.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        View view=new View();
        primaryStage.setScene(view.createLoginScene(primaryStage));
        primaryStage.setTitle("RafBook Login");
        primaryStage.show();
    }

}
