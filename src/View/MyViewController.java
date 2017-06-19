package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by sergayen on 6/14/2017.
 */
public class MyViewController implements Observer, IView {
    MyViewModel vm;

    @FXML
    public HBox selectionPane;
    public BorderPane borderPane;
    public MazeDisplay mazeDisplay;
    public ComboBox HeroBox;
    public ComboBox WallBox;
    public ComboBox GoalBox;

    public void setViewModel(MyViewModel vm){ this.vm = vm; }

    public void generateMaze(){ vm.generateMaze(); }

    public void solveMaze(){ vm.solveMaze(); }

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

    public void editUpdateHero(ActionEvent evt){
        HeroBox.setValue( ( (MenuItem)evt.getSource() ).getText());
    }

    public void editUpdateWalls(ActionEvent evt){
        WallBox.setValue( ( (MenuItem)evt.getSource() ).getText());
    }

    public void editUpdateGoal(ActionEvent evt){
        GoalBox.setValue( ( (MenuItem)evt.getSource() ).getText());
    }

    public void save(){
        FileChooser saveFileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt))", "*.txt");
        saveFileChooser.getExtensionFilters().add(extensionFilter);
        File file = saveFileChooser.showSaveDialog((Stage) mazeDisplay.getScene().getWindow());

        if(file != null){
            vm.saveFile(file);
        }
    }

    public void load(){
        FileChooser loadFileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt))", "*.txt");
        loadFileChooser.getExtensionFilters().add(extensionFilter);
        File file = loadFileChooser.showOpenDialog((Stage) mazeDisplay.getScene().getWindow());

        if(file != null){
            vm.loadFile(file);
        }
    }


    public void tellUserAboutDevs(){

    }

    public void MoveUp(){
        vm.moveHero(KeyCode.UP);
    }

    public void MoveLeft(){
        vm.moveHero(KeyCode.LEFT);
    }

    public void MoveDown(){
        vm.moveHero(KeyCode.DOWN);
    }

    public void MoveRight(){
        vm.moveHero(KeyCode.RIGHT);
    }

    public void KeyPressed(KeyEvent keyEvent){
        vm.moveHero(keyEvent.getCode());
        keyEvent.consume();
    }




    @Override
    public void update(Observable o, Object arg) {
        mazeDisplay.addEventFilter(MouseEvent.MOUSE_CLICKED, (e)-> mazeDisplay.requestFocus());
        if(o == vm) {
//            switch ((String) arg){
//                case "Maze":
//                    mazeDisplay.setMaze( (Maze) arg);
//                    break;
//                case "Position":
//                    mazeDisplay.setHeroPosition((Position) arg);
//                    break;
//                case "Solution":
//                    mazeDisplay.drawSolution( (Solution) arg);
//                    break;
//                case "Hint":
//                    mazeDisplay.drawHint( (Solution) arg);
//                    break;
//            }
            if (arg instanceof Maze) {
                mazeDisplay.setMaze( (Maze) arg);
            }
            if(arg instanceof Position){
                mazeDisplay.setHeroPosition((Position) arg);
            }
            if(arg instanceof Solution){
                mazeDisplay.drawSolution( (Solution) arg);
            }
            if(arg instanceof  String){
                showWin();
            }
        }
    }

    private void showWin() {
        Stage stage = (Stage) mazeDisplay.getScene().getWindow();
        Parent root = null;
        FXMLLoader loader = new FXMLLoader();
        try {
            root = loader.load(getClass().getResource("MyVictoryView.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 875, 750, Color.web("CAEBF2"));
        scene.getStylesheets().add("./View/Design.css");
        String musicPath = "./resources/Audio/champions.mp3";
        Media sound = new Media(new File(musicPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        stage.setScene(scene);
        stage.show();
    }

    public void UpdateLayout() {
        updateBottom();
        updateComboBoxes();
        updateMazeDisplay();
    }

    private void updateMazeDisplay() {
        mazeDisplay.setHeight(borderPane.getHeight() * 0.7);
        mazeDisplay.setWidth(borderPane.getWidth() * 0.7);
        mazeDisplay.redraw();
    }

    private void updateBottom() {
        selectionPane.setMinHeight( borderPane.getHeight() * 0.2 );
        selectionPane.setMaxHeight( borderPane.getHeight() * 0.2 );
        selectionPane.setPrefHeight( borderPane.getHeight() * 0.2 );
        selectionPane.setMinWidth( borderPane.getWidth());
        selectionPane.setMaxWidth( borderPane.getWidth());
        selectionPane.setPrefWidth( borderPane.getWidth());
        selectionPane.setSpacing(borderPane.getWidth() * 0.05);
    }

    private void updateComboBoxes() {
        updateHeroBox();
        updateGoalBox();
        updateWallBox();
    }

    private void updateHeroBox() {
        HeroBox.setMinHeight( selectionPane.getHeight() * 0.3 );
        HeroBox.setMaxHeight( selectionPane.getHeight() * 0.3 );
        HeroBox.setPrefHeight( selectionPane.getHeight() * 0.3 );
        HeroBox.setMinWidth( selectionPane.getWidth() * 0.2 );
        HeroBox.setMaxWidth( selectionPane.getWidth() * 0.2 );
        HeroBox.setPrefWidth( selectionPane.getWidth() * 0.2 );
    }

    private void updateGoalBox() {
        GoalBox.setMinHeight( selectionPane.getHeight() * 0.3 );
        GoalBox.setMaxHeight( selectionPane.getHeight() * 0.3 );
        GoalBox.setPrefHeight( selectionPane.getHeight() * 0.3 );
        GoalBox.setMinWidth( selectionPane.getWidth() * 0.2 );
        GoalBox.setMaxWidth( selectionPane.getWidth() * 0.2 );
        GoalBox.setPrefWidth( selectionPane.getWidth() * 0.2 );
    }

    private void updateWallBox() {
        WallBox.setMinHeight( selectionPane.getHeight() * 0.3 );
        WallBox.setMaxHeight( selectionPane.getHeight() * 0.3 );
        WallBox.setPrefHeight( selectionPane.getHeight() * 0.3 );
        WallBox.setMinWidth( selectionPane.getWidth() * 0.2 );
        WallBox.setMaxWidth( selectionPane.getWidth() * 0.2 );
        WallBox.setPrefWidth( selectionPane.getWidth() * 0.2 );
    }
}
