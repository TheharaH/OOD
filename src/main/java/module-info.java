module com.example.ood {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.ood to javafx.fxml;
    exports com.example.ood;
}