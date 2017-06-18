package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import Server.*;
import Client.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService threadPool = Executors.newFixedThreadPool(15);

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

    //TODO: check weird bug in QA - the maze display update bug
    @Override
    public void Create(int rowSize, int columnSize) {
        threadPool.execute(() -> {
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
                            gameMaze = maze;
                            heroPosition = maze.getStartPosition();
                        } catch (Exception e) {
                        }
                    }
                }).communicateWithServer();

            } catch (Exception e) {
            }
            setChanged();
            notifyObservers(gameMaze);
        });
    }

    @Override
    public void Solve() {
        threadPool.execute(() -> {
            try {
                new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                    @Override
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            gameMaze.setStartPosition(heroPosition);
                            toServer.writeObject(gameMaze);
                            toServer.flush();
                            mazeSolution = (Solution) fromServer.readObject();
                        } catch (Exception e) {
                        }
                    }
                }).communicateWithServer();
            } catch (Exception e) {
            }
            setChanged();
            notifyObservers(mazeSolution);

        });
    }

    @Override
    public void Save() {

    }

    @Override
    public void Load(String path) {

    }

    @Override
    public void moveCharacter(KeyCode moveCode) {
        switch(moveCode){
            case UP:
                if(checkMove(heroPosition.getRowIndex()-1, heroPosition.getColumnIndex())){
                    heroPosition = Position.getPosition(heroPosition.getRowIndex()-1, heroPosition.getColumnIndex());
                }
                break;
            case LEFT:
                if(checkMove(heroPosition.getRowIndex(), heroPosition.getColumnIndex()-1 )){
                    heroPosition = Position.getPosition(heroPosition.getRowIndex(), heroPosition.getColumnIndex()-1);
                }
                break;
            case DOWN:
                if(checkMove(heroPosition.getRowIndex()+1, heroPosition.getColumnIndex())){
                    heroPosition = Position.getPosition(heroPosition.getRowIndex()+1, heroPosition.getColumnIndex());
                }
                break;
            case RIGHT:
                if(checkMove(heroPosition.getRowIndex(), heroPosition.getColumnIndex()+1)){
                    heroPosition = Position.getPosition(heroPosition.getRowIndex(), heroPosition.getColumnIndex()+1);
                }
                break;
        }
        if(heroPosition.equals(gameMaze.getGoalPosition())){
            setChanged();
            notifyObservers("GameOver");
        }
        else{
            setChanged();
            notifyObservers(heroPosition);
        }
    }


    private boolean checkMove(int newRowIndex, int newColumnIndex) {
        if (gameMaze.checkIndexes(newRowIndex, newColumnIndex)) {
            int[][] mazeData = gameMaze.getData();
            if(mazeData[newRowIndex][newColumnIndex]==0){
                return true;
            }
        }
        return false;
    }
}
