package Model;

import Client.Client;
import IO.MyCompressorOutputStream;
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
    private ExecutorService threadPool;

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
        this.threadPool = Executors.newFixedThreadPool(15);
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
        if(rowSize <= 0 || columnSize <= 0 || rowSize == 1 && columnSize == 1){
            setChanged();
            notifyObservers("BadSizes");
        }
        else {
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
                                    byte[] decompressedMaze = new byte[Math.max(compressedMaze.length, 100)];
                                    is.read(decompressedMaze);
                                    gameMaze = new Maze(decompressedMaze);
                                    heroPosition = gameMaze.getStartPosition();
                                    toServer.writeObject(mazeDimensions);
                                } catch (Exception e) {
                                }
                            }
                        }).communicateWithServer();
                    } catch (Exception e) {
                    }
                    setChanged();
                    notifyObservers("Maze");
            });
        }
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
            notifyObservers("Solution");

        });
    }

    @Override
    public void Save(File file) {
        try {
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(file));
            gameMaze.setStartPosition(heroPosition);
            out.write(gameMaze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException var10) {
            var10.printStackTrace();
        }
    }

    @Override
    public void Load(File file) {
        byte[] savedMazeBytes = new byte[0];
        try {
            InputStream in = new MyDecompressorInputStream(new FileInputStream(file));
            savedMazeBytes = new byte[1000];
            in.read(savedMazeBytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameMaze =  new Maze(savedMazeBytes);
        heroPosition = gameMaze.getStartPosition();
        setChanged();
        notifyObservers("Maze");
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
            notifyObservers("HeroPosition");
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
