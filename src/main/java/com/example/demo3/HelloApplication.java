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

        // Dugme za login
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

    // Metoda koja se poziva kad korisnik uspešno loginuje
    private void goToNextPage(Stage primaryStage) {
        // Tabela
        TableView<Person> table = new TableView<>();


        ObservableList<Person> data = FXCollections.observableArrayList(
                new Person("Marko", "Marković", "marko@mail.com"),
                new Person("Jovana", "Jovanović", "jovana@mail.com"),
                new Person("Nikola", "Nikolić", "nikola@mail.com")
        );

        FilteredList<Person> filteredData = new FilteredList<>(data, p -> true);

        // Kolone tabele
        TableColumn<Person, String> firstNameColumn = new TableColumn<>("Ime");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));


        TableColumn<Person, String> lastNameColumn = new TableColumn<>("Prezime");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Person, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));


        //Dodavanje kolona
        table.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn);
        table.setItems(filteredData);

        // Polje za pretragu
        TextField searchField = new TextField();
        searchField.setPromptText("Pretraži klijente...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Prikazujemo sve ako nema unosa
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return person.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
                        person.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                        person.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });

        //Unos novog korisnika
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Ime");


        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Prezime");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        // Dugme za dodavanje korisnika
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
                // Kreiraj potvrdu
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Potvrda");
                confirmationAlert.setHeaderText("Da li ste sigurni da želite da dodate korisnika?");
                confirmationAlert.setContentText("Korisnik: " + firstName + " " + lastName);

                // Ako korisnik klikne OK
                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Dodaj korisnika u listu
                        data.add(new Person(firstName, lastName, email));
                        firstNameField.clear();
                        lastNameField.clear();
                        emailField.clear();
                    } else {
                        //Korisnik otkazao
                        Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                        cancelAlert.setTitle("Otkazano");
                        cancelAlert.setHeaderText("Dodavanje je otkazano.");
                        cancelAlert.show();
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Sva polja moraju biti popunjena!");
                alert.show();
            }
        });


        // Dugme za brisanje korisnika
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

        // Layout za pretragu, polja i dugmad
        Image image2=new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image3=new ImageView(image2);
        image3.setFitWidth(200);  // Postavljanje širine slike
        image3.setPreserveRatio(true);
        HBox searchLayout = new HBox(10, new Label("Pretraga:"), searchField);
        HBox inputLayout = new HBox(10, firstNameField, lastNameField, emailField, addButton, deleteButton, editButton);
        VBox vbox = new VBox(10, searchLayout, table, inputLayout,image3);
        vbox.setStyle("-fx-background-color:white");

        Scene tableScene = new Scene(vbox, 600, 400);

        // Postavljamo novu scenu
        primaryStage.setScene(tableScene);
        primaryStage.setTitle("Data");

    }

    // Klasa koja predstavlja podatke za tabelu
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
}
