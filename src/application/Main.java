package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.DatabaseConnection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseConnection.connectToBDD();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Vue.fxml"));
            VBox root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("FunnyPost - Gestion de posts");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); 
    }
}
