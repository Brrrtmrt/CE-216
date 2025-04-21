package org.example.artifactcatalog2;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
    private Button searchButton;
    @FXML
    private ListView<Artifact> myListResults;
    private boolean isDarkModeOn = false;
    @FXML
    private MenuButton sortBy;
    @FXML
    private Button deleteButton;
    @FXML
    private Button exportButton;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); //??
    private ArrayList<Artifact> loadedList;
    private ArrayList<Artifact> selectedArtifacts = new ArrayList<>();
    @FXML
    private CheckBox selectAll;


    public void refresh() {
        loadedList = JSONOperations.readExistingList();
        myListResults.getItems().addAll(loadedList);
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
                return;
            }
            ArrayList<Artifact> toExport = new ArrayList<>(selectedArtifacts);
            boolean flag = JSONOperations.exportJSON(toExport);
            Platform.runLater(() -> {
                if (flag) {
                    System.out.println("Export operation completed.");
                } else {
                    System.out.println("Export operation failed.");
                }
            });
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

    public boolean isDarkModeOn() {
        return isDarkModeOn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        refresh();
        filter.setValue("Filter");   //naming the filer
        HBox firsLine = new HBox();
        firsLine.setAlignment(Pos.CENTER);


       /* String[] forTest = {"aasd", "asdas", "dfdf", "szfsdfds"};
        myListResults.getItems().addAll(forTest);
*/

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

        String[] sortingByItems = {"Hellenistic", "Necklace", "Tag20"}; //these one should be the existing tags. I will made up some for the sake of demonstration
        for (String tags : sortingByItems) {
            CheckMenuItem item = new CheckMenuItem(tags);
            item.setOnAction(e -> tagSelected());  // listener for all the tags it gets triggered if one of them selected or deselected
            sortBy.getItems().add(item);
        }

    }

    public void darkMode() {
        String darkModeCSS = this.getClass().getResource("DarkMode.css").toExternalForm();
        if (darkModeChecker.isSelected()) {
            Scene sceneMain = darkModeChecker.getParentPopup().getOwnerWindow().getScene();
            sceneMain.getStylesheets().add(darkModeCSS);
            isDarkModeOn = true;
        } else {
            Scene sceneMain = darkModeChecker.getParentPopup().getOwnerWindow().getScene();
            sceneMain.getStylesheets().remove(darkModeCSS);
            isDarkModeOn = true;
        }
    }

    public void search(ActionEvent event) {
        //method that gets activated when user press search button
        try {
            Parent root = FXMLLoader.load(getClass().getResource("uniquePage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void tagSelected() {
        List<String> selectedTags = sortBy.getItems().stream().filter(i -> i instanceof CheckMenuItem).map(i -> (CheckMenuItem) i).filter(CheckMenuItem::isSelected).map(MenuItem::getText).collect(Collectors.toList());

        ArrayList<String> selectedTagsArr = new ArrayList<>(selectedTags);   // selected elements dynamic arraylist
        for (String cur : selectedTagsArr) {
            System.out.println("element: " + cur);
        }
    }


}
