package View;

import Server.properties;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Created by sergayen on 6/14/2017.
 */
public class MyViewController implements Observer, IView {
    private MyViewModel vm;
    public Scene mainScene;
    public TextField rows = new TextField();
    public TextField cols = new TextField();


    @FXML
    public HBox selectionPane;
    public BorderPane borderPane;
    public MazeDisplay mazeDisplay;
    public ComboBox HeroBox;
    public ComboBox WallBox;
    public ComboBox GoalBox;
    public Button backToTheMain;
    public Slider slide;

    public void setViewModel(MyViewModel vm){
        this.vm = vm;
        vm.rows.bind(rows.textProperty());
        vm.cols.bind(cols.textProperty());
        mazeDisplay.addEventFilter(MouseEvent.MOUSE_CLICKED, (e)-> mazeDisplay.requestFocus());
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
        alert.setOnCloseRequest(event -> createMaze());
    }

    public void generateMaze(){ vm.fromMainToOpen(); }

    public void solveMaze(){ vm.solveMaze(); }

    public void updateHero(){
        String ThePathToTheHeroImage = "./resources/character/" +(String) HeroBox.getValue() + ".jpg";
        vm.playMusic("./resources/Audio/" + (String) HeroBox.getValue() + ".mp3");
        vm.mediaPlayer.volumeProperty().bindBidirectional(slide.valueProperty());
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
        String text = "";
        try {
            Scanner scan = new Scanner( new File("./resources/About/AboutUs.txt"));
            text = scan.useDelimiter("\\A").next();
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Stage stage = new Stage();
        stage.setTitle("About us");
        AnchorPane layout = new AnchorPane();
        layout.getChildren().add(new Label(text));

        Scene scene = new Scene(layout, 400, 300, Paint.valueOf("Red"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        //let us tell you abit about ourselves :/
    }

    public void tellUserAboutTheGame(){
        String text = "";
        try {
            Scanner scan = new Scanner( new File("./resources/About/AboutTheGame.txt"));
            text = scan.useDelimiter("\\A").next();
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Stage stage = new Stage();
        stage.setTitle("About TheGame");
        AnchorPane layout = new AnchorPane();
        layout.getChildren().add(new Label(text));

        Scene scene = new Scene(layout, 400, 300, Paint.valueOf("Red"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        //let us tell you abit about ourselves :/
    }

    public void rickrolledUser() {

        Stage stage = new Stage();
        stage.setTitle("<3");
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        Scene scene = new Scene(webView);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    public void showProperties(){
        String text = String.format("Maze creation algorithm: %s \n" +
                                    "Maze solving algorithm: %s \n" +
                                    "threadPool size: %s"
                                    ,properties.getServerMazeGenerateAlgo()
                                    ,properties.getServerSolveMazeAlgo(),
                                    properties.getServerThreadPoolCount());
        Stage stage = new Stage();
        stage.setTitle("The Properties");
        AnchorPane layout = new AnchorPane();
        layout.getChildren().add(new Label(text));

        Scene scene = new Scene(layout, 400, 300, Paint.valueOf("Red"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }



    public void createMaze(){
        Stage stage = new Stage();
        stage.setOnCloseRequest(event -> {

            if(!vm.isThereMaze()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("if you click play you MUST create a maze");
                alert.show();
                alert.setOnCloseRequest(event2 -> vm.fromMainToOpen());

            }
        }
        );
        stage.setTitle("Gathering Data.....");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label instructionLabel = new Label("Please Enter");
        grid.setConstraints(instructionLabel,0,0);

        Label rowLabel = new Label("Number of rows :");
        grid.setConstraints(rowLabel,0,1);
        grid.setConstraints(rows,1,1);
        Label colLabel = new Label("Number of columns :");
        grid.setConstraints(colLabel,0,2);
        grid.setConstraints(cols,1,2);

        Button play = new Button("Create");
        grid.setConstraints(play,1,3);
        play.setOnAction(event -> {vm.generateMaze(); stage.close();
        });

        grid.getChildren().addAll(rowLabel,rows,colLabel,cols,play,instructionLabel);
        Scene scene = new Scene(grid, 400, 150);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
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
        if(o == vm) {
            if (arg instanceof Maze) {
                mazeDisplay.setMaze( (Maze) arg);
            }
            if(arg instanceof Position){
                mazeDisplay.setHeroPosition((Position) arg);
            }
            if(arg instanceof Solution){
                mazeDisplay.drawSolution( (Solution) arg);
            }
            if(arg instanceof String){
                if( ((String) arg).equals("GameOver")) {
                    showWin();
                }
                if( ((String) arg).equals("BadSizes")) {
                    showAlert("Please enter only natural numbers bigger then 1 as maze sizes :/ ");
                }
                if( ((String) arg).equals("NotInt")) {
                    showAlert("Please only numbers as maze sizes :/ ");
                }
                if( ((String) arg).equals("StartGame")) {
                    startGame();
                }
                if( ((String) arg).equals("LoadGame")) {
                    loadGame();
                }
                if( ((String) arg).equals("mainToOpen")) {
                    ((Stage) mainScene.getWindow()).close();
                }
                if( ((String) arg).equals("ShutDown")) {
                    if((Stage) mainScene.getWindow() != null){
                        ((Stage) mainScene.getWindow()).close();
                    }
                }
            }
        }
    }

    private void loadGame() {
        Stage stage = new Stage();
        stage.setOnCloseRequest(event -> vm.closeProgram());
        stage.setScene(mainScene);
        vm.mediaPlayer.volumeProperty().bindBidirectional(slide.valueProperty());
        stage.show();
        load();
    }

    private void startGame() {
        Stage stage = new Stage();
        stage.setOnCloseRequest(event -> vm.closeProgram());
        stage.setScene(mainScene);
        vm.mediaPlayer.volumeProperty().bindBidirectional(slide.valueProperty());
        stage.show();
        createMaze();
    }

    public void closeProgram(){
        vm.closeProgram();
    }

    private void showWin() {
        mazeDisplay.clear();
        ((Stage) mainScene.getWindow()).close();
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

    public void setVolume(int value){
        vm.mediaPlayer.volumeProperty().bindBidirectional(slide.valueProperty());
        slide.setValue(value);
    }

    public void zoomIn(){
        mazeDisplay.setScaleX(mazeDisplay.getScaleX()*2);
        mazeDisplay.setScaleY(mazeDisplay.getScaleY()*2);
        mazeDisplay.setScaleZ(mazeDisplay.getScaleZ()*2);
    }

}
