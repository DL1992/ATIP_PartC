package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

/**
 * This class is the Controller for the Victory scene as part of the view in the MVVM architecture
 *
 * @author Vladislav Sergienko
 * @author Doron Laadan
 */
public class MyVictoryViewController implements Observer, IView {
    private MyViewModel vm;
    public Scene victoryScene;

    @FXML
    public BorderPane mainBorderPane;
    public AnchorPane pictureAnchorPane;
    public HBox mainHBox;
    public Button backToTheMain;

    @Override
    public void setViewModel(MyViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void UpdateLayout() {
        bindStuff();
        Image image = new Image("BackGround/deadpool.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(pictureAnchorPane.getWidth(), pictureAnchorPane.getHeight(), false, false, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        pictureAnchorPane.setBackground(background);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == vm) {
            if (arg instanceof String) {
                if (((String) arg).equals("GameOver")) {
                    weWon();
                }
                if (((String) arg).equals("Continue")) {
                    ((Stage) victoryScene.getWindow()).close();
                }
                if (((String) arg).equals("ShutDown")) {
                    if ((Stage) victoryScene.getWindow() != null) {
                        ((Stage) victoryScene.getWindow()).close();
                    }
                }
            }
        }
    }

    /**
     * this function is called when someone clicks on the "Continue" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void resumeGame() {
        vm.resumeGame();
    }


    /**
     * this function opens a new stage and shows the Victory Scene
     *
     */
    private void weWon() {
        Stage stage = new Stage();
        stage.setTitle("Our AMAZING maze game (Show only Version patch 2.0.1)");
        stage.setOnCloseRequest(event -> vm.closeProgram());
        vm.playMusic("./resources/Audio/champions.mp3");
        stage.setScene(victoryScene);
        stage.show();
        bindStuff();
    }

    /**
     * this function is a helper function for UpdateLayout it binds the Opening View controls to the right proportions
     *
     */
    private void bindStuff() {
        mainBorderPane.prefHeightProperty().bind(victoryScene.heightProperty());
        mainBorderPane.prefWidthProperty().bind(victoryScene.widthProperty());
        pictureAnchorPane.prefHeightProperty().bind(mainBorderPane.prefHeightProperty().multiply(0.8));
        pictureAnchorPane.prefWidthProperty().bind(mainBorderPane.prefWidthProperty().multiply(1));
        mainHBox.prefHeightProperty().bind(mainBorderPane.prefHeightProperty().multiply(0.15));
        mainHBox.prefWidthProperty().bind(mainBorderPane.prefWidthProperty().multiply(1));
        backToTheMain.prefHeightProperty().bind(mainHBox.prefHeightProperty().multiply(0.5));
        backToTheMain.prefWidthProperty().bind(mainHBox.prefWidthProperty().multiply(0.3));
    }
}
