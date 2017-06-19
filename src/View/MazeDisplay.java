package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Created by user on 11/06/2017.
 */
public class MazeDisplay extends Canvas {

    private Maze maze;
    private Position heroPosition;
    private StringProperty WallImageFileName = new SimpleStringProperty();
    private StringProperty HeroImageFileName = new SimpleStringProperty();
    private StringProperty GoalImageFileName = new SimpleStringProperty();


    public Position getHeroPosition() {
        return heroPosition;
    }

    public String getGoalImageFileName() {
        return GoalImageFileName.get();
    }

    public String getWallImageFileName() {
        return WallImageFileName.get();
    }

    public String getHeroImageFileName() {
        return HeroImageFileName.get();
    }

    public void setMaze(Maze newMaze){
        maze=newMaze;
        heroPosition = maze.getStartPosition();
        redraw();
    }

    public void setHeroPosition(Position newHeroPosition){
        if( null != maze) {
            int[][] mazeData = maze.getData();
            double cellWidth = getHeight() / mazeData.length;
            double cellHeight = getWidth() / (mazeData[0]).length;
            deleteHero(cellHeight, cellWidth);
            heroPosition = newHeroPosition;
            redrawHero(cellHeight, cellWidth);
        }
    }

    public void setWallImageFileName(String wallImageFileName) {
        this.WallImageFileName.set(wallImageFileName);
        redraw();
    }

    public void setHeroImageFileName(String heroImageFileName) {
        this.HeroImageFileName.set(heroImageFileName);
        redraw();
    }

    public void setGoalImageFileName(String goalImageFileName) {
        this.GoalImageFileName.set(goalImageFileName);
        redraw();
    }



    public void redraw(){
        if( null != maze) {
            int[][] mazeData = maze.getData();
            double cellWidth = getHeight() / mazeData.length;
            double cellHeight = getWidth() / (mazeData[0]).length;
            redrawWalls(cellHeight , cellWidth);
            redrawHero(cellHeight , cellWidth);
            redrawGoal(cellHeight , cellWidth);
        }
    }

    private void deleteHero(double cellHeight, double cellWidth) {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(heroPosition.getColumnIndex()*cellHeight, heroPosition.getRowIndex()*cellWidth, cellHeight, cellWidth);
    }

    private void redrawHero(double cellHeight, double cellWidth) {
        try {
            Image heroImage = new Image(new FileInputStream(HeroImageFileName.get()));
            GraphicsContext gc = getGraphicsContext2D();
            gc.drawImage(heroImage, heroPosition.getColumnIndex()*cellHeight, heroPosition.getRowIndex()*cellWidth, cellHeight, cellWidth);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
    }



    private void redrawGoal(double cellHeight, double cellWidth) {
        try {
            Image goalImage = new Image(new FileInputStream(GoalImageFileName.get()));
            GraphicsContext gc = getGraphicsContext2D();
            gc.drawImage(goalImage, maze.getGoalPosition().getColumnIndex()*cellHeight, maze.getGoalPosition().getRowIndex()*cellWidth, cellHeight, cellWidth);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
    }



    private void redrawWalls(double cellHeight, double cellWidth) {
            try {
                int[][] mazeData = maze.getData();
                Image wallImage = new Image(new FileInputStream(WallImageFileName.get()));
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0,0, getWidth() , getHeight());
                for(int i=0; i<mazeData.length; i++){
                    for (int j = 0; j < mazeData[0].length; j++) {
                        if(mazeData[i][j] == 1){
                            gc.drawImage(wallImage, j*cellHeight, i*cellWidth, cellHeight, cellWidth);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
    }


    public void drawSolution(Solution solution)
    {
        int[][] mazeData = maze.getData();
        double cellWidth = getHeight() / mazeData.length;
        double cellHeight = getWidth() / (mazeData[0]).length;
        redrawWalls(cellHeight , cellWidth);
        redrawHero(cellHeight , cellWidth);
        redrawGoal(cellHeight , cellWidth);
        ArrayList<AState> mazeSolutionPath = solution.getSolutionPath();
        for (int i = 1; i < mazeSolutionPath.size() - 1; i++) {
            Position pos = ((MazeState) mazeSolutionPath.get(i)).getPosition();
            GraphicsContext gc = getGraphicsContext2D();
            gc.setFill(Color.LIGHTGREEN);
                gc.fillRect(pos.getColumnIndex()*cellHeight, pos.getRowIndex()*cellWidth, cellHeight, cellWidth);
        }
    }

    public void drawHint(Solution solution)
    {
        int[][] mazeData = maze.getData();
        double cellWidth = getHeight() / mazeData.length;
        double cellHeight = getWidth() / (mazeData[0]).length;
        redrawWalls(cellHeight , cellWidth);
        redrawHero(cellHeight , cellWidth);
        redrawGoal(cellHeight , cellWidth);
        ArrayList<AState> mazeSolutionPath = solution.getSolutionPath();
        if(mazeSolutionPath.size() >= 3 ){
                    drawSolution(solution);
        }
        else{
            for (int i = 1; i < 3; i++) {
                Position pos = ((MazeState) mazeSolutionPath.get(i)).getPosition();
                GraphicsContext gc = getGraphicsContext2D();
                gc.setFill(Color.LIGHTGREEN);
                gc.fillRect(pos.getColumnIndex()*cellHeight, pos.getRowIndex()*cellWidth, cellHeight, cellWidth);
            }
        }
    }

}
