package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel m = new MyModel();
        m.startServers();
        MyViewModel vm = new MyViewModel(m);
        m.addObserver(vm);

        FXMLLoader viewLoader = new FXMLLoader();
        FXMLLoader victoryLoader = new FXMLLoader();
        FXMLLoader openingLoader = new FXMLLoader();

        Parent root = viewLoader.load(getClass().getResource("MyView.fxml").openStream());
        MyViewController myViewController = viewLoader.getController();
        myViewController.setViewModel(vm);
        vm.addObserver(myViewController);

        Parent root2 = victoryLoader.load(getClass().getResource("MyVictoryView.fxml").openStream());
        MyVictoryViewController myVictoryViewController = victoryLoader.getController();
        myVictoryViewController.setViewModel(vm);
        vm.addObserver(myVictoryViewController);

        Parent root3 = openingLoader.load(getClass().getResource("MyOpeningView.fxml").openStream());
        MyOpeningViewController myOpeningViewController = openingLoader.getController();
        myOpeningViewController.setViewModel(vm);
        vm.addObserver(myOpeningViewController);

        primaryStage.setTitle("Our AMAZING maze game (Show only Version patch 2.0.1)");

        Scene gameScene = new Scene(root, 875, 750, Color.web("CAEBF2"));
        gameScene.getStylesheets().add("./View/Design.css");

        Scene victoryScene = new Scene(root2, 875, 750, Color.web("CAEBF2"));
        victoryScene.getStylesheets().add("./View/Design.css");

        Scene openingScene = new Scene(root3, 875, 750, Color.web("CAEBF2"));
        openingScene.getStylesheets().add("./View/Design.css");

        myViewController.mainScene = gameScene;
        myVictoryViewController.victoryScene = victoryScene;
        myOpeningViewController.openingScene = openingScene;


        gameScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                myViewController.UpdateLayout();
            }
        });
        gameScene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                myViewController.UpdateLayout();
            }
        });

        vm.playMusic("./resources/Audio/Superman.mp3");
//        vm.mediaPlayer.setVolume(50);
        myViewController.setVolume(50);
        primaryStage.setScene(openingScene);
        primaryStage.show();
        //myViewController.UpdateLayout();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
