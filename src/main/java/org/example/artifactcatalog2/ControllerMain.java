package org.example.artifactcatalog2;

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
    private ListView<String> myListResults;
    private boolean isDarkModeOn = false;
    @FXML
    private MenuButton sortBy;

    public boolean isDarkModeOn(){
        return isDarkModeOn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){




        filter.setValue("Filter");   //naming the filer
        HBox firsLine = new HBox();
        firsLine.setAlignment(Pos.CENTER);


        String[] forTest = {"aasd", "asdas", "dfdf", "szfsdfds"};
        myListResults.getItems().addAll(forTest);


        String[] sortingByItems = {"Hellenistic", "Necklace" }; //these one should be the existing tags. I will made up some for the sake of demonstration
        for(String tags : sortingByItems){
            CheckMenuItem item = new CheckMenuItem(tags);
            item.setOnAction(e->tagSelected());  // listener for all the tags it gets triggered if one of them selected or deselected
            sortBy.getItems().add(item);
        }

    }
    public void darkMode(){
        String darkModeCSS = this.getClass().getResource("DarkMode.css").toExternalForm();
        if(darkModeChecker.isSelected()){
            Scene sceneMain = darkModeChecker.getParentPopup().getOwnerWindow().getScene();
            sceneMain.getStylesheets().add(darkModeCSS);
            isDarkModeOn = true;
        }
        else{
            Scene sceneMain = darkModeChecker.getParentPopup().getOwnerWindow().getScene();
            sceneMain.getStylesheets().remove(darkModeCSS);
            isDarkModeOn = true;
        }
    }

    public void search(ActionEvent event){
        //method that gets activated when user press search button
        try{
            Parent root = FXMLLoader.load(getClass().getResource("uniquePage.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void tagSelected(){
        List<String> selectedTags = sortBy.getItems().stream()
                .filter(i -> i instanceof CheckMenuItem)
                .map(i -> (CheckMenuItem) i)
                .filter(CheckMenuItem::isSelected)
                .map(MenuItem::getText)
                .collect(Collectors.toList());

        ArrayList<String> selectedTagsArr = new ArrayList<>(selectedTags);   // selected elements dynamic arraylist
        for(String cur : selectedTagsArr){
            System.out.println("element: " + cur);
        }
    }
}
