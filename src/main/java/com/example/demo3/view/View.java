package com.example.demo3.view;

import com.example.demo3.Controller.ApiClientUser;
import com.example.demo3.Controller.AuthClient;
import com.example.demo3.HelloController;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.Person;
import com.example.demo3.repository.MainRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.example.demo3.Controller.ApiChannel.getChannels;
import static com.example.demo3.Controller.ApiClientUser.getUsers;

public class View {
    HelloController helloController = new HelloController();


    public Scene createLoginScene(Stage primaryStage) {
        Image logoImage = new Image(getClass().getResource("/images/raf.jpg").toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);
        Image icon = new Image(getClass().getResource("/images/raf.jpg").toExternalForm());
        primaryStage.getIcons().add(icon);
        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);
        // Polja za korisničko ime i lozinku
        Label labela = new Label("Username:");
        labela.setStyle("-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:15px;");
        TextField usernameField = new TextField();
        usernameField.setStyle("-fx-pref-width: 300px;-fx-border-radius:10px;");




        Label labela2 = new Label("Password:");
        labela2.setStyle("-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:15px;");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-pref-width: 300px;-fx-border-radius:20px;");


        Button loginButton = new Button("Login");
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


        Tab firstTab = new Tab("Korisnici");
        firstTab.setClosable(false);
        firstTab.setContent(createClientTabContent());


        Tab secondTab = new Tab("Kanali");
        secondTab.setClosable(false);
        secondTab.setContent(createChannelTabContent());


