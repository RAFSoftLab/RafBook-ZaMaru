package com.example.demo3.view;

import com.example.demo3.Controller.ApiClientUser;
import com.example.demo3.HelloController;
import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.Person;
import com.example.demo3.repository.MainRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.demo3.Controller.ApiClientUser.getUsers;

public class UserView {
    HelloController helloController=new HelloController();
    PopUpWindowView popUpWindowView=new PopUpWindowView();

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

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                List<String> roles = newSelection.getRole();
                System.out.println("Role selektovanog korisnika: " + roles);

                // Ažuriraj tabelu uloga u PopUpWindowView
                if (popUpWindowView != null) {
                    popUpWindowView.updateRolesTable(roles);
                }
            } else {
                System.out.println("Nijedan korisnik nije selektovan.");
                if (popUpWindowView != null) {
                    popUpWindowView.updateRolesTable(Collections.emptyList());
                }
            }
        });



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
                    emailField.getText().isEmpty() /*||
                    roleField.getText().isEmpty()*/) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sva polja moraju biti popunjena");
                alert.show();
                return;
            }
            MainRepository.getInstance().put("firstName", firstNameField.getText());
            MainRepository.getInstance().put("lastName", lastNameField.getText());
            MainRepository.getInstance().put("password", usernameField.getText());
            MainRepository.getInstance().put("email", emailField.getText());
            //MainRepository.getInstance().put("role", roleField.getText());
            MainRepository.getInstance().put("mac", "no mac address");

            helloController.addPerson();
            filteredData.setPredicate(filteredData.getPredicate());

            firstNameField.clear();
            lastNameField.clear();
            usernameField.clear();
            emailField.clear();
            //roleField.clear();

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
            NewUserDTO dto;

            if (selectedPerson == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nijedan red nije selektovan za ažuriranje!");
                alert.show();
                return;
            }
            else{
                dto=helloController.convertPersonToNewUserDTO(selectedPerson);
            }


            try {

                selectedPerson.setFirstName(firstNameField.getText());
                selectedPerson.setLastName(lastNameField.getText());
                selectedPerson.setUsername(usernameField.getText());
                selectedPerson.setEmail(emailField.getText());
                selectedPerson.setRole(Arrays.asList(roleField.getText().split(", ")));


                boolean success = ApiClientUser.editUser(dto);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Korisnik uspešno ažuriran!");
                    alert.show();

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

        Button roleButton=new Button("Uloge");
        roleButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        roleButton.setOnMouseEntered(e -> {
            roleButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        roleButton.setOnMouseExited(e -> {
            roleButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        roleButton.setOnAction(e -> popUpWindowView.showPopupWindow());

        Image image2=new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image3=new ImageView(image2);
        image3.setFitWidth(200);
        image3.setPreserveRatio(true);

        HBox inputLayout = new HBox(10, firstNameField, lastNameField,usernameField, emailField,roleField, addButton,editButton,deleteButton,roleButton);
        VBox vbox = new VBox(10, new Label("Pretraga:"),searchField, table, inputLayout,image3);
        vbox.setStyle("-fx-background-color:white;");
        return vbox;
    }
}