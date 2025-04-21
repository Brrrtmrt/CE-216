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

    public void selected(Artifact selectedArtifact){
        System.out.println("selected method activated");
        titleArtifact.setText(selectedArtifact.getName());

        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(selectedArtifact.getName()).append("\n");
        sb.append("Category: ").append(selectedArtifact.getCategory()).append("\n");
        sb.append("Civilization: ").append(selectedArtifact.getCivilization()).append("\n");
        sb.append("Discovery Location: ").append(selectedArtifact.getDiscoveryLocation()).append("\n");
        sb.append("Composition: ").append(selectedArtifact.getComposition()).append("\n");
        sb.append("Discovery Date: ").append(selectedArtifact.getDiscoveryDate()).append("\n");
        sb.append("Current Place: ").append(selectedArtifact.getCurrentPlace()).append("\n");
        sb.append("Dimensions: ").append(selectedArtifact.getDimension()).append("\n");
        sb.append("Weight: ").append(selectedArtifact.getWeight()).append("\n");
        sb.append("Tags: ").append(String.join(", ", selectedArtifact.getTags())).append("\n");
        sb.append("Unique Identifier: ").append(selectedArtifact.getID());

        explanationArtifact.setText(sb.toString());
    }
}
