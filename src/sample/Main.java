package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.sample2Controller;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("view/sample2.fxml"));
        primaryStage.setTitle("TIC-TAC-TOE");
        primaryStage.setScene(new Scene(root, 800, 600));
        sample2Controller controller = loader.getController();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
