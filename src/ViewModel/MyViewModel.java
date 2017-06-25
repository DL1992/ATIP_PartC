package ViewModel;

import Model.IModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel observedModel;
    public StringProperty rows;
    public StringProperty cols;
    public MediaPlayer mediaPlayer;

    public MyViewModel() {
    }

    public MyViewModel(IModel observedModel) {
        this.observedModel = observedModel;
        rows = new SimpleStringProperty();
        cols = new SimpleStringProperty();
    }

    public void generateMaze() {
        if (tryParseMazeSizes()) {
            observedModel.Create(Integer.parseInt(rows.get()), Integer.parseInt(cols.get()));
        } else {
            setChanged();
            notifyObservers("NotInt");
        }
    }

    private boolean tryParseMazeSizes() {
        try {
            Integer.parseInt(rows.get());
            Integer.parseInt(cols.get());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void solveMaze() {
        observedModel.Solve();
    }

    public void moveHero(KeyCode moveCode) {
        observedModel.moveCharacter(moveCode);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "Maze":
                setChanged();
                notifyObservers(observedModel.getMaze());
                break;
            case "HeroPosition":
                setChanged();
                notifyObservers(observedModel.getCurrentPosition());
                break;
            case "Solution":
                setChanged();
                notifyObservers(observedModel.getSolution());
                break;
            case "GameOver":
                observedModel.reset();
                setChanged();
                notifyObservers("GameOver");
                break;
            case "BadSizes":
                setChanged();
                notifyObservers("BadSizes");
                break;
            case "ShutDown":
                setChanged();
                notifyObservers("ShutDown");
                break;
        }
    }

    public void saveFile(File file) {
        observedModel.Save(file);
    }

    public void loadFile(File file) {
        observedModel.Load(file);
    }

    public void resumeGame() {
        setChanged();
        notifyObservers("Continue");
    }

    public void startGame() {
        setChanged();
        notifyObservers("StartGame");
    }

    public void loadGame() {
        setChanged();
        notifyObservers("LoadGame");
    }

    public void fromMainToOpen() {
        setChanged();
        notifyObservers("mainToOpen");
    }

    public void closeProgram() {
        observedModel.closeProgram();
    }

    public void playMusic(String musicPath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media sound = new Media(new File(musicPath).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);

    }

    public boolean isThereMaze() {
        return (observedModel.getMaze() != null);
    }
}
