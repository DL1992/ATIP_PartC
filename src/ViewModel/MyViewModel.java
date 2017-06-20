package ViewModel;

import Model.IModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel observedModel;
    public StringProperty rows;
    public StringProperty cols;
    public Scene scene1;
    public Scene scene2;

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
            showAlert("PLEASE ENTER FUCKING NUMBERS AS MAZE SIZES YOU GOD DAMN RETARD");
        }
    }

    private void showAlert(String AFUCKINGMESSEGESTRING) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(AFUCKINGMESSEGESTRING);
        alert.show();
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
           case "Solution":
               setChanged();
               notifyObservers(observedModel.getSolution());
               break;
           case "GameOver":
               setChanged();
               notifyObservers("GameOver");
               break;
           case "HeroPosition":
               setChanged();
               notifyObservers(observedModel.getCurrentPosition());
               break;
       }
    }

    public void saveFile(File file) {
        observedModel.Save(file);
    }

    public void loadFile(File file) {
        observedModel.Load(file);
    }

    public void showWin() {

    }

    public void swichToMain() {

    }
}
