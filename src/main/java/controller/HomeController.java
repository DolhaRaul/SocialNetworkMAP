package controller;

import domain.Prietenie;
import domain.PrietenieState;
import domain.User;
import domain.exceptions.EntityIsNull;
import domain.exceptions.EntityNotFound;
import domain.exceptions.MyException;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uifx.LogInApplication;
import utils.Constants;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class HomeController
{
    /**
     * cererile de prietenie ale utilizatorului(care s a logat), atat cele deja existente cat si cele pe care le a primit
     * (cele pe care el le a trimis nu apar)
     */
    private ObservableList<Prietenie> cereriList = FXCollections.observableArrayList();

    /**
     * lista de utilizatori din retea
     */
    private ObservableList<User> useriList = FXCollections.observableArrayList();

    /**
     * cu asta avem acces la toate service urile, prin gett eri
     */
    private DataController controller;

    private Stage stage;

    /**
     * User ul, fereastra acestuia
     */
    private User user;

    /**
     * Componente Tab Persoane
     */
    @FXML
    TableView<User> usersTable;

    @FXML
    TableColumn<User, String> nume_column;

    @FXML
    TableColumn<User, String> prenume_column;

    @FXML
    TableColumn<User, Button> vizualizare_column;

    @FXML
    TableColumn<User, Button> mesaj_column;

    @FXML
    Button searchBtn;

    @FXML
    ToggleButton prieteniToggle;

    @FXML
    TextField searchField;

    /**
     * Componente Tab Cereri
     */

    @FXML
    TableColumn<Prietenie, String> numeColumn;

    @FXML
    TableColumn<Prietenie, String> prenumeColumn;

    @FXML
    TableColumn<Prietenie, String> momentColumn;

    @FXML
    TableColumn<Prietenie, String> statusColumn;

    @FXML
    TableColumn<Prietenie, Button> acceptedColumn;

    @FXML
    TableColumn<Prietenie, Button> rejectedColumn;

    @FXML
    TableView<Prietenie> cereriTable;


    public void setController(DataController controller) {
        this.controller = controller;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    /**
     *
     * @param user-in momentul in care incarcam fereastra pentru user ul logat, LA CREAREA(INSTANTIEREA) ACCESTUI CONTROLLER
     * din clasa LoInController(din functia HomeScene), aici setam user ul ca data membra(cu care vom "naviga" prin retea)
     * (SI TOT AICI INCARCAM SI DATELE FIINDCA ACEST SETT ER SE VA APELA INAINTE DE METODA INITIALIZE)!!!
     */
    public void setUser(User user) {
        this.user = user;
        load_data();
    }

    private void onVizualizare(User userVizualizat) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("profile.fxml"));
        //cream scena(care va fi de fapt una cu fereastra)
        Scene profileScene = new Scene(fxmlLoader.load());

        ProfileController controller = fxmlLoader.getController();
        //evident, lucram cu acelasi DataController(unde sunt datele modificate recent)
        controller.setController(this.controller);
        /*cand vizualizam pagina de profil a cuiva, VREM sa o vizualizam intr o noua fereastra(ca sa o putem
        inchide si sa ne intoarcem inapoi), pentru ca daca e in aceeasi fereastra, nu ne mai putem intoarce
        inapoi dupa ce am vizualizat profilul*/
        Stage stage = new Stage();
        controller.setStage(stage);
        controller.setUserVizualizat(userVizualizat);
        controller.setUserCurent(user);

        stage.setTitle(userVizualizat.getNume()+"'s Profile");
        stage.setScene(profileScene);
        stage.show();
    }

    private void onSendingMessage(User userVizualizat) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("mesaj.fxml"));
        ///incarcam continutul paginii
        Scene scene = new Scene(fxmlLoader.load());
        MessageController controller = fxmlLoader.getController();///fiecare pagina "html" are un controller asoc
        Stage stage = new Stage();
        controller.setStage(stage);///fereastra in care trimitem msaje
        stage.setTitle("Trimite mesaj lui " + userVizualizat.getNume() + " " + userVizualizat.getPrenume() + "!");
        stage.setScene(scene);
        controller.setController(this.controller);
        controller.setUser_curent(this.user);
        controller.setUser_vizualizat(userVizualizat);
        stage.show();
    }

    private void initialize_users()
    {
        usersTable.setItems(useriList);
        /**
         * Aceste metode ne spun cum cum se vor afisa datelle in fiecare coloana(si implicit cum se vor salva)
         */
        nume_column.setCellValueFactory(param -> {
            return new ObservableValueBase<String>()
            {
                @Override
                public String getValue()
                {
                    return param.getValue().getNume();
                }
            };
        });
        prenume_column.setCellValueFactory(param->{
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return param.getValue().getPrenume();
                }
            };
        });
        vizualizare_column.setCellValueFactory(param->{
            return new ObservableValueBase<Button>() {
                @Override
                public Button getValue() {
                    Button rez = new Button("Vizualizare");
                    rez.setStyle("-fx-font-family: Verdana; -fx-font-size: 14; /*-fx-background-color: rgba(43,210,157,0.5)*/");
                    rez.setBackground(new Background(new BackgroundFill(new Color(Math.random(), Math.random(),
                            Math.random(), Math.random()), CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
                    rez.setStyle("border: '10px solid'");
                    rez.setOnAction(ev->{
                        try {
                            ///vizualizam profilul user ului pentru care am apasat butonul de vizualizare
                            onVizualizare(param.getValue());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    return rez;
                }
            };
        });
        ////NU UITA SA SPECIFICI ACTIUNEA CE ARE LOC CAND SE TRIMITE UN MESAJ!!!
        mesaj_column.setCellValueFactory(param -> {
            return new ObservableValueBase<Button>() {
                @Override
                public Button getValue() {
                    Button send_msg = new Button("Send message");
                    send_msg.setStyle("-fx-border-style: outset; -fx-font-family: Verdana; -fx-font-size: 14");
                    send_msg.setStyle("border:10 px solid black");
                    send_msg.setOnAction(ev -> {
                        try{
                            ///pe fiecare linie din TableView, AVEM FE DAPT USERII!!! Acestia se obtin prin
                            ///metoda param.getValue() (pentru linia respectiva); prin metodele setCelValue...
                            ///specificam de fapt cum vor fi "prezentati, afisati" acesti useri
                            onSendingMessage(param.getValue());///
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    });
                    return send_msg;
                }
            };
        });
    }

    private void initialize_cereri()
    {
        cereriTable.setItems(cereriList);
        numeColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return controller.getFriends_service().findOther(param.getValue(), user).getNume();
                }
            };
        });
        prenumeColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return controller.getFriends_service().findOther(param.getValue(), user).getPrenume();
                }
            };
        });
        momentColumn.setCellValueFactory(param->{
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return param.getValue().getDate().format(Constants.DATE_TIME_FORMATTER);
                }
            };
        });
        statusColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return param.getValue().getState().toString();
                }
            };
        });
        acceptedColumn.setCellValueFactory(param->{
            return new ObservableValueBase<Button>() {
                @Override
                public Button getValue() {
                    if(param.getValue().getState() != PrietenieState.Pending)
                        return null;
                    Button rez = new Button("Accept");
                    rez.setStyle("-fx-font-family: Verdana; -fx-font-size: 12; -fx-background-color: rgba(52,243,6,0.7)");
                    rez.setOnAction(ev->{
                        try
                        {
                            onCerereAccept(param.getValue());
                        } catch (EntityNotFound e) {
                           MessageBox.showErrorMessage(stage, e.getMessage());
                        }
                    });
                    return rez;
                }
            };
        });
        rejectedColumn.setCellValueFactory(param->{
            return new ObservableValueBase<Button>() {
                @Override
                public Button getValue()
                {
                    if(param.getValue().getState() != PrietenieState.Pending)
                        return null;
                    Button rez = new Button("Reject");
                    rez.setStyle("-fx-font-family: Verdana; -fx-font-size: 12; -fx-background-color: rgba(243,6,6,0.7)");
                    rez.setOnAction(ev->{
                        try {
                            onCerereReject(param.getValue());
                        }
                        catch(MyException e)
                        {
                            MessageBox.showErrorMessage(stage, e.getMessage());
                        }
                        catch(SQLException e)
                        {
                            e.printStackTrace();
                        }
                    });
                    return rez;
                }
            };
        });

    }

    private void onCerereReject(Prietenie value) throws EntityIsNull, EntityNotFound, SQLException {
        controller.getFriends_service().delete_prietenie(value.getID());
        load_data();
    }

    private void onCerereAccept(Prietenie value) throws EntityNotFound
    {
        this.controller.getFriends_service().update_prietenie(value.getID(), PrietenieState.Accepted);
        load_data();
    }

    @FXML
    public void initialize()
    {
        initialize_users();
        initialize_cereri();
    }
    public void load_data()
    {
        List<User> lista_useri = StreamSupport.stream(controller.getUsers_service().findAll().spliterator(), false).toList();
        /*cu ajutorul lista_cereri, in TableView ul din TabPan ul Cereri vom afisa detaliile despre pirtteniile dintre user ul
        curent si prieteniile acestuia(PENDING SI ACCEPTED):Nume,Prenume, Moment, Status, etc
        */
        List<Prietenie> lista_cereri = Utils.toStream(controller.getFriends_service().findCereri(user)).toList();
        useriList.setAll(lista_useri);
        cereriList.setAll(lista_cereri);
    }

    /**
     * aceasta functie de filtrare o adaugam actiunii OnKeyReleased(si pentru OnAction) pentru acest TextField,
     * dar am adaugat o in SceneBuilder(n am mai adaugat o aici in cod)
     */
    public void SearchChanged()
    {
        String searchtext = searchField.getText();
        List<User> shown;
        ///filtram din userii din retea doar aceia care sunt prieteni(cu user ul curent, cel care s a logat
        if(prieteniToggle.isSelected())
        {
            prieteniToggle.setStyle("-fx-background-color: rgba(118,220,56,0.5)");
            shown = controller.getFriends_service().findPrieteni(user);
        }
        else
        {
            prieteniToggle.setStyle("-fx-background-color: default; -fx-border-color: black; -fx-border-with:2px");
            shown = Utils.toStream(controller.getUsers_service().findAll()).toList();
        }
        /*aici filtram userii(toti din retea sau doar cei care sunt prieteni cu user ul curent, depinde de starea
        ToggleButton ului) in functie de text ul din TextField
        */
        shown = shown.stream().filter(utilizator->utilizator.getNume().startsWith(searchtext)).toList();
        /*
        fiindca e de tipul ObservableList<User>, implicit si tabelul ce il intializam cu aceste valori din lista
        isi va schimba continutul(fiindca e un fel de Observer pentru aceasta lista)
         */
        useriList.setAll(shown);
    }
}
