package com.example.demo3.view;

import com.example.demo3.Model.Person;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class PopUpWindowView {

    TableView<String> tableView = new TableView<>();
    public void showPopupWindow() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Uloge");

        TableColumn<String, String> column1 = new TableColumn<>("Uloge");
        column1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));


        tableView.getColumns().addAll(column1);

        TextField inputField = new TextField();
        inputField.setPromptText("Upisite ulogu...");

        Button btnAddRole = new Button("Dodaj novu ulogu");
        btnAddRole.setOnAction(e -> {
            String newRole = inputField.getText().trim();
            if (!newRole.isEmpty()) {
                tableView.getItems().add(newRole);
                inputField.clear();
            }
        });
        btnAddRole.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        btnAddRole.setOnMouseEntered(e -> {
            btnAddRole.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        btnAddRole.setOnMouseExited(e -> {
            btnAddRole.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        Button btnDeleteRole = new Button("Obrisi ulogu");
        btnDeleteRole.setOnAction(e -> {
            String selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                tableView.getItems().remove(selectedItem);
            }
        });

        btnDeleteRole.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        btnDeleteRole.setOnMouseEntered(e -> {
            btnDeleteRole.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });


        btnDeleteRole.setOnMouseExited(e -> {
            btnDeleteRole.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });



        HBox controlsLayout = new HBox(10, inputField, btnAddRole);
        VBox popupLayout = new VBox(10, tableView, controlsLayout, btnDeleteRole);
        popupLayout.setStyle("-fx-padding: 20;");
        popupLayout.setStyle("-fx-background-color:white;");

        Scene popupScene = new Scene(popupLayout, 400, 400);
        popupStage.setScene(popupScene);


        popupStage.showAndWait();
    }
    public void updateRolesTable(List<String> roles) {
        tableView.setItems(FXCollections.observableArrayList(roles));

    }

}
