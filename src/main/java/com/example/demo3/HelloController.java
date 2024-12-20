package com.example.demo3;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import java.util.Arrays;

import static com.example.demo3.Controller.ApiClientUser.*;

public class HelloController {

    //for user
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField roleField;

    @FXML
    private Button addButton;

    @FXML
    private Button addButton2;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TableView<Person> table;

    @FXML
    private TableColumn<Person, Integer> idColumn;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private TableColumn<Person, String> usernameColumn;

    @FXML
    private TableColumn<Person, String> emailColumn;

    @FXML
    private TableColumn<Person, String> roleColumn;

    private ObservableList<Person> userData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(cellData -> {
            List<String> roles = cellData.getValue().getRole();
            return new SimpleStringProperty(String.join(", ", roles));
        });


        table.setItems(userData);


        addButton.setOnAction(event -> addPerson());
        deleteButton.setOnAction(event -> deletePerson());
        editButton.setOnAction(event -> editPerson());

    }


    public void addPerson() {
        if (firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() ||
                usernameField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                roleField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sva polja moraju biti popunjena");
            alert.show();
            return; }


        Person newPerson = new Person(
                firstNameField.getText(),
                lastNameField.getText(),
                usernameField.getText(),
                emailField.getText(),
                Arrays.asList(roleField.getText().split(","))
        );


        try {
            boolean success = addUser(newPerson.getId(), newPerson);
            if (success) {

                userData.add(newPerson);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Korisnik je uspešno dodat.");
                alert.show();

                firstNameField.clear();
                lastNameField.clear();
                usernameField.clear();
                emailField.clear();
                roleField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom dodavanja korisnika u bazu.");
                alert.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom komunikacije sa serverom.");
            alert.show();
        }
    }

    public void deletePerson(){
        Person selectedPerson = table.getSelectionModel().getSelectedItem();

        if (selectedPerson == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Morate selektovati korisnika za brisanje.");
            alert.show();
            return;
        }


        userData.remove(selectedPerson);


        try {
            boolean success = deleteUserById(selectedPerson.getId());
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Korisnik je uspešno obrisan.");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom brisanja korisnika.");
                alert.show();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Greška pri konekciji sa serverom.");
            alert.show();
            e.printStackTrace();
        }

    }
     public void editPerson(){

             Person selectedPerson = table.getSelectionModel().getSelectedItem();

             if (selectedPerson == null) {
                 Alert alert = new Alert(Alert.AlertType.WARNING, "Morate selektovati korisnika za izmenu.");
                 alert.show();
                 return;
             }


             firstNameField.setText(selectedPerson.getFirstName());
             lastNameField.setText(selectedPerson.getLastName());
             usernameField.setText(selectedPerson.getUsername());
             emailField.setText(selectedPerson.getEmail());
             roleField.setText(String.join(",", selectedPerson.getRole()));


             addButton.setOnAction(event -> {
                 selectedPerson.setFirstName(firstNameField.getText());
                 selectedPerson.setLastName(lastNameField.getText());
                 selectedPerson.setUsername(usernameField.getText());
                 selectedPerson.setEmail(emailField.getText());
                 selectedPerson.setRole(Arrays.asList(roleField.getText().split(",")));


                 table.refresh();

                 try {
                     boolean success = updateUserById(selectedPerson.getId(), selectedPerson);
                     if (success) {
                         Alert alert = new Alert(Alert.AlertType.INFORMATION, "Podaci su uspešno ažurirani.");
                         alert.show();
                     } else {
                         Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške pri ažuriranju korisnika.");
                         alert.show();
                     }
                 } catch (IOException e) {
                     Alert alert = new Alert(Alert.AlertType.ERROR, "Greška pri konekciji sa serverom.");
                     alert.show();
                     e.printStackTrace();
                 }


                 firstNameField.clear();
                 lastNameField.clear();
                 usernameField.clear();
                 emailField.clear();
                 roleField.clear();
             });
         }




}