package org.example.artifactcatalog2;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerUnique implements Initializable {

    private Artifact selectedArtifact;
    @FXML
    private CheckMenuItem darkModeChecker;
    @FXML
    private ImageView pictureArtifact;
    @FXML
    private Label titleArtifact;
    private boolean isDarkModeOn;
    @FXML
    private TextArea explanationArtifact;
    @FXML
    private VBox mainLayout;
    @FXML
    private Button editMode;
    @FXML
    private Button saveButton;

    public boolean isDarkModeOn() {
        return isDarkModeOn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            if (DarkModeManager.getInstance().isDarkModeOn()) {
                String darkModeCSS = this.getClass().getResource("DarkMode.css").toExternalForm();
                Scene sceneMain = mainLayout.getScene();
                if (sceneMain != null) {
                    sceneMain.getStylesheets().add(darkModeCSS);
                }
                if (darkModeChecker == null) return;
                darkModeChecker.setSelected(true);
            } else {
                if (darkModeChecker == null) return;
                darkModeChecker.setSelected(false);
            }
        });

        explanationArtifact.setEditable(false);
        //creating button to turn back to main page
        Button buttonBack = new Button("Back to Main Page");
        buttonBack.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
        buttonBack.setAlignment(Pos.CENTER);
        mainLayout.getChildren().add(buttonBack);
        ControllerMain.getInstance().updateListViewCells();
    }

    public void edit(ActionEvent event) {

        explanationArtifact.setEditable(!explanationArtifact.isEditable());

    }

    public boolean save(ActionEvent event) {
        Artifact artifact;
        String id;
        try {
            ArrayList<String> attr = new ArrayList<>(Arrays.stream(explanationArtifact.getText().split("\n")).toList());
            String name = attr.get(0).split(": ")[1];
            String category = attr.get(1).split(": ")[1];
            String civilization = attr.get(2).split(": ")[1];
            String discoveryLocation = attr.get(3).split(": ")[1];
            ArrayList<String> composition = new ArrayList<>(Arrays.asList(attr.get(4).split(": ")[1].split(", ")));
            LocalDate discoveryDate = LocalDate.parse(attr.get(5).split(": ")[1], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String currentPlace = attr.get(6).split(": ")[1];
            long length = Long.parseLong(attr.get(8).split(": ")[1]);
            long width = Long.parseLong(attr.get(9).split(": ")[1]);
            long height = Long.parseLong(attr.get(10).split(": ")[1]);
            long weight = Long.parseLong(attr.get(11).split(": ")[1]);
            ArrayList<String> tags = new ArrayList<>(Arrays.asList(attr.get(12).split(": ")[1].split(", ")));
            id = attr.get(13).split(": ")[1];
            String imagePath = attr.get(14).split(": ")[1];
            if (imagePath != null && !imagePath.isBlank()) {
                imagePath = imagePath.trim().replace("\\", "/");
                Path sourcePath = Paths.get(imagePath.trim());
                Path targetDirectory = Paths.get(System.getProperty("user.dir"), "out", "images");
                if (!Files.exists(targetDirectory)) {
                    Files.createDirectories(targetDirectory);
                }
                Path targetPath = targetDirectory.resolve(sourcePath.getFileName());
                Files.copy(sourcePath, targetPath);
                imagePath = targetPath.toString();
            }
            Dimension dimension = new Dimension(length, width, height);
            artifact = new Artifact(id, name, category, discoveryLocation, composition, civilization, discoveryDate, currentPlace, dimension, weight, tags, imagePath);
        } catch (Exception e) {
            System.out.println("bad text");
            return false;
        }

        boolean deleted = UserOperations.deleteArtifact(id);
        boolean created = UserOperations.createArtifact(artifact);
        System.out.println("deleted: " + deleted + ", created: " + created);
        return deleted && created;
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

    public void selected(Artifact selectedArtifact) {
        this.selectedArtifact = selectedArtifact;
        System.out.println("selected method activated");
        titleArtifact.setText("Here is the page of the " + selectedArtifact.getName() + ":");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(selectedArtifact.getName()).append("\n");
        sb.append("Category: ").append(selectedArtifact.getCategory()).append("\n");
        sb.append("Civilization: ").append(selectedArtifact.getCivilization()).append("\n");
        sb.append("Discovery Location: ").append(selectedArtifact.getDiscoveryLocation()).append("\n");
        sb.append("Composition: ").append(selectedArtifact.getComposition()).append("\n");
        sb.append("Discovery Date: ").append(selectedArtifact.getDiscoveryDate().format(formatter)).append("\n");
        sb.append("Current Place: ").append(selectedArtifact.getCurrentPlace()).append("\n");
        sb.append("Dimensions: ").append("\n");
        sb.append("Length: ").append(selectedArtifact.getDimension().getLength()).append("\n");
        sb.append("Width: ").append(selectedArtifact.getDimension().getWidth()).append("\n");
        sb.append("Height: ").append(selectedArtifact.getDimension().getHeight()).append("\n");
        sb.append("Weight: ").append(selectedArtifact.getWeight()).append("\n");
        sb.append("Tags: ").append(String.join(", ", selectedArtifact.getTags())).append("\n");
        sb.append("Unique Identifier: ").append(selectedArtifact.getID()).append("\n");
        sb.append("Image Path: ").append(selectedArtifact.getImagePath()).append("\n");
        explanationArtifact.setText(sb.toString());


        try {
            String imagePath = selectedArtifact.getImagePath();
            if (imagePath != null && !imagePath.isBlank()) {
                imagePath = imagePath.trim();
                if (imagePath.startsWith("\"") && imagePath.endsWith("\"")) {
                    imagePath = imagePath.substring(1, imagePath.length() - 1);
                }
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    byte[] imageBytes = java.nio.file.Files.readAllBytes(imageFile.toPath());
                    Image image = new Image(new java.io.ByteArrayInputStream(imageBytes));
                    pictureArtifact.setImage(image);
                } else {
                    pictureArtifact.setImage(null);
                }
            } else {
                pictureArtifact.setImage(null);
            }
        } catch (Exception e) {
            pictureArtifact.setImage(null);
        }
    }
}
