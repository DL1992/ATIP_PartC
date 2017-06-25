package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is the model in the mvvm architecture.
 * the purpose of the model is to be the "brain" of the game. holding all the algorithms used for it.
 * this model is using 2 servers to generate and solve the mazes.
 * it should be observed by a some sort of a ViewModel class.
 *
 * @author Vladislav Sergienko
 * @author Doron Laadan
 */
public class MyModel extends Observable implements IModel {

    private Maze gameMaze;
    private Position heroPosition;
    private Solution mazeSolution;
    private Server mazeGenerationServer;
    private Server mazeSolvingServer;
    private ExecutorService threadPool;

    @Override
    public Maze getMaze() {
        return gameMaze;
    }

    @Override
    public Position getCurrentPosition() {
        return heroPosition;
    }

    @Override
    public Solution getSolution() {
        return mazeSolution;
    }

    /**
     * This function should start up the 2 servers used by this model.
     * one server is for generating maze and the other is to solve the maze.
     * its also open the threadPool used for the servers.
     */
    public void startServers() {
        this.threadPool = Executors.newFixedThreadPool(15);
        mazeGenerationServer = new Server(5400, 2000, new ServerStrategyGenerateMaze());
        mazeSolvingServer = new Server(5401, 2000, new ServerStrategySolveSearchProblem());
        mazeGenerationServer.start();
        mazeSolvingServer.start();
    }

    /**
     * This function is in charge of closing the servers.
     * it should be called once the program is closing for a proper exit.
     */
    public void stopServers() {
        mazeGenerationServer.stop();
        mazeSolvingServer.stop();
    }

    @Override
    /**
     * to create the maze we used a maze generation server and a client strategy which save the generated maze
     * and the hero position to the proper field in the model.
     * it notify the observers that a maze benn created.
     */
    public void Create(int rowSize, int columnSize) {
        if (rowSize <= 0 || columnSize <= 0 || rowSize == 1 && columnSize == 1) {
            setChanged();
            notifyObservers("BadSizes");
        } else {
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
    /**
     * to solve the maze we used a maze solving server and a client strategy which save solution
     * to the proper field in the model.
     * it notify the observers that a solution benn created.
     */
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
    /**
     * this save is done by using the MyCompressorOutputStream class.
     */
    public void Save(File file) {
        try {
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(file));
            gameMaze.setStartPosition(heroPosition);
            out.write(gameMaze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /**
     * this load is done by using the MyDecompressorInputStream class.
     * it notify the observers that the maze has benn loaded.
     */
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
        gameMaze = new Maze(savedMazeBytes);
        heroPosition = gameMaze.getStartPosition();
        setChanged();
        notifyObservers("Maze");
    }

    @Override
    public void moveCharacter(KeyCode moveCode) {
        switch (moveCode) {
            case UP:
                if (checkMove(heroPosition.getRowIndex() - 1, heroPosition.getColumnIndex())) {
                    heroPosition = Position.getPosition(heroPosition.getRowIndex() - 1, heroPosition.getColumnIndex());
                }
                break;
            case LEFT:
                if (checkMove(heroPosition.getRowIndex(), heroPosition.getColumnIndex() - 1)) {
                    heroPosition = Position.getPosition(heroPosition.getRowIndex(), heroPosition.getColumnIndex() - 1);
                }
                break;
            case DOWN:
                if (checkMove(heroPosition.getRowIndex() + 1, heroPosition.getColumnIndex())) {
                    heroPosition = Position.getPosition(heroPosition.getRowIndex() + 1, heroPosition.getColumnIndex());
                }
                break;
            case RIGHT:
                if (checkMove(heroPosition.getRowIndex(), heroPosition.getColumnIndex() + 1)) {
                    heroPosition = Position.getPosition(heroPosition.getRowIndex(), heroPosition.getColumnIndex() + 1);
                }
                break;
        }
        if (heroPosition.equals(gameMaze.getGoalPosition())) {
            setChanged();
            notifyObservers("GameOver");
        } else {
            setChanged();
            notifyObservers("HeroPosition");
        }
    }

    @Override
    public void closeProgram() {
        stopServers();
        threadPool.shutdown();
        setChanged();
        notifyObservers("ShutDown");
    }

    @Override
    public void reset() {
        gameMaze = null;
        heroPosition = null;
        mazeSolution = null;
    }

    /**
     * This function check if a specific move in the maze is legal.
     * this is a helper function for the move Character function.
     *
     * @param newRowIndex    the new row index of the hero.
     * @param newColumnIndex the new col index of the hero
     * @return true if the move is legal or false otherwise.
     */
    private boolean checkMove(int newRowIndex, int newColumnIndex) {
        if (gameMaze.checkIndexes(newRowIndex, newColumnIndex)) {
            int[][] mazeData = gameMaze.getData();
            if (mazeData[newRowIndex][newColumnIndex] == 0) {
                return true;
            }
        }
        return false;
    }


}
