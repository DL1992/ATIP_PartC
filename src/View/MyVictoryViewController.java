package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by sergayen on 6/20/2017.
 */
public class MyVictoryViewController implements Observer, IView{
    private MyViewModel vm;
    public Scene victoryScene;

    @FXML
    public BorderPane mainBorderPane;
    public AnchorPane pictureAnchorPane;
    public HBox mainHBox;
    public Button backToTheMain;

    public void setViewModel(MyViewModel vm){
        this.vm = vm;
    }

    @Override
    public void UpdateLayout() {
        bindStuff();
        Image image = new Image("BackGround/deadpool.jpg");
        //Image image2 = new Image(getClass().getResource("deadpool.jpg"));
//        pictureAnchorPane.setStyle("-fx-background-image: url('" + image2 + "'); " +
//                "-fx-background-position: center center; " +
//                "-fx-background-repeat: stretch;");
        BackgroundSize backgroundSize = new BackgroundSize(pictureAnchorPane.getWidth(), pictureAnchorPane.getHeight(), false, false, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        pictureAnchorPane.setBackground(background);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == vm) {
            if(arg instanceof String) {
                if (((String) arg).equals("GameOver")) {
                    weWon();
                }
                if (((String) arg).equals("Continue")) {
                    moveOn();
                }
                if( ((String) arg).equals("ShutDown")) {
                    if((Stage) victoryScene.getWindow() != null){
                        ((Stage) victoryScene.getWindow()).close();
                    }
                }
            }
        }
    }

    public void resumeGame(){
        vm.resumeGame();
    }

    private void moveOn() {
        ((Stage) victoryScene.getWindow()).close();
    }

    private void weWon() {
        Stage stage = new Stage();
        stage.setTitle("Our AMAZING maze game (Show only Version patch 2.0.1)");
        stage.setOnCloseRequest(event -> vm.closeProgram());
        vm.playMusic("./resources/Audio/champions.mp3");
        stage.setScene(victoryScene);
        bindStuff();
        stage.show();
    }

    /**
     * binds all of the controllers to the "right" size
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
