package ViewModel;

import Model.IModel;
import Model.MyModel;
import View.MazeDisplay;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel observedModel;
    public StringProperty rows;
    public StringProperty cols;


    public MyViewModel() {}

    public MyViewModel(IModel observedModel) {
        this.observedModel = observedModel;
        rows = new SimpleStringProperty();
        cols = new SimpleStringProperty();
    }

    public void generateMaze(){
        if(tryParseMazeSizes()){
            observedModel.Create(Integer.parseInt(rows.get()),Integer.parseInt(cols.get()));
        }
        else{
            setChanged();
            notifyObservers("NotInt");
        }
    }

    private boolean tryParseMazeSizes() {
        try {
            Integer.parseInt(rows.get());
            Integer.parseInt(cols.get());
            return true;
        } catch (NumberFormatException e){
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
       switch ((String) arg){
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
               setChanged();
               notifyObservers("GameOver");
               break;
           case "BadSizes":
               setChanged();
               notifyObservers("BadSizes");
               break;
       }
    }

    public void saveFile(File file) {
        observedModel.Save(file);
    }

    public void loadFile(File file) {
        observedModel.Load(file);
    }
}
