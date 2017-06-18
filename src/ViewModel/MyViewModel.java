package ViewModel;

import Model.IModel;
import Model.MyModel;
import View.MazeDisplay;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    IModel observedModel;


    public MyViewModel() {}

    public MyViewModel(IModel observedModel) {
        this.observedModel = observedModel;
    }

    public void generateMaze(){
        observedModel.Create(15,15);
    }

    public void solveMaze() {
        observedModel.Solve();
    }

    public void moveHero(KeyCode moveCode) {
        observedModel.moveCharacter(moveCode);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == observedModel){
            if(arg instanceof Maze ){
               setChanged();
               notifyObservers(arg);
            }
            if(arg instanceof Position ){
                setChanged();
                notifyObservers(arg);
            }
            if(arg instanceof Solution){
                setChanged();
                notifyObservers(arg);
            }
            if(arg instanceof  String){
                setChanged();
                notifyObservers(arg);
            }
        }
    }
}
