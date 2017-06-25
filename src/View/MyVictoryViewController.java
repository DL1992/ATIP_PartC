package View;

import ViewModel.MyViewModel;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by sergayen on 6/20/2017.
 */
public class MyVictoryViewController implements Observer, IView{
    private MyViewModel vm;
    public Scene victoryScene;

    public void setViewModel(MyViewModel vm){
        this.vm = vm;
    }

    @Override
    public void UpdateLayout() {

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
        stage.show();
    }
}
