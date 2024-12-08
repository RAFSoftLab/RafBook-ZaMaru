package com.example.demo3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Pokrećemo login stranicu
        primaryStage.setScene(createLoginScene(primaryStage));
        primaryStage.setTitle("RafBook");
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
            // Provera
            if (usernameField.getText().equalsIgnoreCase("admin") && passwordField.getText().equals("password")) {
                // Ako su podaci tačni, prešli smo na sledeću stranicu
                goToNextPage(primaryStage);
            } else {
                //Ako korisnik nije admin ili je losa lozinka prikazi upozorenje
                Alert alert = new Alert(Alert.AlertType.ERROR, "Pogrešno korisničko ime ili lozinka.");
                alert.show();
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
        primaryStage.setTitle("Tabbed Interface");
        primaryStage.show();
    }


    private VBox createClientTabContent() {
        TableView<Person> table = new TableView<>();
        ObservableList<Person> data = FXCollections.observableArrayList(
                new Person("Ivana", "Jankovic", "ivanajankovic1302@gmail.com")

        );

        FilteredList<Person> filteredData = new FilteredList<>(data, p -> true);

        TableColumn<Person, String> firstNameColumn = new TableColumn<>("Ime");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Person, String> lastNameColumn = new TableColumn<>("Prezime");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Person, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn);
        table.setItems(filteredData);

        TextField searchField = new TextField();
        searchField.setPromptText("Pretraži klijente...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return person.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
                        person.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                        person.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });

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
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();

            if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()) {

                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Potvrda dodavanja");
                confirmationAlert.setHeaderText("Da li ste sigurni da želite da dodate novog korisnika?");
                confirmationAlert.setContentText("Korisnik: " + firstName + " " + lastName);

                // Ako korisnik klikne OK
                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Dodavanje korisnika u listu
                        data.add(new Person(firstName, lastName, email));
                        firstNameField.clear();
                        lastNameField.clear();
                        emailField.clear();
                    } else {
                        // Otkazivanje dodavanja
                        Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                        cancelAlert.setTitle("Dodavanje otkazano");
                        cancelAlert.setHeaderText("Korisnik nije dodat.");
                        cancelAlert.show();
                    }
                });
            } else {
                // Ako polja nisu popunjena, prikazuje se upozorenje
                Alert alert = new Alert(Alert.AlertType.WARNING, "Sva polja moraju biti popunjena!");
                alert.show();
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
            Person selectedPerson = table.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                // Kreiranje potvrde
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Potvrda");
                confirmationAlert.setHeaderText("Da li ste sigurni da želite da obrišete korisnika?");
                confirmationAlert.setContentText("Korisnik: " + selectedPerson.getFirstName() + " " + selectedPerson.getLastName());

                // Ako korisnik klikne OK
                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Brisanje korisnika
                        data.remove(selectedPerson);
                    } else {
                        //Korisnik otkazao
                        Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                        cancelAlert.setTitle("Otkazano");
                        cancelAlert.setHeaderText("Brisanje je otkazano.");
                        cancelAlert.show();
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Morate odabrati korisnika za brisanje!");
                alert.show();
            }
        });


        // Dugme za izmenu korisnika
        Button editButton = new Button("Izmeni");
        editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        editButton.setOnMouseEntered(e -> {
            editButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        editButton.setOnMouseExited(e -> {
            editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        editButton.setOnAction(e -> {
            Person selectedPerson = table.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                // Kreiraj potvrdu
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Potvrda");
                confirmationAlert.setHeaderText("Da li ste sigurni da želite da izmenite korisnika?");
                confirmationAlert.setContentText("Korisnik: " + selectedPerson.getFirstName() + " " + selectedPerson.getLastName());

                // Ako korisnik klikne OK
                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Izmena korisnika
                        selectedPerson.setFirstName(firstNameField.getText());
                        selectedPerson.setLastName(lastNameField.getText());
                        selectedPerson.setEmail(emailField.getText());
                        table.refresh(); // Osvježavanje prikaza tabele
                    } else {
                        //Otkazivanje
                        Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                        cancelAlert.setTitle("Otkazano");
                        cancelAlert.setHeaderText("Izmena je otkazana.");
                        cancelAlert.show();
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Morate odabrati korisnika za izmenu!");
                alert.show();
            }
        });

        Image image2=new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image3=new ImageView(image2);
        image3.setFitWidth(200);  // Postavljanje širine slike
        image3.setPreserveRatio(true);

        HBox inputLayout = new HBox(10, firstNameField, lastNameField, emailField, addButton,editButton,deleteButton);
        VBox vbox = new VBox(10, new Label("Pretraga:"), searchField, table, inputLayout,image3);
        vbox.setStyle("-fx-background-color:white;");
        return vbox;
    }


    private VBox createChannelTabContent() {
        TableView<Channel> table = new TableView<>();


        TableColumn<Channel, String> nameColumn = new TableColumn<>("Naziv");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Channel, String> descriptionColumn = new TableColumn<>("Opis");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));


        table.getColumns().addAll(nameColumn, descriptionColumn);

        ObservableList<Channel> data = FXCollections.observableArrayList(
                new Channel("General", "Opšti kanal za diskusiju"),
                new Channel("Announcements", "Kanal za objave")

        );
        table.setItems(data);

        FilteredList<Channel> filteredData = new FilteredList<>(data, p -> true);
        table.setItems(filteredData);

        Label pretraga=new Label("Pretraga:");


        TextField searchField = new TextField();
        searchField.setPromptText("Pretraži kanale...");


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(channel -> {
                // Ako je polje za pretragu prazno, prikazujemo sve
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return channel.getName().toLowerCase().contains(lowerCaseFilter)
                        || channel.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });


        TextField nameField = new TextField();
        nameField.setPromptText("Naziv");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Opis");

        Button addButton = new Button("Dodaj");
        addButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        addButton.setOnMouseEntered(e -> {
            addButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        addButton.setOnMouseExited(e -> {
            addButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            if (!name.isEmpty() && !description.isEmpty()) {
                // Kreiraj prozor za potvrdu
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Da li ste sigurni da želite da dodate kanal?");
                confirmationAlert.setHeaderText("Potvrda dodavanja");
                confirmationAlert.setTitle("Potvrda");


                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    data.add(new Channel(name, description));
                    nameField.clear();
                    descriptionField.clear();
                }
            } else {

                Alert warningAlert = new Alert(Alert.AlertType.WARNING, "Sva polja moraju biti popunjena!");
                warningAlert.setHeaderText("Upozorenje");
                warningAlert.setTitle("Greška");
                warningAlert.show();
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
            Channel selectedChannel = table.getSelectionModel().getSelectedItem();
            if (selectedChannel != null) {
                // Potvrda brisanja
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
        });

        // Dugme za izmenu
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
                // Unos novih vrednosti
                String newName = nameField.getText();
                String newDescription = descriptionField.getText();

                if (!newName.isEmpty() && !newDescription.isEmpty()) {
                    // Potvrda izmene
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Potvrda izmene");
                    confirmationAlert.setHeaderText("Da li ste sigurni da želite da izmenite kanal?");
                    confirmationAlert.setContentText("Kanal: " + selectedChannel.getName());

                    confirmationAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            selectedChannel.setname(newName);
                            selectedChannel.setdescription(newDescription);
                            table.refresh(); // Osvježavanje prikaza tabele
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
        Image image4=new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image5=new ImageView(image4);
        image5.setFitWidth(200);  // Postavljanje širine slike
        image5.setPreserveRatio(true);



        HBox inputLayout = new HBox(10, nameField, descriptionField, addButton,editButton,deleteButton);
        VBox vbox = new VBox(10,pretraga,searchField, table, inputLayout,image5);
        vbox.setStyle("-fx-background-color:white");
        return vbox;
    }


    public static class Person {
        private String firstName;
        private String lastName;
        private String email;

        public Person(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
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
    public static class Channel{
        private String name;
        private String description;
        public Channel(String name,String description){
            this.name=name;
            this.description=description;
        }
        public String getName(){
            return name;
        }
        public String getDescription(){
            return description;
        }
        public void setname(String name){
            this.name=name;
        }
        public void setdescription(String description){
            this.description=description;
        }

    }
}
