package org.example.artifactcatalog2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage mainStage) throws Exception{
        FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("mainf.fxml"));
        Parent root = loaderMain.load();

        Scene sceneMain = new Scene(root);  //kind of a main page




        mainStage.setMaximized(true);

        mainStage.setScene(sceneMain);
        mainStage.show();
    }
}
