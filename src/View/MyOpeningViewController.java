package View;

import ViewModel.MyViewModel;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by sergayen on 6/20/2017.
 */
public class MyOpeningViewController implements IView, Observer {
    private MyViewModel vm;
    public Scene openingScene;

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
                if (((String) arg).equals("Continue") || ((String) arg).equals("mainToOpen")) {
                    showOpeningView();
                }
                if (((String) arg).equals("StartGame") || ((String) arg).equals("LoadGame")) {
                    ((Stage) openingScene.getWindow()).close();
                }
                if( ((String) arg).equals("ShutDown")) {
                    if((Stage) openingScene.getWindow() != null){
                        ((Stage) openingScene.getWindow()).close();
                    }
                }
            }
        }
    }

    public void startGame(){
        vm.startGame();
    }

    public void loadGame(){
        vm.loadGame();
    }

    private void showOpeningView() {
        Stage stage = new Stage();
        stage.setTitle("Our AMAZING maze game (Show only Version patch 2.0.1)");
        vm.playMusic("./resources/Audio/Superman.mp3");
        stage.setOnCloseRequest(event -> vm.closeProgram());
        stage.setScene(openingScene);
        stage.show();
    }
}
