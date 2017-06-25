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


/**
 * This class is the ViewModel in the mvvm architecture.
 * the purpose of the Viewmodel is to be the "bridge" between the view and the model.
 * this ViewModel connects between 3 different views.
 * it should be observed by a some sort of a View class.
 * it should observer the model.
 *
 * @author Vladislav Sergienko
 * @author Doron Laadan
 */
public class MyViewModel extends Observable implements Observer {

    private IModel observedModel;
    public StringProperty rows;
    public StringProperty cols;
    public MediaPlayer mediaPlayer;

    public MyViewModel() {
    }

    /**
     * A constructor for the ViewModel
     *
     * @param observedModel is the model we observe.
     */
    public MyViewModel(IModel observedModel) {
        this.observedModel = observedModel;
        rows = new SimpleStringProperty();
        cols = new SimpleStringProperty();
    }

    /**
     * This function is used to tell the model it need to generate a new maze.
     * it uses the bounded strings from the create new maze window in the view to send the model the
     * right params.
     * if the params are not int then it notify the view to show a alert box.
     */
    public void generateMaze() {
        if (tryParseMazeSizes()) {
            observedModel.Create(Integer.parseInt(rows.get()), Integer.parseInt(cols.get()));
        } else {
            setChanged();
            notifyObservers("NotInt");
        }
    }

    /**
     * this function checks if you can use the strings in the textBox as integers for the maze
     * a helper function for generateMaze.
     *
     * @return true if you can parse both of the strings, false otherwise.
     */
    private boolean tryParseMazeSizes() {
        try {
            Integer.parseInt(rows.get());
            Integer.parseInt(cols.get());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Tells the model to solve the current maze.
     */
    public void solveMaze() {
        observedModel.Solve();
    }

    /**
     * Tell the mode to move the hero.
     *
     * @param moveCode the KeyCode the player enter in the view.
     */
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

    /**
     * Tells the model to save the current maze.
     *
     * @param file the file we want to save the maze to. chosen by the user in the view.
     */
    public void saveFile(File file) {
        observedModel.Save(file);
    }

    /**
     * Tells the model to load a specific maze.
     *
     * @param file the file we want to load the maze from. chosen by the user in the view.
     */
    public void loadFile(File file) {
        observedModel.Load(file);
    }

    /**
     * Switch the views between the victory view to the opening view.
     */
    public void resumeGame() {
        setChanged();
        notifyObservers("Continue");
    }

    /**
     * Switch the views between the opening view to the game/main view while opening the new maze window.
     */
    public void startGame() {
        setChanged();
        notifyObservers("StartGame");
    }

    /**
     * Switch the views between the opening view to the game/main view while opening the load maze window.
     */
    public void loadGame() {
        setChanged();
        notifyObservers("LoadGame");
    }

    /**
     * Switch the views between the game/main view to the opening view.
     */
    public void fromMainToOpen() {
        setChanged();
        notifyObservers("mainToOpen");
    }

    /**
     * Tells the model to close the program properly.
     * happen if the user press the X or exit buttons.
     */
    public void closeProgram() {
        observedModel.closeProgram();
    }

    /**
     * This function is in charge of decide which music the media player will play in any given scene
     * or moment in the game.
     *
     * @param musicPath the path to the music file we want to play.
     */
    public void playMusic(String musicPath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media sound = new Media(new File(musicPath).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);

    }

    /**
     * This function is used by the vies to check if there is a given maze right now in the model.
     * int order to see if we can start showing the game in the main scene.
     *
     * @return true if the model has a maze, false otherwise
     */
    public boolean isThereMaze() {
        return (observedModel.getMaze() != null);
    }
}
