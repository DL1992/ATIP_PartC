package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.scene.canvas.Canvas;

/**
 * Created by user on 11/06/2017.
 */
public class MazeDisplay extends Canvas {

    private Maze maze;

    private Position heroPosition;

    public void setMaze(Maze newMaze){
        maze=newMaze;
        //redrawMaze()
    }

    public void setHeroPosition(Position newHeroPosition){
        heroPosition=newHeroPosition;
        //redrawHero()
    }

    public Position getHeroPosition() {
        return heroPosition;
    }



}
