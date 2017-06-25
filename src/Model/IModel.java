package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;

/**
 * This class is the Interface for a Modle of a maze in the mvvm architecture.
 * the purpose of the model is to be the "brain" of the game. holding all the algorithms used for it.
 * it should be observed by a some sort of a ViewModel class.
 *
 * @author Vladislav Sergienko
 * @author Doron Laadan
 */
public interface IModel {

    /**
     * This function creates the maze. the maze should be a random generated maze with a start and a goal.
     * upon finishing the maze the function should notify all the model observers.
     *
     * @param rowSize    the number of rows in the maze.
     * @param columnSize the number of columns in the maze.
     */
    void Create(int rowSize, int columnSize);

    /**
     * This function solves a given maze.
     * using any kind of search algorithm this function should create a Solution for the maze.
     * upon finishing the solution the function should notify all the model observers.
     * if, there was a problem in hte creation the function should notify the observers to throw an alert.
     */
    void Solve();

    /**
     * This function save a given maze file to the computer to be used later.
     * the file its saves must be compressed in some way to save space.
     *
     * @param file the file the maze should be save to.
     */
    void Save(File file);

    /**
     * This function load a maze from a file to the model.
     * the file must be decompressed and stored in the model.
     * upon complete the function should notify the observers of the model that the loaded maze is ready.
     *
     * @param file
     */
    void Load(File file);

    /**
     * This function control the character movement. to play a maze game the main character must be able to
     * move around the maze. this function should decide if a specific move is legal or not.
     * if it is, it should move the character to the right place, change the character position
     * and notify the observers of the model that the character has benn moved.
     * if the Character got to the goal position the function should notify to end the game.
     *
     * @param moveCode the code of the key that was preesed by the user in order to move the character
     *                 in a specific direction.
     */
    void moveCharacter(KeyCode moveCode);

    /**
     * This function is in charge of a proper closing of the game.
     * it should shut down any servers,pools,or background procedure running before closing.
     */
    void closeProgram();

    /**
     * This function should reset the properites of the model. in order to start a new clean game.
     */
    void reset();

    /**
     * This function return the maze of the model.
     * it is used by the ViewMode most of the times.
     *
     * @return the maze object used by the model.
     */
    Maze getMaze();

    /**
     * This function return the position of the hero of the maze.
     *
     * @return Poistion of the hero used by the maze.
     */
    Position getCurrentPosition();

    /**
     * This function return the solution for the maze in the model.
     *
     * @return Soultion of the maze used by the model.
     */
    Solution getSolution();
}
