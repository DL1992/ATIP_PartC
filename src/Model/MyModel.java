package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import Server.*;
import Client.*;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * Created by user on 11/06/2017.
 */
public class MyModel extends Observable implements IModel {

    private Maze gameMaze;
    private Position heroPosition;
    private Solution mazeSolution;
    private boolean isMazeChanged = false;
    private boolean isHeroPositionChanged = false;
    private boolean isSolutionChanged = false;
    private Server mazeGenerationServer;
    private Server mazeSolvingServer;

    public Maze getMaze() {
        return gameMaze;
    }

    public Position getCurrentPosition() {
        return heroPosition;
    }

    public Solution getSolution() {
        return mazeSolution;
    }

    public void setGameMaze(Maze gameMaze) {
        this.gameMaze = gameMaze;
    }

    public void setHeroPosition(Position heroPosition) {
        this.heroPosition = heroPosition;
    }

    public void setMazeSolution(Solution mazeSolution) {
        this.mazeSolution = mazeSolution;
    }

    public void startServers(){
        mazeGenerationServer = new Server(5400, 2000, new ServerStrategyGenerateMaze());
        mazeSolvingServer = new Server(5401, 2000, new ServerStrategySolveSearchProblem());
        mazeGenerationServer.start();
        mazeSolvingServer.start();
    }

    public void stopServers(){
        mazeGenerationServer.stop();
        mazeSolvingServer.stop();
    }

    @Override
    public void Create(int rowSize, int columnSize) {
        try {
            new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rowSize, columnSize};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[compressedMaze.length];
                        is.read(decompressedMaze);
                        Maze maze = new Maze(decompressedMaze);
                        gameMaze= maze;
                        heroPosition = maze.getStartPosition();
                    } catch (Exception e) {
                    }
                }
            }).communicateWithServer();
        } catch (Exception e) {
        }
        setChanged();
        notifyObservers(gameMaze);
    }

    @Override
    public void Solve() {

    }

    @Override
    public void Save() {

    }

    @Override
    public void Load(String path) {

    }
}
