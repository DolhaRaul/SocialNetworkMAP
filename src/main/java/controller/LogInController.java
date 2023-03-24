package controller;

import domain.User;
import domain.exceptions.MyException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uifx.LogInApplication;

import java.io.IOException;

public class LogInController {

    private DataController controller;

    private Stage stage;
    @FXML
    private Label nume;

    @FXML
    private Label prenume;

    @FXML
    private TextField enter_nume;

    @FXML
    private TextField enter_prenume;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setController(DataController controller)
    {
        this.controller = controller;
    }

    private void showHome(User user) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("home.fxml"));
        Scene homeScene = new Scene(fxmlLoader.load());

        /**
         * Dupa ce Login ul se va executa cu succes, dupa accea vom lucra in aceasta fereastra
         */
        HomeController controller = fxmlLoader.getController();
        controller.setController(new DataController());
        controller.setStage(stage);
        controller.setUser(user);

        stage.setTitle("Home!");
        stage.setScene(homeScene);
        stage.show();
    }

    /**
     * Functia aceasta de LogIn am aduagat o butonului de LogIn, dar in SceneBuilder am facut asta(nu din cod),
     * observam ca aici nici nu l am mai aduagat ca data membra
     * @throws MyException
     */
    public void Login() throws MyException {
       String nume = enter_nume.getText();
       String prenume = enter_prenume.getText();
       try {
           ///daca user ul e null(nu e gasit), se arunca o exceptie
           User gasit = controller.getUsers_service().login(nume, prenume);
           //MessageBox.showSuccesMessage(stage, "SUCCES!");
           //afisam fereastra utilizatorului gasit, cel care s a logat
           showHome(gasit);
       }
       catch(MyException ex) {
           MessageBox.showErrorMessage(stage, ex.getMessage());
       } catch (IOException e) {
            e.printStackTrace();
       }
    }
}