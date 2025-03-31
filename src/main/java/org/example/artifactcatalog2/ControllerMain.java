package org.example.artifactcatalog2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        filter.setValue("Filter");
        HBox firsLine = new HBox();
        firsLine.setAlignment(Pos.CENTER);
        
    }
}
