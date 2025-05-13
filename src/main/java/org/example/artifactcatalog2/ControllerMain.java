package org.example.artifactcatalog2;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerMain implements Initializable {
    @FXML
    private ChoiceBox<String> filter;   //filter choice box
    @FXML
    private Label mainLabel;
    @FXML
    private TextField searchBar;
    @FXML
    private VBox mainLayout;
    @FXML
    private CheckMenuItem darkModeChecker;
    @FXML
    private Button selectButton;
    @FXML
    private ListView<Artifact> myListResults;
    private boolean isDarkModeOn = false;
    @FXML
    private MenuButton ListByTags;
    @FXML
    private Button deleteButton;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); //??
    private ArrayList<Artifact> loadedList;
    private ArrayList<Artifact> selectedArtifacts = new ArrayList<>();
    @FXML
    private CheckBox selectAll;
    @FXML
    private HBox lastRow;
    @FXML
    private HBox anotherRow;
    @FXML
    private MenuItem exportJSON;
    @FXML
    private MenuItem importJSON;
    private static ControllerMain instance;

    public ControllerMain() {
        instance = this;
    }

    public static ControllerMain getInstance() {
        return instance;
    }

    
    public void addArtifactToList(Artifact artifact) {
        myListResults.getItems().add(artifact);
    }

    public void refresh() {
        loadedList = JSONOperations.readExistingList();
        myListResults.getItems().setAll(loadedList);
        ControllerMain.getInstance().updateListViewCells();
    }

    public void tags(MouseEvent event) {

        if (loadedList == null || loadedList.isEmpty()) {
            System.out.println("Loaded list is empty or null.");
            return;
        }

        if (!ListByTags.getItems().isEmpty()) {
            return;
        }

        List<String> uniqueTags = loadedList.stream()
                .flatMap(artifact -> artifact.getTags().stream())
                .distinct().sorted()
                .toList();
        ListByTags.getItems().clear();


        for (String tag : uniqueTags) {
            CheckBox item = new CheckBox(tag);
            item.setOnAction(e ->
                    updateListViewWithSelectedTags());// listener for all the tags, triggered when one is selected or deselected
            CustomMenuItem ci = new CustomMenuItem(item);
            ci.setHideOnClick(false);
            ListByTags.getItems().add(ci);
        }
    }

    public void updateListViewWithSelectedTags() {
        List<String> selectedTags = ListByTags.getItems().stream()
                .filter(CustomMenuItem.class::isInstance)
                .map(CustomMenuItem.class::cast)
                .map(CustomMenuItem::getContent)
                .filter(CheckBox.class::isInstance)
                .map(CheckBox.class::cast)
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .toList();

        if (selectedTags.isEmpty()) {
            myListResults.getItems().setAll(loadedList);
            return;
        }

        List<Artifact> filteredList = loadedList.stream()
                .filter(artifact -> artifact.getTags().containsAll(selectedTags))
                .toList();

        myListResults.getItems().setAll(filteredList);
    }

    //UI Skull 10 saniye
    public void delete(ActionEvent event) {
        Task<Void> deleteTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (selectedArtifacts == null || selectedArtifacts.isEmpty()) {
                    return null;
                }

                ArrayList<Artifact> toDeleteList = new ArrayList<>(selectedArtifacts);


                boolean isDeleted = UserOperations.deleteArtifacts(toDeleteList);

                if (isDeleted) {
                    Platform.runLater(() -> {
                        System.out.println("Artifacts deleted successfully.");
                        loadedList.removeIf(toDeleteList::contains);
                        selectedArtifacts.removeIf(toDeleteList::contains);
                        myListResults.getItems().removeIf(toDeleteList::contains);
                        toDeleteList.clear();
                    });
                } else {
                    Platform.runLater(() -> System.out.println("Delete operation failed."));
                }

                return null;
            }
        };

        deleteTask.setOnFailed(e -> {
            System.out.println("An error occurred during the delete operation.");
        });

        Thread thread = new Thread(deleteTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void select(ActionEvent event) {
        boolean isSelected = selectAll.isSelected();
        selectedArtifacts.clear();
        if (isSelected) {
            selectedArtifacts.addAll(loadedList);
        }

        myListResults.setCellFactory(listView -> new ListCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Artifact artifact, boolean empty) {
                super.updateItem(artifact, empty);
                if (empty || artifact == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    checkBox.setText(artifact.getID() + "\n" + artifact.getName());
                    checkBox.setSelected(isSelected);
                    checkBox.setOnAction(e -> {
                        if (checkBox.isSelected()) {
                            if (!selectedArtifacts.contains(artifact)) {
                                selectedArtifacts.add(artifact);
                            }
                        } else {
                            selectedArtifacts.remove(artifact);
                        }
                    });
                    setGraphic(checkBox);
                }
            }
        });

        myListResults.refresh();
    }

    public void export(ActionEvent event) {
        Runnable runnable = () -> {
            if (selectedArtifacts == null || selectedArtifacts.isEmpty()) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Select Artifacts to Export.");
                    alert.setTitle("");
                    alert.setHeaderText("");
                    alert.showAndWait();
                });
                return;
            }
            ArrayList<Artifact> toExport = new ArrayList<>(selectedArtifacts);
            boolean flag = JSONOperations.exportJSON(toExport);
            //TODO: REMOVE LATER
            Platform.runLater(() -> {
                if (flag) {
                    System.out.println("Export operation completed.");
                } else {
                    System.out.println("Export operation failed.");
                }
            });
            //
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

    public void importJSON(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select JSON file to import");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        File selectedFile = fc.showOpenDialog(stage);
        if (selectedFile != null) {
            Path filePath = selectedFile.toPath();
            System.out.println("File: " + filePath);
            JSONOperations.importJSON(filePath);
            refresh();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        refresh(); //creates db if not
        filter.setValue("Filter");   //naming the filer
        Platform.runLater(() -> {
            if (DarkModeManager.getInstance().isDarkModeOn()) {
                String darkModeCSS = this.getClass().getResource("DarkMode.css").toExternalForm();
                Scene sceneMain = mainLayout.getScene();
                if (sceneMain != null) {
                    sceneMain.getStylesheets().add(darkModeCSS);
                }
                darkModeChecker.setSelected(true);
            } else {
                darkModeChecker.setSelected(false);
            }
        });
        HBox firsLine = new HBox();
        firsLine.setAlignment(Pos.CENTER);

        exportJSON.setOnAction(this::export);
        importJSON.setOnAction(this::importJSON);
        ListByTags.setOnMouseClicked(this::tags);
        tags(null);

        Button bttnSearch = new Button("Search");
        bttnSearch.setOnAction(event -> search(event));
        lastRow.getChildren().add(bttnSearch);

        searchBar.setOnKeyPressed(keyEvent -> {       //search func also works with button presses
            if (keyEvent.getCode() == KeyCode.ENTER) {
                ActionEvent actionEvent = new ActionEvent(searchBar, null);
                this.search(actionEvent);
            }
        });

        myListResults.setCellFactory(listView -> new ListCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Artifact artifact, boolean empty) {
                super.updateItem(artifact, empty);
                if (empty || artifact == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    checkBox.setText(artifact.getID() + "\n" + artifact.getName());
                    checkBox.setSelected(myListResults.getSelectionModel().getSelectedItems().contains(artifact));
                    checkBox.setOnAction(event -> {
                        if (checkBox.isSelected()) {
                            if (!selectedArtifacts.contains(artifact)) {
                                selectedArtifacts.add(artifact);
                                System.out.println("Artifact added: " + artifact.getID());
                            }
                        } else {
                            selectedArtifacts.remove(artifact);
                            System.out.println("Artifact removed: " + artifact.getID());
                        }
                    });
                    setGraphic(checkBox);
                }
            }
        });
    }

    public void darkMode() {
        String darkModeCSS = this.getClass().getResource("DarkMode.css").toExternalForm();
        Scene sceneMain = darkModeChecker.getParentPopup().getOwnerWindow().getScene();
        if (darkModeChecker.isSelected()) {
            sceneMain.getStylesheets().add(darkModeCSS);
            DarkModeManager.getInstance().setDarkModeOn(true);
        } else {
            sceneMain.getStylesheets().remove(darkModeCSS);
            DarkModeManager.getInstance().setDarkModeOn(false);
        }
    }

    public void goPage(ActionEvent event) {
        //method that gets activated when user press search button
        if (selectedArtifacts.size() != 1) {
            System.out.println("You have to select 1 element to view its page!");
            return;
        } else {
            try {


                FXMLLoader loader = new FXMLLoader(getClass().getResource("uniquePage.fxml"));


                Parent root = loader.load();
                ControllerUnique unique = loader.getController();

                //unique.selected(selectedArtifacts.getFirst());
                Platform.runLater(() -> unique.selected(selectedArtifacts.getFirst()));

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);

                boolean isFull = stage.isFullScreen();
                boolean isMax = stage.isMaximized();

                stage.setScene(scene);

                Platform.runLater(() -> {   //IDK WHY IT RESETS THE FULLSCREEN OR MAX SCREEN
                    stage.setFullScreen(isFull);
                    stage.setMaximized(isMax);
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public void tagSelected() {
        List<String> selectedTags = ListByTags.getItems().stream()
                .filter(CheckMenuItem.class::isInstance)
                .map(CheckMenuItem.class::cast)
                .filter(CheckMenuItem::isSelected)
                .map(MenuItem::getText)
                .toList();
    }

    public void search(ActionEvent event) {
        String currentText = searchBar.getText();
        ObservableList<Artifact> items = myListResults.getItems();

        for (Artifact a : loadedList) {
            if (a.getName().toLowerCase().contains(currentText)) {
                if (!items.contains(a)) {
                    items.add(a);
                }
            } else {
                if (items.contains(a)) {
                    items.remove(a);
                }
            }
        }
    }

    @FXML
    public void openAddArtifact(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addArtifact.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Artifact");
            Scene scene = new Scene(root);

            // Apply dark mode if enabled
            if (DarkModeManager.getInstance().isDarkModeOn()) {
                String darkModeCSS = getClass().getResource("DarkMode.css").toExternalForm();
                scene.getStylesheets().add(darkModeCSS);
            }
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   

    public void updateListViewCells() {
        myListResults.setCellFactory(listView -> new ListCell<>() {
        private final CheckBox checkBox = new CheckBox();

        @Override
        protected void updateItem(Artifact artifact, boolean empty) {
            super.updateItem(artifact, empty);
            if (empty || artifact == null) {
                setText(null);
                setGraphic(null);
            } else {
                checkBox.setText(artifact.getID() + "\n" + artifact.getName());
                checkBox.setSelected(selectedArtifacts.contains(artifact));
                checkBox.setOnAction(event -> {
                    if (checkBox.isSelected()) {
                        selectedArtifacts.add(artifact);
                        System.out.println("[DEBUG] Artifact added: " + artifact.getID());
                    } else {
                        selectedArtifacts.remove(artifact);
                        System.out.println("[DEBUG] Artifact removed: " + artifact.getID());
                    }
                });
                setGraphic(checkBox);
            }
            }
        });
    }

    public void showUserManual(ActionEvent event) {
        try {
            String resourcePath = "/org/example/artifactcatalog2/user_manual.txt";
            URL resourceURL = getClass().getResource(resourcePath);
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                throw new FileNotFoundException("User manual not found at: " + resourcePath);
            }
            String manualText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            TextArea textArea = new TextArea(manualText);
            textArea.setWrapText(true);
            textArea.setEditable(false);
            textArea.setPrefWidth(600);
            textArea.setPrefHeight(600);
            VBox root = new VBox(textArea);
            root.setPrefWidth(600);
            root.setPrefHeight(600);
            root.setPadding(new Insets(10));
            Scene scene = new Scene(root, 600, 600);
            if (DarkModeManager.getInstance().isDarkModeOn()) {
                String darkCSS = getClass().getResource("DarkMode.css").toExternalForm();
                scene.getStylesheets().add(darkCSS);
            }
            Stage stage = new Stage();
            stage.setTitle("User Manual");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load the user manual.");
            alert.showAndWait();
        }
    }
}
