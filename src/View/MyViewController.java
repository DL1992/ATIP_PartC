package View;

import Server.properties;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
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
 * This class is the Controller for the Main game scene as part of the view in the MVVM architecture
 *
 * @author Vladislav Sergienko
 * @author Doron Laadan
 */
public class MyViewController implements Observer, IView {
    private MyViewModel vm;
    public Scene mainScene;
    public TextField rows = new TextField();
    public TextField cols = new TextField();


    @FXML
    public BorderPane borderPane;
    public MenuBar menuBar;
    public AnchorPane MazeAnchor;
    public HBox SelectionHBox;
    public VBox rightSideMainVBox;
    public MazeDisplay mazeDisplay;
    public ComboBox HeroBox;
    public ComboBox WallBox;
    public ComboBox GoalBox;
    public VBox rightSideButtonsVBox;
    public Button backToOpeningButton;
    public Button saveMazeButton;
    public Button loadMazeButton;
    public Button solveMazeButton;
    public GridPane movementGridPane;
    public Button upButton;
    public Button leftButton;
    public Button downButton;
    public Button rightButton;
    public HBox volumeHBox;
    public Label volumeLabel;
    public Slider volumeSlider;

    @Override
    public void setViewModel(MyViewModel vm) {
        this.vm = vm;
        vm.rows.bind(rows.textProperty());
        vm.cols.bind(cols.textProperty());
        mazeDisplay.addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> mazeDisplay.requestFocus());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == vm) {
            if (arg instanceof Maze) {
                mazeDisplay.setMaze((Maze) arg);
            }
            if (arg instanceof Position) {
                mazeDisplay.setHeroPosition((Position) arg);
            }
            if (arg instanceof Solution) {
                mazeDisplay.drawSolution((Solution) arg);
            }
            if (arg instanceof String) {
                if (((String) arg).equals("GameOver")) {
                    showWin();
                }
                if (((String) arg).equals("BadSizes")) {
                    showAlert("Please enter only natural numbers bigger then 1 as maze sizes :/ ");
                }
                if (((String) arg).equals("NotInt")) {
                    showAlert("Please only numbers as maze sizes :/ ");
                }
                if (((String) arg).equals("StartGame")) {
                    startGame();
                }
                if (((String) arg).equals("LoadGame")) {
                    loadGame();
                }
                if (((String) arg).equals("mainToOpen")) {
                    ((Stage) mainScene.getWindow()).close();
                }
                if (((String) arg).equals("ShutDown")) {
                    if ((Stage) mainScene.getWindow() != null) {
                        ((Stage) mainScene.getWindow()).close();
                    }
                }
            }
        }
    }

    @Override
    public void UpdateLayout() {
        mazeDisplay.redraw();
    }

    /**
     * this function is called when someone clicks on the "Try Another Maze" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void backToOpening() {
        vm.fromMainToOpen();
    }

    /**
     * this function is called when someone clicks on the "Save Maze" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void save() {
        FileChooser saveFileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt))", "*.txt");
        saveFileChooser.getExtensionFilters().add(extensionFilter);
        File file = saveFileChooser.showSaveDialog((Stage) mazeDisplay.getScene().getWindow());

        if (file != null) {
            vm.saveFile(file);
        }
    }

    /**
     * this function is called when someone clicks on the "Load Maze" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void load() {
        FileChooser loadFileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt))", "*.txt");
        loadFileChooser.getExtensionFilters().add(extensionFilter);
        File file = loadFileChooser.showOpenDialog((Stage) mazeDisplay.getScene().getWindow());

        if (file != null) {
            vm.loadFile(file);
        }
    }

    /**
     * this function is called when someone clicks on the "Highlight Path" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void solveMaze() {
        vm.solveMaze();
    }

    /**
     * this function is called when someone changes the hero image.
     * it sends the signal to the vm to change the music.
     * it also updates the hero image
     *
     */
    public void updateHero() {
        String ThePathToTheHeroImage = "./resources/character/" + (String) HeroBox.getValue() + ".png";
        vm.playMusic("./resources/Audio/" + (String) HeroBox.getValue() + ".mp3");
        vm.mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        mazeDisplay.setHeroImageFileName(ThePathToTheHeroImage);
    }

    /**
     * this function is called when someone changes the wall image.
     * it also updates the wall image
     *
     */
    public void updateWalls() {
        String ThePathToTheWallImage;
        if (WallBox.getValue().equals("FireWall")) {
            ThePathToTheWallImage = "./resources/Walls/" + (String) WallBox.getValue() + ".png";
        } else {
            ThePathToTheWallImage = "./resources/Walls/" + (String) WallBox.getValue() + ".jpg";
        }
        mazeDisplay.setWallImageFileName(ThePathToTheWallImage);
    }

    /**
     * this function is called when someone changes the goal image.
     * it also updates the Goal image
     *
     */
    public void updateGoal() {
        String ThePathToTheGoalImage = "./resources/Goal/" + (String) GoalBox.getValue() + ".png";
        mazeDisplay.setGoalImageFileName(ThePathToTheGoalImage);
    }

    /**
     * this function is called when someone changes the hero image.
     * it sends the signal to the vm to change the music.
     * it also updates the hero image
     *
     */
    public void editUpdateHero(ActionEvent evt) {
        HeroBox.setValue(((MenuItem) evt.getSource()).getText());
    }

    /**
     * this function is called when someone changes the wall image.
     * it also updates the wall image
     *
     */
    public void editUpdateWalls(ActionEvent evt) {
        WallBox.setValue(((MenuItem) evt.getSource()).getText());
    }

    /**
     * this function is called when someone changes the goal image.
     * it also updates the Goal image
     *
     */
    public void editUpdateGoal(ActionEvent evt) {
        GoalBox.setValue(((MenuItem) evt.getSource()).getText());
    }

    /**
     * this function is called when someone clicks on the "About - Us" button
     * it opens a new window with a text about the creators of this program
     *
     */
    public void tellUserAboutDevs() {
        String text = "";
        try {
            Scanner scan = new Scanner(new File("./resources/About/AboutUs.txt"));
            text = scan.useDelimiter("\\A").next();
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Stage stage = new Stage();
        stage.setTitle("About us");
        AnchorPane layout = new AnchorPane();
        Label textLabel = new Label(text);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setFont(Font.font("Segoi", 15));
        layout.getChildren().add(textLabel);

        Scene scene = new Scene(layout, 476, 400, Paint.valueOf("Red"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * this function is called when someone clicks on the "Help - Instructions" button
     * it opens a new window with a text about the Instructions for this game
     *
     */
    public void showInstructions() {
        String text = "";
        try {
            Scanner scan = new Scanner(new File("./resources/About/Instructions.txt"));
            text = scan.useDelimiter("\\A").next();
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Stage stage = new Stage();
        stage.setTitle("Instructions");
        AnchorPane layout = new AnchorPane();
        Label textLabel = new Label(text);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setFont(Font.font("Segoi", 15));
        layout.getChildren().add(textLabel);

        Scene scene = new Scene(layout, 530, 600, Paint.valueOf("Red"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * this function is called when someone clicks on the "About - The Game" button
     * it opens a new window with a text about this game
     *
     */
    public void tellUserAboutTheGame() {
        String text = "";
        try {
            Scanner scan = new Scanner(new File("./resources/About/AboutTheGame.txt"));
            text = scan.useDelimiter("\\A").next();
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Stage stage = new Stage();
        stage.setTitle("About The Game");
        AnchorPane layout = new AnchorPane();
        Label textLabel = new Label(text);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setFont(Font.font("Segoi", 15));
        layout.getChildren().add(textLabel);

        Scene scene = new Scene(layout, 515, 360, Paint.valueOf("Red"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * this function is called when someone clicks on the "About - Hidden Knowledge" button
     * it opens a new window with a rickroll
     *
     */
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

    /**
     * this function is called when someone clicks on the "Help - Properties" button
     * it opens a new window with a text about the properties
     *
     */
    public void showProperties() {
        String text = String.format("Maze creation algorithm: %s \n" +
                        "Maze solving algorithm: %s \n" +
                        "threadPool size: %s"
                , properties.getServerMazeGenerateAlgo()
                , properties.getServerSolveMazeAlgo(),
                properties.getServerThreadPoolCount());
        Stage stage = new Stage();
        stage.setTitle("The Properties");
        AnchorPane layout = new AnchorPane();
        Label textLabel = new Label(text);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setFont(Font.font("Segoi", 15));
        layout.getChildren().add(textLabel);

        Scene scene = new Scene(layout, 300, 75, Paint.valueOf("Red"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * this function is called when someone clicks on the "Create Maze" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void createMaze() {
        Stage stage = new Stage();
        stage.setOnCloseRequest(event -> {

                    if (!vm.isThereMaze()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("if you click play you MUST create a maze");
                        alert.show();
                        alert.setOnCloseRequest(event2 -> vm.fromMainToOpen());

                    }
                }
        );
        stage.setTitle("Gathering Data.....");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label instructionLabel = new Label("Please Enter");
        grid.setConstraints(instructionLabel, 0, 0);

        Label rowLabel = new Label("Number of rows :");
        grid.setConstraints(rowLabel, 0, 1);
        grid.setConstraints(rows, 1, 1);
        Label colLabel = new Label("Number of columns :");
        grid.setConstraints(colLabel, 0, 2);
        grid.setConstraints(cols, 1, 2);

        Button play = new Button("Create");
        grid.setConstraints(play, 1, 3);
        play.setOnAction(event -> {
            vm.generateMaze();
            stage.close();
        });

        rows.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                vm.generateMaze();
                stage.close();
            }
        });

        cols.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                vm.generateMaze();
                stage.close();
            }
        });

        grid.getChildren().addAll(rowLabel, rows, colLabel, cols, play, instructionLabel);
        Scene scene = new Scene(grid, 400, 150);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * this function is called when someone clicks on the "Move Up" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void MoveUp() {
        vm.moveHero(KeyCode.UP);
    }

    /**
     * this function is called when someone clicks on the "Move Left" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void MoveLeft() {
        vm.moveHero(KeyCode.LEFT);
    }

    /**
     * this function is called when someone clicks on the "Move Down" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void MoveDown() {
        vm.moveHero(KeyCode.DOWN);
    }

    /**
     * this function is called when someone clicks on the "Move Right" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void MoveRight() {
        vm.moveHero(KeyCode.RIGHT);
    }

    /**
     * this function is called when someone presses a key - it sends it to the View Model
     */
    public void KeyPressed(KeyEvent keyEvent) {
        vm.moveHero(keyEvent.getCode());
        keyEvent.consume();
    }

    /**
     * this function creates this scene from a new maze
     *
     */
    private void startGame() {
        Stage stage = new Stage();
        stage.setTitle("Our AMAZING maze game (Show only Version patch 2.0.1)");
        stage.setOnCloseRequest(event -> vm.closeProgram());
        stage.setScene(mainScene);
        bindStuff();
        vm.mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        stage.show();
        createMaze();
    }

    /**
     * this function creates this scene from a loaded maze
     *
     */
    private void loadGame() {
        Stage stage = new Stage();
        stage.setOnCloseRequest(event -> vm.closeProgram());
        stage.setScene(mainScene);
        bindStuff();
        vm.mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        stage.show();
        load();
    }

    /**
     * closes this window to allow the victory scene to show
     */
    private void showWin() {
        mazeDisplay.clear();
        ((Stage) mainScene.getWindow()).close();
    }

    /**
     * this function sets the volume of the music to a given Value
     *
     * @param value is the new music volume value
     */
    public void setVolume(int value) {
        vm.mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        volumeSlider.setValue(value);
    }

    /**
     * this function is called when someone clicks on the "Exit" button
     * it sends the signal to the vm and lets it decide what to do.
     *
     */
    public void closeProgram() {
        vm.closeProgram();
    }

    /**
     * this function is called when someone enters invalid maze creation indexes
     * it opens an alert box with an appropriate text.
     *
     */
    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
        alert.setOnCloseRequest(event -> createMaze());
    }

    /**
     * this function is a helper function for UpdateLayout it binds the Opening View controls to the right proportions
     *
     */
    private void bindStuff() {
        borderPane.prefHeightProperty().bind(mainScene.heightProperty());
        borderPane.prefWidthProperty().bind(mainScene.widthProperty());
        menuBar.prefHeightProperty().bind(borderPane.prefHeightProperty().multiply(0.03));
        menuBar.prefWidthProperty().bind(borderPane.prefWidthProperty().multiply(1));
        MazeAnchor.prefHeightProperty().bind(borderPane.prefHeightProperty().multiply(0.7));
        MazeAnchor.prefWidthProperty().bind(borderPane.prefWidthProperty().multiply(0.7));
        mazeDisplay.heightProperty().bind(MazeAnchor.prefHeightProperty().multiply(1));
        mazeDisplay.widthProperty().bind(MazeAnchor.prefWidthProperty().multiply(1));
        SelectionHBox.prefHeightProperty().bind(borderPane.prefHeightProperty().multiply(0.2));
        SelectionHBox.prefWidthProperty().bind(borderPane.prefWidthProperty().multiply(1));
        HeroBox.prefHeightProperty().bind(SelectionHBox.prefHeightProperty().multiply(0.25));
        HeroBox.prefWidthProperty().bind(SelectionHBox.prefWidthProperty().multiply(0.17));
        WallBox.prefHeightProperty().bind(SelectionHBox.prefHeightProperty().multiply(0.25));
        WallBox.prefWidthProperty().bind(SelectionHBox.prefWidthProperty().multiply(0.17));
        GoalBox.prefHeightProperty().bind(SelectionHBox.prefHeightProperty().multiply(0.25));
        GoalBox.prefWidthProperty().bind(SelectionHBox.prefWidthProperty().multiply(0.17));
        rightSideMainVBox.prefHeightProperty().bind(borderPane.prefHeightProperty().multiply(0.7));
        rightSideMainVBox.prefWidthProperty().bind(borderPane.prefWidthProperty().multiply(0.27));
        rightSideButtonsVBox.prefHeightProperty().bind(rightSideMainVBox.prefHeightProperty().multiply(0.5));
        rightSideButtonsVBox.prefWidthProperty().bind(rightSideMainVBox.prefWidthProperty().multiply(1));
        backToOpeningButton.prefHeightProperty().bind(rightSideButtonsVBox.prefHeightProperty().multiply(0.2));
        backToOpeningButton.prefWidthProperty().bind(rightSideButtonsVBox.prefWidthProperty().multiply(0.9));
        saveMazeButton.prefHeightProperty().bind(rightSideButtonsVBox.prefHeightProperty().multiply(0.2));
        saveMazeButton.prefWidthProperty().bind(rightSideButtonsVBox.prefWidthProperty().multiply(0.9));
        loadMazeButton.prefHeightProperty().bind(rightSideButtonsVBox.prefHeightProperty().multiply(0.2));
        loadMazeButton.prefWidthProperty().bind(rightSideButtonsVBox.prefWidthProperty().multiply(0.9));
        solveMazeButton.prefHeightProperty().bind(rightSideButtonsVBox.prefHeightProperty().multiply(0.2));
        solveMazeButton.prefWidthProperty().bind(rightSideButtonsVBox.prefWidthProperty().multiply(0.9));
        movementGridPane.prefHeightProperty().bind(rightSideMainVBox.prefHeightProperty().multiply(0.4));
        movementGridPane.prefWidthProperty().bind(rightSideMainVBox.prefWidthProperty().multiply(1));
        upButton.prefHeightProperty().bind(movementGridPane.prefHeightProperty().multiply(0.33));
        upButton.prefWidthProperty().bind(movementGridPane.prefWidthProperty().multiply(0.33));
        leftButton.prefHeightProperty().bind(movementGridPane.prefHeightProperty().multiply(0.33));
        leftButton.prefWidthProperty().bind(movementGridPane.prefWidthProperty().multiply(0.33));
        downButton.prefHeightProperty().bind(movementGridPane.prefHeightProperty().multiply(0.33));
        downButton.prefWidthProperty().bind(movementGridPane.prefWidthProperty().multiply(0.33));
        rightButton.prefHeightProperty().bind(movementGridPane.prefHeightProperty().multiply(0.33));
        rightButton.prefWidthProperty().bind(movementGridPane.prefWidthProperty().multiply(0.33));
        volumeHBox.prefHeightProperty().bind(rightSideMainVBox.prefHeightProperty().multiply(0.2));
        volumeHBox.prefWidthProperty().bind(rightSideMainVBox.prefWidthProperty().multiply(1));
        volumeLabel.prefHeightProperty().bind(volumeHBox.prefHeightProperty().multiply(1));
        volumeLabel.prefWidthProperty().bind(volumeHBox.prefWidthProperty().multiply(0.33));
        volumeSlider.prefHeightProperty().bind(volumeHBox.prefHeightProperty().multiply(1));
        volumeSlider.prefWidthProperty().bind(volumeHBox.prefWidthProperty().multiply(0.66));

    }
}
