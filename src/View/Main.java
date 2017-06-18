package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        final MyModel m = new MyModel();
//        m.startServers();
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("MyViewController.fxml"));
//        loader.setControllerFactory(new Callback<Class<?>, Object>() {
//            @Override
//            public Object call(Class<?> aClass) {
//                return new MyViewModel(m);
//            }
//        });
//
//        m.addObserver( loader.getController() );
//
//        Parent root = (Parent) loader.load();;
//        primaryStage.setTitle("Our AMAZING maze game (firework version patch 1.0.1)");
//        Scene scene = new Scene(root, 850, 750, Color.web("CAEBF2"));
//        scene.getStylesheets().add("./View/Design.css");
//        primaryStage.setScene(scene);
//        primaryStage.show();

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
        primaryStage.setScene(scene);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
