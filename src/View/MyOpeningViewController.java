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
 * This class is the Controller for the opening scene as part of the view in the MVVM architecture
 *
 * @author Vladislav Sergienko
 * @author Doron Laadan
 */
public class MyOpeningViewController implements IView, Observer {
    private MyViewModel vm;
    public Scene openingScene;

    @FXML
    public BorderPane mainBorderPane;
    public AnchorPane pictureAnchorPane;
    public VBox mainVBox;
    public Button startGameButton;
    public Button loadGameButton;


    @Override
    public void setViewModel(MyViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void UpdateLayout() {
        bindStuff();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == vm) {
            if (arg instanceof String) {
                if (((String) arg).equals("Continue") || ((String) arg).equals("mainToOpen")) {
                    showOpeningView();
                }
                if (((String) arg).equals("StartGame") || ((String) arg).equals("LoadGame")) {
                    ((Stage) openingScene.getWindow()).close();
                }
                if (((String) arg).equals("ShutDown")) {
                    if ((Stage) openingScene.getWindow() != null) {
                        ((Stage) openingScene.getWindow()).close();
                    }
                }
            }
        }
    }

    /**
     * this function is called when someone clicks on the "Start Game" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void startGame() {
        vm.startGame();
    }

    /**
     * this function is called when someone clicks on the "Load Game" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void loadGame() {
        vm.loadGame();
    }

    /**
     * this function opens a new stage and shows the Opening Scene
     *
     */
    private void showOpeningView() {
        Stage stage = new Stage();
        stage.setTitle("Our AMAZING maze game (Show only Version patch 2.0.1)");
        vm.playMusic("./resources/Audio/Superman.mp3");
        stage.setOnCloseRequest(event -> vm.closeProgram());
        stage.setScene(openingScene);
        stage.show();
        bindStuff();
    }

    /**
     * this function is a helper function for UpdateLayout it binds the Opening View controls to the right proportions
     *
     */
    public void bindStuff() {
        mainBorderPane.prefHeightProperty().bind(openingScene.heightProperty());
        mainBorderPane.prefWidthProperty().bind(openingScene.widthProperty());
        pictureAnchorPane.prefHeightProperty().bind(mainBorderPane.prefHeightProperty().multiply(0.8));
        pictureAnchorPane.prefWidthProperty().bind(mainBorderPane.prefWidthProperty().multiply(1));
        mainVBox.prefHeightProperty().bind(mainBorderPane.prefHeightProperty().multiply(0.2));
        mainVBox.prefWidthProperty().bind(mainBorderPane.prefWidthProperty().multiply(1));
        startGameButton.prefHeightProperty().bind(mainVBox.prefHeightProperty().multiply(0.15));
        startGameButton.prefWidthProperty().bind(mainVBox.prefWidthProperty().multiply(0.3));
        loadGameButton.prefHeightProperty().bind(mainVBox.prefHeightProperty().multiply(0.15));
        loadGameButton.prefWidthProperty().bind(mainVBox.prefWidthProperty().multiply(0.3));
        Image image = new Image("BackGround/Heroes.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(pictureAnchorPane.getWidth(), pictureAnchorPane.getHeight(), false, false, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        pictureAnchorPane.setBackground(background);
    }
}
