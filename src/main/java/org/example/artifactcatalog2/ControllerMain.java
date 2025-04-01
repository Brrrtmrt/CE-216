package org.example.artifactcatalog2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMain implements Initializable {
    @FXML
    private ChoiceBox<String> filter;
    @FXML
    private Label mainLabel;
    @FXML
    private TextField searchBar;
    @FXML
    private VBox mainLayout;
    @FXML
    private CheckMenuItem darkModeChecker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        filter.setValue("Filter");
        HBox firsLine = new HBox();
        firsLine.setAlignment(Pos.CENTER);

    }
    public void darkMode(ActionEvent event){
        String darkModeCSS = this.getClass().getResource("DarkMode.css").toExternalForm();
        if(darkModeChecker.isSelected()){
            Scene sceneMain = darkModeChecker.getParentPopup().getOwnerWindow().getScene();
            sceneMain.getStylesheets().add(darkModeCSS);
        }
    }
}
