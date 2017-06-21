package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;

/**
 * Created by user on 11/06/2017.
 */
public interface IModel {

    void Create(int rowSize, int columnSize);
    void Solve();
    void Save(File file);
    void Load(File file);
    void moveCharacter(KeyCode moveCode);
    void closeProgram();

    Maze getMaze();
    Position getCurrentPosition();
    Solution getSolution();
}
