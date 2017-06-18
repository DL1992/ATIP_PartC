package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

/**
 * Created by user on 11/06/2017.
 */
public interface IModel {

    void Create(int rowSize, int columnSize);
    void Solve();
    void Save();
    void Load(String path);
    Maze getMaze();
    Position getCurrentPosition();
    Solution getSolution();

    void moveCharacter(KeyCode moveCode);
}
