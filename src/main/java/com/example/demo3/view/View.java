package com.example.demo3.view;
import com.example.demo3.Controller.AuthClient;
import com.example.demo3.HelloController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class View {
    HelloController helloController = new HelloController();
    UserView Userview=new UserView();
    ChannelView Channelview=new ChannelView();


    public Scene createLoginScene(Stage primaryStage) {
        Image logoImage = new Image(getClass().getResource("/images/raf.jpg").toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);
        Image icon = new Image(getClass().getResource("/images/raf.jpg").toExternalForm());
        primaryStage.getIcons().add(icon);

        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);

        Label labela = new Label("Username:");
        labela.setStyle("-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:15px;");
        TextField usernameField = new TextField();
        usernameField.setId("usernameField");
        usernameField.setStyle("-fx-pref-width: 300px;-fx-border-radius:10px;");




        Label labela2 = new Label("Password:");
        labela2.setStyle("-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:15px;");
        PasswordField passwordField = new PasswordField();
        passwordField.setId("passwordField");
        passwordField.setStyle("-fx-pref-width: 300px;-fx-border-radius:20px;");


        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");
        loginButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:15px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:15px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:15px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();


            try {

                String token = AuthClient.authenticate(username, password);

                if (token != null) {

                    goToNextPage(primaryStage);
                } else {

                    Alert alert = new Alert(Alert.AlertType.ERROR, "Pogrešno korisničko ime ili lozinka");
                    alert.show();
                }

            } catch (IOException ex) {
                ex.printStackTrace();

            }

        });

        HBox usernameLayout = new HBox(10, labela, usernameField);
        usernameLayout.setAlignment(Pos.CENTER);

        HBox passwordLayout = new HBox(10, labela2, passwordField);
        passwordLayout.setAlignment(Pos.CENTER);

        VBox loginLayout = new VBox(15,logoImageView, usernameLayout, passwordLayout, loginButton);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-background-color:white;");
        return new Scene(loginLayout, 500, 500);
    }


    private void goToNextPage(Stage primaryStage) {

        TabPane tabPane = new TabPane();
        tabPane.setId("tabPane");

        Tab firstTab = new Tab("Korisnici");
        firstTab.setClosable(false);
        firstTab.setContent(Userview.createClientTabContent());

        Tab secondTab = new Tab("Kanali");
        secondTab.setClosable(false);
        secondTab.setContent(Channelview.createChannelTabContent());

        tabPane.getTabs().addAll(firstTab, secondTab);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("RafBook");
        primaryStage.show();
    }






}

