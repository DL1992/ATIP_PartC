package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

/**
 * Created by sergayen on 6/14/2017.
 */
public class MyView{
    MyViewModel vm;

    @FXML
    public MazeDisplay mazeDisplay;
    public ComboBox HeroBox;
    public ComboBox WallBox;
    public ComboBox GoalBox;

    public void setViewModel(MyViewModel vm){
        this.vm = vm;
    }

    public void generateMaze(){
        vm.generateMaze(mazeDisplay);
    }

    public void updateHero(){
        vm.updateHero(HeroBox);
    }

    public void updateWalls(){
        vm.updateWalls(WallBox);
    }

    public void updateGoal(){
        vm.updateGoal(GoalBox);
    }

    public void tellUserAboutDevs(){
        vm.tellUserAboutDevs();
    }


}
