package View;

import ViewModel.MyViewModel;

/**
 * Created by sergayen on 6/18/2017.
 */
public interface IView {
    void setViewModel(MyViewModel vm);
    void UpdateLayout();
}