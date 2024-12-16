package com.example.demo3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.List;


import static com.example.demo3.ApiChannel.getChannels;
import static com.example.demo3.ApiChannelAdd.addChannelById;
import static com.example.demo3.ApiClientAddUser.addUser;
import static com.example.demo3.ApiClientDeleteUser.deleteUserById;
import static com.example.demo3.ApiClientUpdateUser.updateUserById;
import static com.example.demo3.ApiClientUser.getUsers;

public class HelloApplication extends Application {



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Pokrećemo login stranicu
        primaryStage.setScene(createLoginScene(primaryStage));
        primaryStage.setTitle("RafBook Login");
        primaryStage.show();
    }

    // Metod za login scenu
    private Scene createLoginScene(Stage primaryStage) {
        Image logoImage = new Image(getClass().getResource("/images/raf.jpg").toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);
        Image icon = new Image(getClass().getResource("/images/raf.jpg").toExternalForm());
        primaryStage.getIcons().add(icon);
        logoImageView.setFitWidth(200);  // Postavljanje širine slike
        logoImageView.setPreserveRatio(true);  // Održavanje proporcija slike
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
                // Takođe, možeš obraditi greške u slučaju problema sa mrežom
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
        // Kreiranje TabPane-a
        TabPane tabPane = new TabPane();


        Tab firstTab = new Tab("Korisnici");
        firstTab.setClosable(false);
        firstTab.setContent(createClientTabContent());


        Tab secondTab = new Tab("Kanali");
        secondTab.setClosable(false);
        secondTab.setContent(createChannelTabContent());


        tabPane.getTabs().addAll(firstTab, secondTab);

        // Postavljanje scene
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("RafBook");
        primaryStage.show();
    }


    private VBox createClientTabContent() {
        TableView<Person> table = new TableView<>();

        TableColumn<Person, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Person, String> firstNameColumn = new TableColumn<>("Ime");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Person, String> lastNameColumn = new TableColumn<>("Prezime");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Person, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn);
        try {

            List<Person> users = getUsers();


            ObservableList<Person> userData = FXCollections.observableArrayList(users);
            table.setItems(userData);
        } catch (IOException e) {
            e.printStackTrace(); // Obrada greške

        }



        TextField searchField = new TextField();
        searchField.setPromptText("Pretraži klijente...");

        /*searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            userData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return person.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
                        person.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                        person.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });*/

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Ime");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Prezime");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Button addButton = new Button("Dodaj");
        addButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        addButton.setOnMouseEntered(e -> {
            addButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        addButton.setOnMouseExited(e -> {
            addButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        addButton.setOnAction(e -> {
            // Prikupljamo podatke iz TextField polja
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();

            // Kreiraj objekat sa prikupljenim podacima (npr. Person objekat)
            Person newPerson = new Person(firstName, lastName, email);

            // Pozovi metodu addChannelById sa odgovarajućim ID-em i novim podacima
            try {
                // Zameni "channelId" sa stvarnim ID-em koji trebaš koristiti
                int channelId = 1; // Primer ID-a
                boolean success = addUser(channelId, newPerson);

                // Proveri da li je operacija bila uspešna
                if (success) {
                    // Ako je uspešno, dodaj podatke u tabelu
                    table.getItems().add(newPerson);
                    // Očisti polja nakon dodavanja
                    firstNameField.clear();
                    lastNameField.clear();
                    emailField.clear();
                } else {
                    // Ako dodavanje nije uspelo, prikaži poruku o grešci
                    System.out.println("Error adding the person.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button deleteButton = new Button("Obriši");
        deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        deleteButton.setOnMouseEntered(e -> {
            deleteButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        deleteButton.setOnMouseExited(e -> {
            deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        deleteButton.setOnAction(e -> {
            // Dobijanje selektovanog korisnika iz tabele
            Person selectedPerson = table.getSelectionModel().getSelectedItem();

            if (selectedPerson != null) {
                // Potvrda za brisanje (opcionalno)
                System.out.println("Brisanje korisnika: " + selectedPerson.getFirstName());

                try {
                    boolean success = deleteUserById(selectedPerson.getId());

                    if (success) {
                        table.getItems().remove(selectedPerson);
                        System.out.println("Korisnik uspešno obrisan.");
                    } else {
                        System.out.println("Greška pri brisanju korisnika.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Nijedan korisnik nije selektovan.");
            }
        });



        Button editButton = new Button("Izmeni");
        editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        editButton.setOnMouseEntered(e -> {
            editButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        editButton.setOnMouseExited(e -> {
            editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        editButton.setOnAction(event -> {
            Person selectedPerson = table.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                try {
                    // Ažuriranje podataka iz TextField-a
                    selectedPerson.setFirstName(firstNameField.getText());
                    selectedPerson.setLastName(lastNameField.getText());
                    selectedPerson.setEmail(emailField.getText());

                    // Slanje PUT zahteva
                    boolean success = updateUserById(selectedPerson.getId(), selectedPerson);
                    if (success) {
                        // Osvježavanje tabele
                        table.refresh();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Image image2=new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image3=new ImageView(image2);
        image3.setFitWidth(200);
        image3.setPreserveRatio(true);

        HBox inputLayout = new HBox(10, firstNameField, lastNameField, emailField, addButton,editButton,deleteButton);
        VBox vbox = new VBox(10, new Label("Pretraga:"),searchField, table, inputLayout,image3);
        vbox.setStyle("-fx-background-color:white;");
        return vbox;
    }


    private VBox createChannelTabContent() {
        TableView<Channel> table = new TableView<>();


        TableColumn<Channel, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Channel, String> nameColumn = new TableColumn<>("Naziv");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<Channel, String> descriptionColumn = new TableColumn<>("Opis");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.getColumns().addAll(idColumn, nameColumn, descriptionColumn);
        try{
            List<Channel> channels = getChannels();

            ObservableList<HelloApplication.Channel> channelData = FXCollections.observableArrayList(channels);
            table.setItems(channelData);
        }catch (IOException e) {
            e.printStackTrace(); // Obrada greške

        }

        Label pretraga = new Label("Pretraga:");

        TextField searchField = new TextField();
        searchField.setPromptText("Pretraži kanale...");

        /*searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(channel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return channel.getName().toLowerCase().contains(lowerCaseFilter) ||
                        channel.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });*/


        TextField nameField = new TextField();
        nameField.setPromptText("Naziv");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Opis");

        Button addButton = new Button("Dodaj");
        addButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        addButton.setOnMouseEntered(e -> {
            addButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        addButton.setOnAction(e -> {
            // Prikupljamo podatke iz TextField polja
            String name = nameField.getText();
            String description = descriptionField.getText();

            // Kreiraj objekat sa prikupljenim podacima (npr. Channel objekat)
            Channel newChannel = new Channel(name, description);

            // Pozovi metodu addChannelById sa odgovarajućim ID-em i novim podacima
            try {
                // Zameni "channelId" sa stvarnim ID-em koji trebaš koristiti
                int channelId = 1; // Primer ID-a
                boolean success = addChannelById(channelId, newChannel);

                // Proveri da li je operacija bila uspešna
                if (success) {
                    // Ako je uspešno, dodaj podatke u tabelu
                    table.getItems().add(newChannel);
                    // Očisti polja nakon dodavanja
                    nameField.clear();
                    descriptionField.clear();
                } else {
                    // Ako dodavanje nije uspelo, prikaži poruku o grešci
                    System.out.println("Error adding the channel.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        addButton.setOnMouseExited(e -> {
            addButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        Button deleteButton = new Button("Obriši");
        deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        deleteButton.setOnMouseEntered(e -> {
            deleteButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        deleteButton.setOnMouseExited(e -> {
            deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        /*deleteButton.setOnAction(e -> {
            Channel selectedChannel = table.getSelectionModel().getSelectedItem();
            if (selectedChannel != null) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Potvrda brisanja");
                confirmationAlert.setHeaderText("Da li ste sigurni da želite da obrišete kanal?");
                confirmationAlert.setContentText("Kanal: " + selectedChannel.getName());

                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        data.remove(selectedChannel);
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Morate odabrati kanal za brisanje!");
                alert.show();
            }
        });*/

        Button editButton = new Button("Izmeni");
        editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        editButton.setOnMouseEntered(e -> {
            editButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        editButton.setOnMouseExited(e -> {
            editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        editButton.setOnAction(e -> {
            Channel selectedChannel = table.getSelectionModel().getSelectedItem();
            if (selectedChannel != null) {
                String newName = nameField.getText();
                String newDescription = descriptionField.getText();

                if (!newName.isEmpty() && !newDescription.isEmpty()) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Potvrda izmene");
                    confirmationAlert.setHeaderText("Da li ste sigurni da želite da izmenite kanal?");
                    confirmationAlert.setContentText("Kanal: " + selectedChannel.getName());

                    confirmationAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            selectedChannel.setName(newName);
                            selectedChannel.setDescription(newDescription);
                            table.refresh();
                            nameField.clear();
                            descriptionField.clear();
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Sva polja moraju biti popunjena za izmenu!");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Morate odabrati kanal za izmenu!");
                alert.show();
            }
        });

        Image image4 = new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image5 = new ImageView(image4);
        image5.setFitWidth(200);
        image5.setPreserveRatio(true);

        HBox inputLayout = new HBox(10,nameField, descriptionField, addButton, editButton, deleteButton);
        VBox vbox = new VBox(10, pretraga, searchField, table, inputLayout, image5);
        vbox.setStyle("-fx-background-color:white");
        return vbox;
    }



    public static class Person {
        private int id;
        private String firstName;
        private String lastName;
        private String email;

        public Person(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        // Getter i setter za ID
        public int getId() {
            return id;
        }

        public void setId(int id){
            this.id=id;
        }


        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Channel {
        private int id;
        private String name;
        private String description;

        public Channel(String name, String description) {

            this.name = name;
            this.description = description;
        }


        public int getId() {
            return id;
        }




        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
