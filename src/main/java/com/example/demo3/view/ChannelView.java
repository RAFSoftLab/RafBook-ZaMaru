package com.example.demo3.view;

import com.example.demo3.Controller.ApiStudies;
import com.example.demo3.Controller.ApiStudyProgram;
import com.example.demo3.HelloController;
import com.example.demo3.Model.*;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo3.Controller.ApiChannel.editChannel;
import static com.example.demo3.Controller.ApiChannel.getChannels;

public class ChannelView {
    HelloController helloController=new HelloController();
    PopUpChannelView popUpChannelView=new PopUpChannelView();


    public VBox createChannelTabContent() {
        TableView<Channel> table2 = new TableView<>();
        table2.setId("table2");

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

        TableColumn<Channel, String> roleColumn = new TableColumn<>("Uloge");
        roleColumn.setCellValueFactory(cellData -> {
            List<RolePermissionDTO> roles = cellData.getValue().getRolePermissionDTOList();
            String rolesAsString = roles.stream()
                    .map(RolePermissionDTO::getRole)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(rolesAsString);
        });

        table2.getColumns().addAll(idColumn, nameColumn, descriptionColumn,roleColumn);

        helloController.setTable2(table2);

        popUpChannelView.setMainTableAndData(table2,channelData,filteredData);

        table2.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedChannelId = newSelection.getId();
                List<RolePermissionDTO> roles = newSelection.getRolePermissionDTOList();

                System.out.println("Uloge selektovanog kanala: " + roles);

                if (popUpChannelView != null) {
                    popUpChannelView.updateRolesTable(roles);

                    popUpChannelView.setChannelId(selectedChannelId);
                }
            } else {
                System.out.println("Nijedan kanal nije selektovan.");
                if (popUpChannelView != null) {
                    popUpChannelView.updateRolesTable(Collections.emptyList());
                }
            }
        });

        Label kanali=new Label("Kanali");
        kanali.setStyle("-fx-text-fill:#173669;-fx-font-weight:bold;");
        Label kategorije=new Label("Kategorije");
        kategorije.setStyle("-fx-text-fill:#173669;-fx-font-weight:bold;");

        Separator separator = new Separator();
        separator.setPrefWidth(150);

        Separator separator2 = new Separator();
        separator2.setPrefWidth(150);

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
        nameField.setId("name");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Opis");
        descriptionField.setId("description");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Naziv");
        categoryField.setId("catName");

        TextField categoryField2 = new TextField();
        categoryField2.setPromptText("Opis");
        categoryField2.setId("catDesc");

        ComboBox<String> comboBox4 = new ComboBox<>();
        comboBox4.setId("comboBox4");
        ComboBox<String> comboBox5 = new ComboBox<>();
        comboBox5.setId("comboBox5");

        comboBox4.setStyle("-fx-background-color: white; -fx-padding: 0 5px;-fx-border-color: #173669;-fx-border-radius: 5px;-fx-text-fill:#173669;");
        comboBox5.setStyle("-fx-background-color: white; -fx-padding: 0 5px;-fx-border-color: #173669;-fx-border-radius: 5px;-fx-text-fill:#173669;");

        try {
            List<StudiesDTO> studiesList = ApiStudies.getStudies();

            comboBox4.getItems().addAll(
                    studiesList.stream().map(StudiesDTO::getName).collect(Collectors.toList())
            );

            comboBox4.valueProperty().addListener((observable, oldValue, newValue) -> {
                comboBox5.getItems().clear();

                if (newValue != null) {
                    try {
                        List<StudyProgram> studyPrograms = ApiStudyProgram.getStudyPrograms(newValue);
                        System.out.println("Selected study: " + newValue);
                        System.out.println(studyPrograms);

                        List<String> programNames = studyPrograms.stream()
                                .map(StudyProgram::getName)
                                .collect(Collectors.toList());
                        comboBox5.getItems().addAll(programNames);

                        comboBox5.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                            if (newValue1 != null) {
                                StudyProgram selectedProgram = studyPrograms.stream()
                                        .filter(program -> program.getDescription().equals(newValue1))
                                        .findFirst()
                                        .orElse(null);

                                if (selectedProgram != null) {
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        table2.setRowFactory(tv -> {
            TableRow<Channel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Channel selectedChannel = row.getItem();

                    nameField.setText(selectedChannel.getName());
                    descriptionField.setText(selectedChannel.getDescription());

                }
            });
            return row;
        });



        TextField studies=new TextField();
        studies.setPromptText("Studies");
        studies.setId("studies");

        TextField studyProgram=new TextField();
        studyProgram.setPromptText("StudyProgram");
        studyProgram.setId("StudyProgram");

        ComboBox<String> comboBox1 = new ComboBox<>();
        comboBox1.setId("comboBox1");
        ComboBox<String> comboBox2 = new ComboBox<>();
        comboBox2.setId("comboBox2");
        ComboBox<String> comboBox3 = new ComboBox<>();
        comboBox3.setId("comboBox3");

        try {
            List<StudiesDTO> studiesList = ApiStudies.getStudies();

            comboBox1.getItems().addAll(
                    studiesList.stream().map(StudiesDTO::getName).collect(Collectors.toList())
            );

            comboBox1.valueProperty().addListener((observable, oldValue, newValue) -> {
                comboBox2.getItems().clear();
                comboBox3.getItems().clear();

                if (newValue != null) {
                    try {
                        List<StudyProgram> studyPrograms = ApiStudyProgram.getStudyPrograms(newValue);
                        System.out.println("Selected study: " + newValue);
                        System.out.println(studyPrograms);

                        List<String> programNames = studyPrograms.stream()
                                .map(StudyProgram::getName)
                                .collect(Collectors.toList());
                        comboBox2.getItems().addAll(programNames);

                        comboBox2.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                            comboBox3.getItems().clear();

                            if (newValue1 != null) {
                                System.out.println("Selected study program: " + newValue1);

                                StudyProgram selectedProgram = studyPrograms.stream()
                                        .filter(program -> program.getName().equals(newValue1))
                                        .findFirst()
                                        .orElse(null);

                                if (selectedProgram != null) {
                                    List<String> categoryNames = selectedProgram.getCategories().stream()
                                            .map(NewCategoryDTO::getName)
                                            .collect(Collectors.toList());
                                    comboBox3.getItems().addAll(categoryNames);
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }





        comboBox1.setStyle("-fx-background-color: white; -fx-padding: 0 5px;-fx-border-color: #173669;-fx-border-radius: 5px;-fx-text-fill:#173669;");
        comboBox2.setStyle("-fx-background-color: white; -fx-padding: 0 5px;-fx-border-color: #173669;-fx-border-radius: 5px;-fx-text-fill:#173669;");
        comboBox3.setStyle("-fx-background-color: white; -fx-padding: 0 5px;-fx-border-color: #173669;-fx-border-radius: 5px;-fx-text-fill:#173669;");

        Button addCategory= new Button("Dodaj");
        addCategory.setId("addButton3");
        addCategory.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        addCategory.setOnMouseEntered(e -> {
            addCategory.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        addCategory.setOnMouseExited(e -> {
            addCategory.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        addCategory.setOnAction(e -> {
            if (categoryField.getText().isEmpty() || categoryField2.getText().isEmpty() || comboBox5.getValue() == null || comboBox4.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sva polja moraju biti popunjena, uključujući kategoriju i selektovane studije");
                alert.show();
                return;
            }

            MainRepository.getInstance().put("name", categoryField.getText());
            MainRepository.getInstance().put("description", categoryField2.getText());
            MainRepository.getInstance().put("studyProgram", comboBox5.getValue());
            MainRepository.getInstance().put("studies", comboBox4.getValue());

            helloController.addCategory();

            categoryField.clear();
            categoryField2.clear();
            comboBox5.setValue(null);
            comboBox4.setValue(null);
        });




        Button addButton2 = new Button("Dodaj");
        addButton2.setId("addButton2");
        addButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        addButton2.setOnMouseEntered(e -> {
            addButton2.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        addButton2.setOnMouseExited(e -> {
            addButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        addButton2.setOnAction(e -> {
            if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                    comboBox1.getValue() == null || comboBox2.getValue() == null || comboBox3.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sva polja moraju biti popunjena, uključujući kategoriju");
                alert.show();
                return;
            }


            MainRepository.getInstance().put("name", nameField.getText());
            MainRepository.getInstance().put("description", descriptionField.getText());
            MainRepository.getInstance().put("studiesName", comboBox1.getValue()); // Ispravljeno
            MainRepository.getInstance().put("studyProgramName", comboBox2.getValue()); // Ispravljeno
            MainRepository.getInstance().put("categoryName", comboBox3.getValue());

            helloController.addChannel();
            nameField.clear();
            descriptionField.clear();
            comboBox1.getSelectionModel().clearSelection();
            comboBox2.getSelectionModel().clearSelection();
            comboBox3.getSelectionModel().clearSelection();
        });



        Button deleteButton = new Button("Obriši");
        deleteButton.setId("deleteButton2");
        deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        deleteButton.setOnMouseEntered(e -> {
            deleteButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        deleteButton.setOnMouseExited(e -> {
            deleteButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        deleteButton.setOnAction(e->helloController.deleteChannel(table2));

        Button editButton = new Button("Izmeni");
        editButton.setId("editDugme");
        editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        editButton.setOnMouseEntered(e -> {
            editButton.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        editButton.setOnMouseExited(e -> {
            editButton.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        editButton.setOnAction(event -> {
            if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sva polja moraju biti popunjena!");
                alert.show();
                return;
            }

            Channel selectedChannel = table2.getSelectionModel().getSelectedItem();

            if (selectedChannel == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nijedan red nije selektovan za ažuriranje!");
                alert.show();
                return;
            }

            try {
                boolean success = editChannel(selectedChannel.getId(), nameField.getText(), descriptionField.getText());

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kanal uspešno ažuriran!");
                    alert.show();

                    selectedChannel.setName(nameField.getText());
                    selectedChannel.setDescription(descriptionField.getText());
                    table2.refresh();

                    nameField.clear();
                    descriptionField.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Ažuriranje kanala nije uspelo! Proverite prava pristupa.");
                    alert.show();
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške: " + e.getMessage());
                alert.show();
            }
        });


        Button roleButton2=new Button("Uloge");
        roleButton2.setId("roleButton2");
        roleButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        roleButton2.setOnMouseEntered(e -> {
            roleButton2.setStyle("-fx-background-color:#173669;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:white;-fx-border-radius:10px;-fx-background-radius:10px");
        });

        roleButton2.setOnMouseExited(e -> {
            roleButton2.setStyle("-fx-background-color:white;-fx-text-fill:#173669;-fx-font-weight:bold;-fx-font-size:12px;-fx-border-color:#173669;-fx-border-radius:10px;-fx-background-radius:10px");
        });
        roleButton2.setOnAction(e -> popUpChannelView.showPopupWindow());

        Image image4 = new Image(getClass().getResource("/images/raf3.png").toExternalForm());
        ImageView image5 = new ImageView(image4);
        image5.setFitWidth(200);
        image5.setPreserveRatio(true);

        HBox inputLayout = new HBox(10,nameField, descriptionField,comboBox1,comboBox2,comboBox3,addButton2, editButton, deleteButton,roleButton2);
        HBox categoryLayout=new HBox(10,categoryField,categoryField2,comboBox4,comboBox5,addCategory);
        VBox vbox = new VBox(10, pretraga, searchField, table2,kanali, inputLayout,separator,kategorije,categoryLayout,separator2,image5);
        vbox.setStyle("-fx-background-color:white");
        return vbox;

    }
}
