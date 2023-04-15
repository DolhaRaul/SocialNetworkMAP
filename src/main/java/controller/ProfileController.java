package controller;

import domain.Prietenie;
import domain.PrietenieState;
import domain.User;
import domain.exceptions.EntityIsNull;
import domain.exceptions.EntityNotFound;
import domain.exceptions.MyException;
import domain.exceptions.ValidatorException;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ProfileController
{
    /**
     * prietenii(care sunt useri) utilizatorului pe care il vizualizam
     */
    private ObservableList<User> prieteniVizualizati = FXCollections.observableArrayList();

    private Stage stage;

    private DataController controller;

    /**
     * userul pentru care am deschis fereastra
     */
    private User userVizualizat;
    /**
     * userul curent, cel care a deschis fereastra aceasta fereastra "Profile", fereastra pentru userVizualizat
     */
    private User userCurent;

    /**
     * Ca sa stiu cum este prietenia intre userCurent si userVizualizat:Acceptata, in Curs sau Rejected
     */
    private Prietenie prietenie;

    private void getPrietenie(){
        if (controller == null)
            return;
        if(userCurent == null)
            return ;
        if(userVizualizat == null)
            return ;
        /**
         * Steam prietenii pentru user ul vizualizat si prietenie(daca exista sau nu) intre user ul curent, cel logat
         * si el vizualizat
         */
        prieteniVizualizati.setAll(controller.getFriends_service().findPrieteni(userVizualizat));
        prietenie = controller.getFriends_service().findByUsers(userCurent, userVizualizat);
        updateButton();
    }

    /**
     * numele user ului vizualizat
     */
    @FXML
    Label nume;

    /**
     * prenumele useru ului vizualizat
     */
    @FXML
    Label prenume;

    /**
     * oras ul user ului vizualizat
     */
    @FXML
    Label oras;

    @FXML
    TableColumn<User, String> numeColumn;

    @FXML
    TableColumn<User, String> prenumeColumn;

    @FXML
    TableColumn<User, String> orasColumn;

    @FXML
    TableView<User> tablePrieteni;

    @FXML
    Button sendBtn;

    @FXML
    Button removeBtn;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setController(DataController controller)
    {
        this.controller = controller;
        /*
        Dupa ce initializam controller ul(ca sa avem acces la datele din Service, EVIDENT) apelam metoda
        getPrietenie()
         */
        getPrietenie();
    }

    public void setUserVizualizat(User userVizualizat)
    {
        this.userVizualizat = userVizualizat;
        nume.setText(userVizualizat.getNume());
        prenume.setText(userVizualizat.getPrenume());
        oras.setText(userVizualizat.getOras());
        /*
        getPrietenie() o punem la fiecare setter fiindca nu stim care se va apela primul, astfel sa se intializeze
        listele de prieteni cu null si sa dea eroare
         */
        getPrietenie();
    }

    public void setUserCurent(User userCurent) {
        this.userCurent = userCurent;
        //analog
        getPrietenie();
    }

    @FXML
    public void initialize()
    {
        /*setam cum se vor afisam numele utilizatorilor(prietenii pentru userVizualizat) in
        coloana cu nume
        */
        tablePrieteni.setItems(prieteniVizualizati);
        numeColumn.setCellValueFactory(param->{
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return param.getValue().getNume();
                }
            };
        });
        prenumeColumn.setCellValueFactory(param->{
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return param.getValue().getPrenume();
                }
            };
        });
        orasColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return param.getValue().getOras();
                }
            };
        });
    }

    private void setButonState(Button buton, boolean isEnabled)
    {
        buton.setVisible(isEnabled);
        buton.setDisable(!isEnabled);
    }

    public void updateButton()
    {
        if(prietenie == null)
        {
            setButonState(removeBtn, false);
            setButonState(sendBtn, true);
            sendBtn.setText("Trimite cerere!");
        }
        else if (prietenie.getState() == PrietenieState.Pending)
        {
            /*eu sunt cel care a trimis aceasta cerere de prietenie, acestui userVizualizat caruia
            i am accesat profilul
             */
            if(prietenie.getSenderId() == userCurent.getID())
            {;
                sendBtn.setText("Cererea trimisa!");
                /*
                Daca am trimis deja cererea, vrem sa NU O PUTEM TRIMITE DE MAIM ULTE ORI;
                EA A FOST DEJA TRIMISA
                 */
                sendBtn.setDisable(true);
                sendBtn.setVisible(true);

                removeBtn.setText("Anuleaza cererea!");
                setButonState(removeBtn, true);
             }
            /*
            Suntem in cel de al doilea caz, cand eu sunt de fpat cel care a primit aceasta cerere
            de prietenie
             */
            else
            {
                sendBtn.setText("Cerere primita!");
                /*
                Daca am trimis deja cererea, vrem sa NU O PUTEM TRIMITE DE MAIM ULTE ORI;
                EA A FOST DEJA TRIMISA
                 */
                sendBtn.setDisable(true);
                sendBtn.setVisible(true);
                setButonState(removeBtn, true);
            }
        }
        else if (prietenie.getState() == PrietenieState.Accepted)
        {
            sendBtn.setText("✓ Prieteni");
            sendBtn.setDisable(true);
            sendBtn.setVisible(true);

            removeBtn.setText("Sterge prietenia!");
            setButonState(removeBtn, true);
        }
    }

    public void sendRequest() {
        try
        {
            prietenie = controller.getFriends_service().sendRequest(userCurent, userVizualizat);
            updateButton();
        }
        catch(MyException ex)
        {
            MessageBox.showErrorMessage(stage, ex.getMessage());
        }
    }
    public void prietenieDelete()
    {
        try {
            controller.getFriends_service().delete_prietenie(prietenie.getID());
            prietenie = null;
            updateButton();
            prieteniVizualizati.setAll(controller.getFriends_service().findPrieteni(userVizualizat));
        }
        catch (MyException ex)
        {
            MessageBox.showErrorMessage(stage, ex.getMessage());
        }
    }
}
