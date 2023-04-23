package controller;

import domain.Message;
import domain.User;
import domain.exceptions.EntityIsNull;
import domain.exceptions.MyException;
import domain.exceptions.ValidatorException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class MessageController
{
    ///user ul curent, cel ce trimite mesajul
    private User user_curent;

    ///user ul vizualizat, cel ce primeste mesajul
    private User user_vizualizat;

    ///lista de mesaje dintre cei doi utilizatori
    private ObservableList<Message> mesaje_users = FXCollections.observableArrayList();

    ///controller ce ne permite sa avem acces la date(din service uri)
    private DataController controller;

    ///fereastra asociata trimiterii mesajelor
    private Stage stage;

    ///in functie de numarul de mesaje, grid pane ul se va modifica(are nevoie de atatea linii cate mesaje sunt)
    @FXML
    GridPane panou_mesaje;

    @FXML
    Button type_btn;

    @FXML
    Button see_messages;

    @FXML
    TextField type_message;


    /**
     * Setam user ul curent(cel ce navigheaza prin aplicatie)
     * @param user_curent-User
     */
    public void setUser_curent(User user_curent) {
        this.user_curent = user_curent;
    }

    /**
     * Setam user ul vizualizat(cel caruia dorim sa ii trimitem mesaje)
     * @param user_vizualizat-User
     */
    public void setUser_vizualizat(User user_vizualizat) {
        this.user_vizualizat = user_vizualizat;
        ///dupa ce setam user ul vizualizat(inainte am setat user ul curent, asa e ordinea in
        // HomeCoontroller), putem stabili mesajele dintre ei
        load_data();
    }

    /**
     * Setam controller ul prin care avem acces la date
     * @param controller-DataController
     */
    public void setController(DataController controller) {
        this.controller = controller;
    }

    /**
     * setam fereastra(unde trimitem mesaje)
     * @param stage-Stage
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public void load_data()
    {
        ///Ordinea conteaza!!! E bine sa stim care e user ul curent si care e cel vizaulizat, ca sa stim
        ///cum pozitionam mesajele
        List<Message> mesaje_useri = this.controller.getMessages_service().
                findMessagesBtwUser(user_curent, user_vizualizat);
        this.mesaje_users.setAll(mesaje_useri);
    }

    public void see_Conversation()
    {
        int numberMessages = this.mesaje_users.size();
        ///pe coloana 0 vom avea mesajele trimise de el, si pe coloana 1 mesajele trimise de noi
        for(int i = 0; i < numberMessages; i++)
        {
            panou_mesaje.addRow(i + 1);///adaugam linia i(linia 0 exista deja, acolo vom pue optiune de send msg)
            VBox date_mesaj = new VBox();
            Label data = new Label(mesaje_users.get(i).getAn()+ ":" +
                    mesaje_users.get(i).getLuna() + ":" + mesaje_users.get(i).getZi() + " " +
                    mesaje_users.get(i).getOra() + ":" +
                    mesaje_users.get(i).getMinut());
            TextField text_mesaj = new TextField(mesaje_users.get(i).getContinut());
            text_mesaj.setDisable(true);
            if(mesaje_users.get(i).sentByUser(user_curent))///acest mesaj a fost trimis de user curemt
            {
                data.setAlignment(Pos.CENTER_RIGHT);
                data.setTextAlignment(TextAlignment.RIGHT);
                text_mesaj.setAlignment(Pos.CENTER_RIGHT);
                date_mesaj.getChildren().addAll(data, text_mesaj);
                panou_mesaje.add(date_mesaj, 1, i + 1);
            }
            else
            {
                data.setTextAlignment(TextAlignment.LEFT);
                text_mesaj.setAlignment(Pos.CENTER_LEFT);
                date_mesaj.getChildren().addAll(data, text_mesaj);
                panou_mesaje.add(date_mesaj, 0, i + 1);
            }
        }
    }

    public void scrieMesaj()
    {
        try
        {
            see_messages.setDisable(false);///Scriem din nou u
            // n mesaj, DECI IAR putem apasa butonul
            ///vrem sa trimitem mesajul SI am scris ceva
            if(!Objects.equals(type_message.getText(), ""))
            {
                controller.getMessages_service().
                        add_message(user_curent.getID(), user_vizualizat.getID(), type_message.getText());
                type_message.setText("Scrie mesajul aici...");
                load_data();
                see_Conversation();///ca sa vedem grid pane ul actual
            }
            ///stergem ultima linie din GridPane, unde scriam mesajul curent;
        }
        catch(MyException ex)
        {
            MessageBox.showErrorMessage(stage, ex.getMessage());
        }
    }

    @FXML
    public void initialize()
    {
      ;
    }
}
