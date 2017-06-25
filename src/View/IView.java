package View;

import ViewModel.MyViewModel;

/**
 * This class is the Interface for the View layer fxml controller - as part of the MVVM architecture.
 * contains basic functions such as setting the observed View-Model and updating the layout.
 *
 * @author Vladislav Sergienko
 * @author Doron Laadan
 */
public interface IView {

    /**
     * Setter function for the View-Model that the View contains and observes
     *
     * @param myViewModel is the new View Model.
     */
    void setViewModel(MyViewModel myViewModel);

    /**
     * A general visual function that is used to resize the view
     *
     */
    void UpdateLayout();
}
