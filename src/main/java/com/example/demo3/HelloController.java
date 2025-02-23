package com.example.demo3;

import com.example.demo3.Controller.ApiChannel;
import com.example.demo3.Controller.ApiClientCategory;
import com.example.demo3.Controller.ApiClientUser;
import com.example.demo3.Controller.AuthClient;
import com.example.demo3.Model.*;
import com.example.demo3.repository.MainRepository;
import com.example.demo3.view.View;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.example.demo3.Controller.ApiClientUser.*;

public class HelloController {


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

    @FXML
    private TableView<Channel> table2;

    @FXML
    private TableColumn<Channel, String> nameColumn;

    @FXML
    private TableColumn<Channel, String> descriptionColumn;


    private ObservableList<Channel> channelData = FXCollections.observableArrayList();





    ApiChannel apiChannel = new ApiChannel();


    ApiClientUser apiClientUser = new ApiClientUser();


    private void refreshUserData() {
        try {
            List<Person> users = ApiClientUser.getUsers();
            userData.clear();
            userData.addAll(users);
            table.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error loading users: " + e.getMessage());
            alert.show();
        }
    }

    private void refreshChannelData() {
        try {
            List<Channel> channels = ApiChannel.getChannels();
            channelData.clear();
            channelData.addAll(channels);
            table2.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error loading channels: " + e.getMessage());
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


        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        if (channelData == null) {
            channelData = FXCollections.observableArrayList();
        }

        table2.setItems(channelData);
        refreshChannelData();

    }

    public void setTable(TableView<Person> table) {
        this.table = table;
    }

    public void setTable2(TableView<Channel> table2) {
        this.table2 = table2;
    }

    public List<String> getUpdatedRolesForUser(int userId) {
        return new ArrayList<>();
    }

    public void addPerson() {
        try {
            String firstName = MainRepository.getInstance().get("firstName");
            String lastName = MainRepository.getInstance().get("lastName");
            String username = MainRepository.getInstance().get("password");
            String email = MainRepository.getInstance().get("email");
            String mac = MainRepository.getInstance().get("mac");
            String role=MainRepository.getInstance().get("role");



            NewUserDTO newUser = new NewUserDTO();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setUsername(username);
            newUser.setPassword(username);
            newUser.setEmail(email.toLowerCase());
            newUser.setMacAddress(mac);
            newUser.setRole(role);



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

    public static NewUserDTO convertPersonToNewUserDTO(Person person) {
        if (person == null) {
            return null;
        }

        NewUserDTO dto = new NewUserDTO();


        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setUsername(person.getUsername());
        dto.setEmail(person.getEmail());
        dto.setPassword(person.getUsername());
        dto.setMacAddress("no mac address");
        return dto;
    }



    public void deleteRoleFromTable(TableView<String> tableView, int userId) {
        String selectedRole = tableView.getSelectionModel().getSelectedItem();

        if (selectedRole != null) {
            try {
                boolean success = ApiClientUser.deleteRoleFromUser(userId, selectedRole);

                if (success) {

                    tableView.getItems().remove(selectedRole);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Uloga je uspešno obrisana.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom brisanja uloge iz baze.");
                    alert.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Greška u komunikaciji sa serverom: " + e.getMessage());
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nijedna uloga nije selektovana.");
            alert.show();
        }
    }

    public void addRoleToTable(TableView<String> tableView, TextField inputField, int userId) {
        String newRole = inputField.getText().trim();

        if (!newRole.isEmpty()) {

            try {
                boolean success = addRoleFromUser(userId, newRole);
                if (success) {
                    tableView.getItems().add(newRole);
                    inputField.clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Uloga je uspešno dodata.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Dodavanje uloge nije uspelo. Proverite podatke.");
                    alert.show();
                }
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Neispravan unos: " + e.getMessage());
                alert.show();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom komunikacije sa serverom: " + e.getMessage());
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Morate uneti ulogu.");
            alert.show();
        }
    }

    public void addChannel () {
            try {
                String name = MainRepository.getInstance().get("name");
                String description = MainRepository.getInstance().get("description");
                String category = MainRepository.getInstance().get("category");

                NewChannelDTO newChannel = new NewChannelDTO();
                newChannel.setName(name);
                newChannel.setDescription(description);
                newChannel.setCategory(category);

                boolean success = ApiChannel.addChannel(newChannel);

                if (success) {
                    Alert alert1=new Alert(Alert.AlertType.CONFIRMATION,"Channel added successfully");
                    System.out.println("Channel added successfully, refreshing table...");

                    List<Channel> channels = ApiChannel.getChannels();
                    System.out.println("Fetched " + channels.size() + " channels");

                    channelData.clear();

                    channelData = FXCollections.observableArrayList(channels);

                    if (table2 != null) {
                        table2.setItems(null);
                        table2.setItems(channelData);
                        table2.refresh();
                        System.out.println("Table refreshed with " + table2.getItems().size() + " items");
                    } else {
                        System.out.println("table2 is null!");
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Channel successfully added!");
                    alert.show();
                } else {
                    System.out.println("Channel addition failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Error adding channel: " + e.getMessage());
                alert.show();
            }
        }

        public ObservableList<Channel> getChannelData () {
            return channelData;
        }


        public ObservableList<Person> getUserData () {
            return userData;
        }

    public void removeRoleFromChannel(TableView<RolePermissionDTO> rolesTableView, long channelId) {
        RolePermissionDTO selectedRoleDTO = rolesTableView.getSelectionModel().getSelectedItem();
        System.out.println("selektovani korisnici:"+selectedRoleDTO);

        if (selectedRoleDTO != null) {

            String selectedRole = selectedRoleDTO.getRole();
            System.out.println("ROLA JE "+selectedRole);

            if (selectedRole != null && !selectedRole.isEmpty()) {
                try {
                    List<String> rolesToRemove = new ArrayList<>();
                    rolesToRemove.add(selectedRole);

                    boolean success = ApiChannel.removeRolesFromChannel(channelId, rolesToRemove);

                    if (success) {
                        rolesTableView.getItems().remove(selectedRoleDTO);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Uloga je uspešno uklonjena sa kanala.");
                        alert.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Uklanjanje uloge sa kanala nije uspelo. Proverite podatke.");
                        alert.show();
                    }
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Neispravan unos: " + e.getMessage());
                    alert.show();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom komunikacije sa serverom: " + e.getMessage());
                    alert.show();
                }
            } else {
                // Show warning if no role is selected
                Alert alert = new Alert(Alert.AlertType.WARNING, "Morate selektovati ulogu za uklanjanje.");
                alert.show();
            }
        } else {
            // Show warning if no role is selected
            Alert alert = new Alert(Alert.AlertType.WARNING, "Morate selektovati ulogu za uklanjanje.");
            alert.show();
        }
    }

    public void addRoleToChannel(TableView<String> tableView, TextField inputField, long channelId) {
        String newRole = inputField.getText().trim();

        if (!newRole.isEmpty()) {
            try {
                List<String> roles = new ArrayList<>();
                roles.add(newRole);

                boolean success = ApiChannel.addRolesToChannel(channelId, roles);

                if (success) {
                    tableView.getItems().add(newRole);
                    inputField.clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Uloga je uspešno dodata kanalu.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Dodavanje uloge kanalu nije uspelo. Proverite podatke.");
                    alert.show();
                }
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Neispravan unos: " + e.getMessage());
                alert.show();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom komunikacije sa serverom: " + e.getMessage());
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Morate uneti ulogu.");
            alert.show();
        }
    }

    public void addCategory() {
        try {
            String name = MainRepository.getInstance().get("name");
            String description = MainRepository.getInstance().get("description");

            NewCategoryDTO newCategory = new NewCategoryDTO();
            newCategory.setName(name);
            newCategory.setDescription(description);

            boolean success = ApiClientCategory.addCategory(newCategory);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Category added successfully");
                alert.show();
                System.out.println("Uspešno dodata kategorija");
            }
        } catch (IOException e) {
            Alert alert3 = new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage());
            alert3.showAndWait();
            System.err.println("Greška prilikom dodavanja kategorije: " + e.getMessage());
        }
    }

}