        tabPane.getTabs().addAll(firstTab, secondTab);


        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("RafBook");
        primaryStage.show();
    }


    public VBox createClientTabContent() {
        TableView<Person> table = new TableView<>();

        ObservableList<Person> userData = FXCollections.observableArrayList();
        FilteredList<Person> filteredData = new FilteredList<>(userData, p -> true);

        try {
            List<Person> users = getUsers();
            userData.addAll(users);
        } catch (IOException e) {
            e.printStackTrace();
        }

        table.setItems(filteredData);

        helloController.setTable(table);

        TableColumn<Person, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Person, String> firstNameColumn = new TableColumn<>("Ime");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Person, String> lastNameColumn = new TableColumn<>("Prezime");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Person, String> usernameColumn = new TableColumn<>("Password");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Person, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Person, String> roleColumn = new TableColumn<>("Uloga");
        roleColumn.setCellValueFactory(cellData -> {
            List<String> roles = cellData.getValue().getRole();
            return new SimpleStringProperty(String.join(", ", roles));
        });

        table.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, usernameColumn, emailColumn, roleColumn);

        TextField searchField = new TextField();
        searchField.setPromptText("Pretraži korisnike...");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return person.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
                        person.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                        person.getUsername().toLowerCase().contains(lowerCaseFilter) ||
                        person.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });

        table.setItems(filteredData);



        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Ime");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Prezime");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField roleField = new TextField();
        roleField.setPromptText("role");

        table.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Person selectedPerson = row.getItem();

                    // Popuni polja za unos sa podacima selektovanog korisnika
                    firstNameField.setText(selectedPerson.getFirstName());
                    lastNameField.setText(selectedPerson.getLastName());
                    usernameField.setText(selectedPerson.getUsername());
                    emailField.setText(selectedPerson.getEmail());
                    roleField.setText(String.join(", ", selectedPerson.getRole()));

                }
            });
            return row;
        });




        Button addButton = new Button("Dodaj");
        addButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        addButton.setOnMouseEntered(e -> {
            addButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        addButton.setOnMouseExited(e -> {
            addButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        addButton.setOnAction(e -> {
            if (firstNameField.getText().isEmpty() ||
                    lastNameField.getText().isEmpty() ||
                    usernameField.getText().isEmpty() ||
                    emailField.getText().isEmpty() ||
                    roleField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sva polja moraju biti popunjena");
                alert.show();
                return;
            }
            MainRepository.getInstance().put("firstName", firstNameField.getText());
            MainRepository.getInstance().put("lastName", lastNameField.getText());
            MainRepository.getInstance().put("password", usernameField.getText());
            MainRepository.getInstance().put("email", emailField.getText());
            MainRepository.getInstance().put("role", roleField.getText());
            MainRepository.getInstance().put("mac", "no mac address");

            helloController.addPerson();
            filteredData.setPredicate(filteredData.getPredicate());

            // Osveži filter da uključi novog korisnika


            // Clear fields after attempt
            firstNameField.clear();
            lastNameField.clear();
            usernameField.clear();
            emailField.clear();
            roleField.clear();

        });




        Button deleteButton = new Button("Obriši");
        deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        deleteButton.setOnMouseEntered(e -> {
            deleteButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        deleteButton.setOnMouseExited(e -> {
            deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        deleteButton.setOnAction(e->helloController.deletePerson(table));


        Button editButton = new Button("Izmeni");


        editButton.setOnAction(e -> {
            if (firstNameField.getText().isEmpty() ||
                    lastNameField.getText().isEmpty() ||
                    usernameField.getText().isEmpty() ||
                    emailField.getText().isEmpty() ||
                    roleField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sva polja moraju biti popunjena");
                alert.show();
                return;
            }
            Person selectedPerson = table.getSelectionModel().getSelectedItem();
            if (selectedPerson == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nijedan red nije selektovan za ažuriranje!");
                alert.show();
                return;
            }

            // Ažuriraj vrednosti selektovanog korisnika
            selectedPerson.setFirstName(firstNameField.getText());
            selectedPerson.setLastName(lastNameField.getText());
            selectedPerson.setUsername(usernameField.getText());
            selectedPerson.setEmail(emailField.getText());
            selectedPerson.setRole(Arrays.asList(roleField.getText().split(", ")));

            // Pozovi API za ažuriranje korisnika
            try {
                // Ažuriraj vrednosti selektovanog korisnika
                selectedPerson.setFirstName(firstNameField.getText());
                selectedPerson.setLastName(lastNameField.getText());
                selectedPerson.setUsername(usernameField.getText());
                selectedPerson.setEmail(emailField.getText());
                selectedPerson.setRole(Arrays.asList(roleField.getText().split(", ")));

                // Pozovi API za ažuriranje korisnika
                boolean success = ApiClientUser.editUser(selectedPerson);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Korisnik uspešno ažuriran!");
                    alert.show();

                    // Osveži podatke u tabeli
                    table.refresh();

                    // Očisti polja za unos
                    firstNameField.clear();
                    lastNameField.clear();
                    usernameField.clear();
                    emailField.clear();
                    roleField.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Ažuriranje korisnika nije uspelo!");
                    alert.show();
                }
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške: " + ex.getMessage());
                alert.show();
            }


        });


        editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        editButton.setOnMouseEntered(e -> {
            editButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });



        editButton.setOnMouseExited(e -> {
            editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });




        Image image2=new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image3=new ImageView(image2);
        image3.setFitWidth(200);
        image3.setPreserveRatio(true);

        HBox inputLayout = new HBox(10, firstNameField, lastNameField,usernameField, emailField,roleField, addButton,editButton,deleteButton);
        VBox vbox = new VBox(10, new Label("Pretraga:"),searchField, table, inputLayout,image3);
        vbox.setStyle("-fx-background-color:white;");
        return vbox;
    }


    private VBox createChannelTabContent() {
        TableView<Channel> table2 = new TableView<>();

        ObservableList<Channel> channelData = FXCollections.observableArrayList();
        FilteredList<Channel> filteredData = new FilteredList<>(channelData, p -> true);

        try {
            List<Channel> channels = getChannels();
            channelData.addAll(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }

        table2.setItems(filteredData);

        TableColumn<Channel, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Channel, String> nameColumn = new TableColumn<>("Naziv");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Channel, String> descriptionColumn = new TableColumn<>("Opis");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        table2.getColumns().addAll(idColumn, nameColumn, descriptionColumn);

        helloController.setTable2(table2);

        Label pretraga = new Label("Pretraga:");

        TextField searchField = new TextField();
        searchField.setPromptText("Pretraži kanale...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(channel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return channel.getName().toLowerCase().contains(lowerCaseFilter) ||
                        channel.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });

        table2.setItems(filteredData);



        TextField nameField = new TextField();
        nameField.setPromptText("Naziv");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Opis");

        Button addButton2 = new Button("Dodaj");
        addButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        addButton2.setOnMouseEntered(e -> {
            addButton2.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        addButton2.setOnMouseExited(e -> {
            addButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        addButton2.setOnAction(e -> {
            if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sva polja moraju biti popunjena");
                alert.show();
                return;
            }

            MainRepository.getInstance().put("name", nameField.getText());
            MainRepository.getInstance().put("description", descriptionField.getText());

            helloController.addChannel();
            nameField.clear();
            descriptionField.clear();
        });

        Button deleteButton = new Button("Obriši");
        deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        deleteButton.setOnMouseEntered(e -> {
            deleteButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        deleteButton.setOnMouseExited(e -> {
            deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        Button editButton = new Button("Izmeni");
        editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        editButton.setOnMouseEntered(e -> {
            editButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        editButton.setOnMouseExited(e -> {
            editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        Image image4 = new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image5 = new ImageView(image4);
        image5.setFitWidth(200);
        image5.setPreserveRatio(true);

        HBox inputLayout = new HBox(10,nameField, descriptionField, addButton2, editButton, deleteButton);
        VBox vbox = new VBox(10, pretraga, searchField, table2, inputLayout, image5);
        vbox.setStyle("-fx-background-color:white");
        return vbox;

    }
}

