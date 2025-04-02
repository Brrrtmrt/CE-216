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
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        filter.setValue("Filter");
        HBox firsLine = new HBox();
        firsLine.setAlignment(Pos.CENTER);


        String[] forTest = {"aasd", "asdas", "dfdf", "szfsdfds"};
        myListResults.getItems().addAll(forTest);

    }
    public void darkMode(ActionEvent event){
        String darkModeCSS = this.getClass().getResource("DarkMode.css").toExternalForm();
        if(darkModeChecker.isSelected()){
            Scene sceneMain = darkModeChecker.getParentPopup().getOwnerWindow().getScene();
            sceneMain.getStylesheets().add(darkModeCSS);
        }
        else{
            Scene sceneMain = darkModeChecker.getParentPopup().getOwnerWindow().getScene();
            sceneMain.getStylesheets().remove(darkModeCSS);
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
}
