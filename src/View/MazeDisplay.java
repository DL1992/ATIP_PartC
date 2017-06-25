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
 * This class is the mazeDisply Button.
 * the purpose of the the class is to add a new custom container to the GUI
 * one that knows how to draw a maze game.
 * it extends the canvas class of javafx.
 *
 * @author Vladislav Sergienko
 * @author Doron Laadan
 */
public class MazeDisplay extends Canvas {

    private Maze maze;
    private Position heroPosition;
    private StringProperty WallImageFileName = new SimpleStringProperty();
    private StringProperty HeroImageFileName = new SimpleStringProperty();
    private StringProperty GoalImageFileName = new SimpleStringProperty();

    /**
     * @return the string that represents the current goal image file name.
     */
    public String getGoalImageFileName() {
        return GoalImageFileName.get();
    }

    /**
     * @return the string that represents the current wall image file name.
     */
    public String getWallImageFileName() {
        return WallImageFileName.get();
    }

    /**
     * @return the string that represents the current hero image file name.
     */
    public String getHeroImageFileName() {
        return HeroImageFileName.get();
    }

    /**
     * this function sets the maze the display should draw.
     * it set also the hero position in it.
     * and then redraw the maze.
     *
     * @param newMaze the maze we want to redraw.
     */
    public void setMaze(Maze newMaze) {
        maze = newMaze;
        heroPosition = maze.getStartPosition();
        redraw();
    }

    /**
     * this function sets the hero position we want to draw.
     * it makes sure the size of the image is the right size.
     *
     * @param newHeroPosition the new position of the hero in the maze.
     */
    public void setHeroPosition(Position newHeroPosition) {
        if (null != maze) {
            int[][] mazeData = maze.getData();
            double cellWidth = getHeight() / mazeData.length;
            double cellHeight = getWidth() / (mazeData[0]).length;
            deleteHero(cellHeight, cellWidth);
            heroPosition = newHeroPosition;
            redrawHero(cellHeight, cellWidth);
        }
    }

    /**
     * this function sets the image of the walls in the maze and redraw it.
     *
     * @param wallImageFileName is the path to the new wall image.
     */
    public void setWallImageFileName(String wallImageFileName) {
        this.WallImageFileName.set(wallImageFileName);
        redraw();
    }

    /**
     * this function sets the image of the hero in the maze and redraw it.
     *
     * @param heroImageFileName is the path to the new hero image.
     */
    public void setHeroImageFileName(String heroImageFileName) {
        this.HeroImageFileName.set(heroImageFileName);
        redraw();
    }

    /**
     * this function sets the image of the goal in the maze and redraw it.
     *
     * @param goalImageFileName is the path to the new goal image.
     */
    public void setGoalImageFileName(String goalImageFileName) {
        this.GoalImageFileName.set(goalImageFileName);
        redraw();
    }

    /**
     * The main function of the maze disply.
     * it sets the right sizes of each cell and then redraw the maze on it accordingly.
     * it draws the walls,the hero and the goal.
     */
    public void redraw() {
        if (null != maze) {
            int[][] mazeData = maze.getData();
            double cellWidth = getHeight() / mazeData.length;
            double cellHeight = getWidth() / (mazeData[0]).length;
            redrawWalls(cellHeight, cellWidth);
            redrawHero(cellHeight, cellWidth);
            redrawGoal(cellHeight, cellWidth);
        }
    }

    /**
     * This function is used to delete the image of a hero after it moves.
     * helper function to set hero position.
     *
     * @param cellHeight the cell height.
     * @param cellWidth  the cell width.
     */
    private void deleteHero(double cellHeight, double cellWidth) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(heroPosition.getColumnIndex() * cellHeight, heroPosition.getRowIndex() * cellWidth, cellHeight, cellWidth);
    }

    /**
     * This function is used to draw the hero image in the right place in the maze.
     *
     * @param cellHeight the cell height.
     * @param cellWidth  the cell width.
     */
    private void redrawHero(double cellHeight, double cellWidth) {
        try {
            Image heroImage = new Image(new FileInputStream(HeroImageFileName.get()));
            GraphicsContext gc = getGraphicsContext2D();
            gc.drawImage(heroImage, heroPosition.getColumnIndex() * cellHeight, heroPosition.getRowIndex() * cellWidth, cellHeight, cellWidth);
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * This function is used to draw the goal image in the right place in the maze.
     *
     * @param cellHeight the cell height.
     * @param cellWidth  the cell width.
     */
    private void redrawGoal(double cellHeight, double cellWidth) {
        try {
            Image goalImage = new Image(new FileInputStream(GoalImageFileName.get()));
            GraphicsContext gc = getGraphicsContext2D();
            gc.drawImage(goalImage, maze.getGoalPosition().getColumnIndex() * cellHeight, maze.getGoalPosition().getRowIndex() * cellWidth, cellHeight, cellWidth);
        } catch (FileNotFoundException e) {
        }
    }


    /**
     * This function is used to draw the walls image all over the maze.
     *
     * @param cellHeight the cell height.
     * @param cellWidth  the cell width.
     */
    private void redrawWalls(double cellHeight, double cellWidth) {
        try {
            int[][] mazeData = maze.getData();
            Image wallImage = new Image(new FileInputStream(WallImageFileName.get()));
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
            for (int i = 0; i < mazeData.length; i++) {
                for (int j = 0; j < mazeData[0].length; j++) {
                    if (mazeData[i][j] == 1) {
                        gc.drawImage(wallImage, j * cellHeight, i * cellWidth, cellHeight, cellWidth);
                    }
                }
            }
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * This function is used to draw the solution path from the model on the maze.
     * it start from where the hero is and ends in the goal.
     *
     * @param solution is the solution of the maze given by the model.
     */

    public void drawSolution(Solution solution) {
        int[][] mazeData = maze.getData();
        double cellWidth = getHeight() / mazeData.length;
        double cellHeight = getWidth() / (mazeData[0]).length;
        ArrayList<AState> mazeSolutionPath = solution.getSolutionPath();
        for (int i = 1; i < mazeSolutionPath.size() - 1; i++) {
            Position pos = ((MazeState) mazeSolutionPath.get(i)).getPosition();
            GraphicsContext gc = getGraphicsContext2D();
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(pos.getColumnIndex() * cellHeight, pos.getRowIndex() * cellWidth, cellHeight, cellWidth);
        }
    }

    /**
     * This function is used to clear the display after switching scenes.
     */
    public void clear() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
