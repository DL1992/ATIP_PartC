package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel m = new MyModel();
        m.startServers();
        MyViewModel vm = new MyViewModel(m);
        m.addObserver(vm);

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("MyView.fxml").openStream());
        MyViewController mv = loader.getController();
        mv.setViewModel(vm);
        vm.addObserver(mv);

        primaryStage.setTitle("Our AMAZING maze game (Show only Version patch 2.0.1)");
        Scene scene = new Scene(root, 875, 750, Color.web("CAEBF2"));
        scene.getStylesheets().add("./View/Design.css");
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mv.UpdateLayout();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                mv.UpdateLayout();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        mv.UpdateLayout();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
