package org.example.artifactcatalog2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerUnique implements Initializable {
    @FXML
    private CheckMenuItem darkModeChecker;
    @FXML
    private ImageView pictureArtifact;
    @FXML
    private Label titleArtifact;
    private boolean isDarkModeOn;
    @FXML
    private TextArea explanationArtifact;


    public boolean isDarkModeOn(){
        return isDarkModeOn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        explanationArtifact.setEditable(false);
        Image image = new Image(getClass().getResource("/images/fatMalenia.png").toExternalForm());
        pictureArtifact.setImage(image);
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
}
