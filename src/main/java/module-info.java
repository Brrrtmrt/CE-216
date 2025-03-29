module org.example.artifactcatalog2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens org.example.artifactcatalog2 to javafx.fxml;
    exports org.example.artifactcatalog2;
}