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
    public void start(Stage primaryStage) throws Exception {
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

        Scene gameScene = getScene(root);
        Scene victoryScene = getScene(root2);
        Scene openingScene = getScene(root3);

        myViewController.mainScene = gameScene;
        myVictoryViewController.victoryScene = victoryScene;
        myOpeningViewController.openingScene = openingScene;

        openingScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                myOpeningViewController.UpdateLayout();
            }
        });
        openingScene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                myOpeningViewController.UpdateLayout();
            }
        });
        gameScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                myViewController.UpdateLayout();
            }
        });
        gameScene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                myViewController.UpdateLayout();
            }
        });
        victoryScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                myVictoryViewController.UpdateLayout();
            }
        });
        victoryScene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                myVictoryViewController.UpdateLayout();
            }
        });

        vm.playMusic("./resources/Audio/Superman.mp3");
        myViewController.setVolume(50);
        primaryStage.setScene(openingScene);
        primaryStage.setOnCloseRequest(event -> vm.closeProgram());
        myOpeningViewController.UpdateLayout();
        primaryStage.show();
        myOpeningViewController.UpdateLayout();
    }

    private Scene getScene(Parent root) {
        Scene scene = new Scene(root, 875, 750, Color.web("CAEBF2"));
        scene.getStylesheets().add("./View/Design.css");
        return scene;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
