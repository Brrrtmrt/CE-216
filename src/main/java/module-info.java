module org.example.artifactcatalog2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.artifactcatalog2 to javafx.fxml;
    exports org.example.artifactcatalog2;
}