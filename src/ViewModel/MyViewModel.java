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
//    @FXML
    public MazeDisplay mazeDisplay;
//    public ComboBox HeroBox;
//    public ComboBox WallBox;
//    public ComboBox GoalBox;


    public MyViewModel() {}

    public MyViewModel(IModel observedModel) {
        this.observedModel = observedModel;
    }

    public void generateMaze( MazeDisplay mazeDisplay){
        this.mazeDisplay = mazeDisplay;
        observedModel.Create(15,15);
        //Maze mazy = new MyMazeGenerator().generate(15,15);
        //mazeDisplay.setMaze(mazy);
    }

    public void updateHero(ComboBox HeroBox){
        String ThePathToTheHeroImage = "./resources/character/" +(String) HeroBox.getValue() + ".jpg";
        mazeDisplay.setHeroImageFileName(ThePathToTheHeroImage);
    }

    public void updateWalls(ComboBox WallBox){
        String ThePathToTheWallImage = "./resources/Walls/" +(String) WallBox.getValue() + ".jpg";
        mazeDisplay.setWallImageFileName(ThePathToTheWallImage);
    }

    public void updateGoal(ComboBox GoalBox){
        String ThePathToTheGoalImage = "./resources/Goal/" +(String) GoalBox.getValue() + ".jpg";
        mazeDisplay.setGoalImageFileName(ThePathToTheGoalImage);
    }

    public void tellUserAboutDevs(){

    }


    @Override
    public void update(Observable o, Object arg) {
        //if(o == observedModel){
        System.out.println("FUCK OFF");
            if(arg instanceof Maze ){
                mazeDisplay.setMaze((Maze) arg);
            }
            if(arg instanceof Position ){

            }
            if(arg instanceof Solution){

            }
        //}
    }
}
