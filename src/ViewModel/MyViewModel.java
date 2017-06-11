package ViewModel;

import Model.IModel;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    IModel observedModel;


    @Override
    public void update(Observable o, Object arg) {
    }
}
