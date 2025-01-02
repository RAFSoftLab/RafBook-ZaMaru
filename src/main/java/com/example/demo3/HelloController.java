package com.example.demo3;
import com.example.demo3.Controller.ApiChannel;
import com.example.demo3.Controller.ApiClientUser;
import com.example.demo3.Controller.AuthClient;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.NewChannelDTO;
import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.Person;
import com.example.demo3.repository.MainRepository;
import com.example.demo3.view.View;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import java.util.Arrays;
import java.util.Optional;

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




    ApiClientUser apiClientUser = new ApiClientUser();
    ApiChannel apichannel=new ApiChannel();

    private void refreshUserData() {
        try {
            List<Person> users = ApiClientUser.getUsers();
            userData.clear();
            userData.addAll(users);
            table.refresh(); // Force table refresh
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error loading users: " + e.getMessage());
            alert.show();
        }
    }

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
        refreshUserData();

        addButton.setOnAction(event -> addPerson());
        deleteButton.setOnAction(event -> deletePerson(table));
        editButton.setOnAction(event -> editPerson());

    }

    public void setTable(TableView<Person> table) {
        this.table = table;
    }

    public static boolean createUser(String firstName, String lastName, String username,
                                     String email, String role, String mac) throws IOException {
        // Create  NewUserDTO or object the API expects
        // Make the HTTP POST request to create the user
        // Return true if successful, false otherwise

        // Example implementation (adjust according to your API):
        URL url = new URL("your-api-endpoint/users");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + AuthClient.getToken());
        conn.setDoOutput(true);

        String jsonInputString = String.format(
                "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"username\": \"%s\", " +
                        "\"email\": \"%s\", \"role\": \"%s\", \"mac\": \"%s\"}",
                firstName, lastName, username, email, role, mac);

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
    }


    // Keep as void:
    public void addPerson() {
        try {
            String firstName = MainRepository.getInstance().get("firstName");
            String lastName = MainRepository.getInstance().get("lastName");
            String username = MainRepository.getInstance().get("password");
            String email = MainRepository.getInstance().get("email");
            String role = MainRepository.getInstance().get("role");
            String mac = MainRepository.getInstance().get("mac");

            NewUserDTO newUser = new NewUserDTO();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setUsername(username);
            newUser.setPassword(username);
            newUser.setEmail(email.toLowerCase());
            newUser.setRole("ADMIN");
            newUser.setMacAddress(mac);

            boolean success = ApiClientUser.addUser(newUser);

            if (success) {
                List<Person> users = ApiClientUser.getUsers();
                userData = FXCollections.observableArrayList(users);
                table.setItems(userData);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User successfully added!");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error adding person: " + e.getMessage());
            alert.show();
        }
    }



    public void deletePerson(TableView<Person> table) {
        Person selectedUser = table.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Morate selektovati korisnika za brisanje.");
            alert.show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Da li ste sigurni da želite obrisati korisnika?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            boolean isDeleted = apiClientUser.deleteUser(selectedUser);
            if (isDeleted) {
                // Refresh the table data from the server
                try {
                    List<Person> users = ApiClientUser.getUsers();
                    table.setItems(FXCollections.observableArrayList(users));
                    table.refresh();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Korisnik je uspešno obrisan.");
                    successAlert.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Brisanje nije uspelo. Proverite server.");
                errorAlert.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom komunikacije sa serverom.");
            errorAlert.show();
        }
    }
    public void editPerson() {
        //TBD
    }

    public ObservableList<Person> getUserData() {
        return userData;
    }


}