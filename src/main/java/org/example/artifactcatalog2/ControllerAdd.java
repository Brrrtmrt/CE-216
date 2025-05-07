package org.example.artifactcatalog2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ControllerAdd {

    @FXML private TextField inputID;
    @FXML private TextField inputName;
    @FXML private TextField inputCategory;
    @FXML private TextField inputCivilization;
    @FXML private TextField inputDiscoveryLocation;
    @FXML private DatePicker inputDiscoveryDate;
    @FXML private TextField inputCurrentPlace;
    @FXML private TextField inputComposition;
    @FXML private TextField inputTags;
    @FXML private TextField inputLength;
    @FXML private TextField inputWidth;
    @FXML private TextField inputHeight;
    @FXML private TextField inputWeight;
    @FXML private Label labelImagePath;
    @FXML private Button imageSelectButton;
    @FXML private Label imagePathLabel;

    private String selectedImagePath = null;

    @FXML
    public void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Artifact Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Path targetDir = Paths.get(System.getProperty("user.dir"), "out", "images");
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                }
                Path targetPath = targetDir.resolve(selectedFile.getName());
                Files.copy(selectedFile.toPath(), targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                selectedImagePath = "out/images/" + selectedFile.getName();
                labelImagePath.setText(selectedFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Image selection failed: " + e.getMessage());
            }
        }
    }

    @FXML
    public void addArtifact(ActionEvent event) {
        try {
            Artifact artifact = new Artifact(
                inputID.getText(),
                inputName.getText(),
                inputCategory.getText(),
                inputDiscoveryLocation.getText(),
                new ArrayList<>(Arrays.asList(inputComposition.getText().split(",\\s*"))),
                inputCivilization.getText(),
                inputDiscoveryDate.getValue(),
                inputCurrentPlace.getText(),
                new Dimension(
                    Long.parseLong(inputLength.getText()),
                    Long.parseLong(inputWidth.getText()),
                    Long.parseLong(inputHeight.getText())
                ),
                Long.parseLong(inputWeight.getText()),
                new ArrayList<>(Arrays.asList(inputTags.getText().split(",\\s*"))),
                selectedImagePath
            );
            boolean success = UserOperations.createArtifact(artifact);
            if (success) {
                ControllerMain.getInstance().addArtifactToList(artifact);
                showAlert("Artifact added successfully!");
                closeWindow();
            } else {
                showAlert("Failed to add artifact. Maybe duplicate ID?");
            }            
        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) inputID.getScene().getWindow();
        stage.close();
    }
}
