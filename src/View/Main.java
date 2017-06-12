package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        primaryStage.setTitle("Our AMAZING maze game (firework version patch 1.0.1)");
        Scene s = new Scene(root, 850, 750, Color.web("CAEBF2"));
        primaryStage.setScene(s);;
        //starting the server should be here?

        //
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
