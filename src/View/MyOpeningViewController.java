package View;

import ViewModel.MyViewModel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
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
                    openingScene.getWindow().hide();
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
//        String musicPath = "./resources/Audio/champions.mp3";
//        Media sound = new Media(new File(musicPath).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(sound);
//        mediaPlayer.play();
        stage.setScene(openingScene);
        stage.show();
    }
}
