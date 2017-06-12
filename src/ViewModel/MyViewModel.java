package ViewModel;

import Model.IModel;
import View.MazeDisplay;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    @FXML
    public MazeDisplay mazeDisplay;
    public ComboBox HeroBox;
    public ComboBox WallBox;
    public ComboBox GoalBox;

    IModel observedModel;

    public void generateMaze(){
        Maze mazy = new MyMazeGenerator().generate(15,15);
        mazeDisplay.setMaze(mazy);
    }

    public void updateHero(){
        String ThePathToTheHeroImage = "./resources/character/" +(String) HeroBox.getValue() + ".jpg";
        mazeDisplay.setHeroImageFileName(ThePathToTheHeroImage);
    }

    public void updateWalls(){
        String ThePathToTheWallImage = "./resources/Walls/" +(String) WallBox.getValue() + ".jpg";
        mazeDisplay.setWallImageFileName(ThePathToTheWallImage);
    }

    public void updateGoal(){
        String ThePathToTheGoalImage = "./resources/Goal/" +(String) GoalBox.getValue() + ".jpg";
        mazeDisplay.setGoalImageFileName(ThePathToTheGoalImage);
    }




    @Override
    public void update(Observable o, Object arg) {
    }
}
