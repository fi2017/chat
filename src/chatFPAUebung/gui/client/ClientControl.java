package chatFPAUebung.gui.client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.activation.ActivationGroup_Stub;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;
import chatFPAUebung.klassen.Chatroom;
import chatFPAUebung.klassen.Nachricht;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.threads.ClientReadingThread;
import chatFPAUebung.threads.ClientWritingThread;
import javafx.animation.*;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.css.Rect;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientControl implements Initializable
{
    public Button btnFriends;
    public Button btnRoom;
    public Button btnNewRoom;
    public Button btnSettings;
    public AnchorPane menuBar;
    public Pane paneActive;
    public VBox friendList;
    public TextField searchFieldFriend;
    public TextField searchFieldRoom;
    public VBox roomList;
    public Button btnMin;
    public Button btnClose;
    public Pane paneSettings;
    public Button btnSettingsReturn;
    public ImageView returnArrow;
    public VBox addRoom;
    public TextField txtFieldRoomName;
    public PasswordField txtFieldRoomPw;
    public ImageView addRoomBackground;
    public Pane paneAddRoomPw;
    public Pane paneAddRoomName;
    public Label labelAddRoomPw;
    public Label labelAddRoomName;
    public Button btnAddRoomCreate;
    public VBox vBoxRoom;
    public Button btnSearchFriend;
    public Button btnSearchRoom;
    public AnchorPane paneChat;
    public TextField txtFieldChat;
    public ScrollPane scrollPaneChat;


    private double xOffset;
    private double yOffset;
    private DefaultListModel<Nachricht> listModel;
    private Socket clientSocket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private ClientReadingThread clientReadingThread;
    private ArrayList<Chatroom> chatrooms = new ArrayList<Chatroom>();
    private ArrayList<DefaultListModel> listmodels = new ArrayList<DefaultListModel>();


    int i = 0;

    //TODO:
    //      - Allgemein Nachrichten versenden
    //      - Suchfunktion der für friendList & roomList
    //      - Nachrichten einfügen, einmal nachricht bekommen andermal nachricht gesenden (links - rechts && Farbgestaltung)
    //      - Evtl. Audiofiles einbauen -> Audio Klasse?
    //      - Bilder versenden!
    //      - Settingsmenü noch nicht vorhanden (Marcel)
    //              - Themes
    //              - Language
    //              - Username & Password ändern
    //              - Log out
    //              - Mute

    //Anlegen der events der einzelnen Komponenten der GUI
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //Anwendung in "startform" geben
        vBoxRoom.setSpacing(5);
        this.listModel = new DefaultListModel<Nachricht>();
        listmodels.add(listModel);
        toggleNewRoom(1200, false);
        erstelleVerbindung();
        hideLists();

        //Menüleiste Buttons
        btnFriends.setOnAction(e -> {
            moveActivePane(btnFriends);
            switchList(friendList, roomList);
            toggleNewRoom(1200, false);
            movePaneChat(350);
        });

        btnRoom.setOnAction(e -> {
            moveActivePane(btnRoom);
            switchList(roomList, friendList);
            toggleNewRoom(1200, false);
            movePaneChat(350);
        });

        btnNewRoom.setOnAction(e -> {
            moveActivePane(btnNewRoom);
            toggleList(-friendList.getWidth(), friendList);
            toggleList(-roomList.getWidth(), roomList);
            addRoomBackground.setVisible(true);
            addRoom.setVisible(true);
            toggleNewRoom(850, true);
            movePaneChat(0);
        });

        btnSettings.setOnAction(e -> {
            paneSettings.setVisible(true);
            toggleSettings(0.85, true);
        });

        //Fenster draggable machen
        menuBar.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        menuBar.setOnMouseDragged(e -> {
            menuBar.getScene().getWindow().setX(e.getScreenX() - xOffset);
            menuBar.getScene().getWindow().setY(e.getScreenY() - yOffset);
        });

        //Menüleisten fürs Schließen und minimieren
        btnClose.setOnAction(e -> ((Stage)btnClose.getScene().getWindow()).close());

        btnMin.setOnAction(e -> ((Stage)btnMin.getScene().getWindow()).setIconified(true));
        //Settingsmenü wieder zurück in die Hauptanwendung
        btnSettingsReturn.setOnMouseEntered(e -> rotateArrow(returnArrow.getRotate() + 360));
        btnSettingsReturn.setOnAction(e -> toggleSettings(0, false));

        //AddRoom-Sidebar(rechts) -> neuen Raum erstellen
        btnAddRoomCreate.setOnAction(e -> erstelleChatroom(txtFieldRoomName.getText(), 15, txtFieldRoomPw.getText()));
        txtFieldRoomName.setOnAction(e -> erstelleChatroom(txtFieldRoomName.getText(), 15, txtFieldRoomPw.getText()));
        txtFieldRoomPw.setOnAction(e -> erstelleChatroom(txtFieldRoomName.getText(), 15, txtFieldRoomPw.getText()));

        //Im Chatroom drinnen (Beim anzeigen, der Nachrichten)
        //TODO: Evtl. Schauen welcher Chat grade geöffnet ist, und dementsprechend die friendlist bzw. die roomlist öffnen. (Generics mit Wildcards)
        paneChat.setOnMouseClicked(e -> {
            if(addRoom.isVisible())
            {
                toggleNewRoom(1200, false);
                switchList(roomList, friendList);
                moveActivePane(btnRoom);
                movePaneChat(350.0);
            }
        });

        txtFieldChat.setOnAction(e -> {

            //TODO:
            // Aktuell nur Temporär, der Funktionsaufruf muss dann ins Protokoll kommen, wo man eine Nachricht erhält.
            for(Chatroom c : chatrooms)
            {
                if(c.getScrollPane().isVisible())
                {
                    i++;
                    if(i % 2 == 0)
                        createSentMessage(txtFieldChat.getText());
                    else
                        createRecievedMessage(txtFieldChat.getText());
                }
            }
            txtFieldChat.setText("");
        });
    }

    //Erstellt Verbindung zum Server
    public void erstelleVerbindung()
    {
        try
        {
            setClientSocket(new Socket("localhost", 8008));
            setOutToServer(new ObjectOutputStream(getClientSocket().getOutputStream()));
            setInFromServer(new ObjectInputStream(getClientSocket().getInputStream()));

            setClientReadingThread(new ClientReadingThread(this));
            getClientReadingThread().start();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        sendeNachrichtAnServer(new Uebertragung(1, 0,null));
    }

    //Protokoll für das Empfangen von Nachrichten
    public void empfangeNachrichtVonServer(Object uebertragungObjekt)
    {
        if(uebertragungObjekt instanceof Uebertragung)
        {
            Uebertragung uebertragung = (Uebertragung) uebertragungObjekt;

            switch(uebertragung.getZweck())
            {
                //Empfangen von allen bisherigen Nachrichten
                //TODO: Kann es sein, dass das hier nicht so viel Sinn macht?
                // Sollte man nicht eher hier nur eine Datei deserialisieren, da man ja anders eh keinen Chatverlauf hat.
                // Man muss ja iwann mal was geschrieben haben, wenn man aber dann die Anwendung schließt, ist alles ja wieder weg.
                // Und da wir noch keine User haben kann man auch schwer auf andere Chatrooms zugreifen
                case 1:
                    if(uebertragung.getUebertragung() instanceof Nachricht[])
                    {
                        DefaultListModel aktuellesModel=listmodels.get(((Uebertragung) uebertragungObjekt).getZiel());

                        if(((Nachricht[]) uebertragung.getUebertragung()).length != 0)
                        {
                            aktuellesModel.clear();

                            for(Nachricht aktNachricht : (Nachricht[]) uebertragung.getUebertragung())
                            {
                                aktuellesModel.addElement(aktNachricht);
                            }

                            //zeigeNeuesteNachricht();
                            //TODO:
                            // Ich muss hier noch wissen, von wem die Nachricht kommt.
                            // Bitte im Protokoll noch hinzufügen.
                            // Weiterhin: Was genau soll hier gemacht werden?
                            // Es sollen doch eh alle Nachrichten angezeigt werden, oder?
                            // Oder check ich's grade nicht?
                        }
                    }
                    break;

                //Empfangen von neuen Nachrichten
                case 2:
                    if(uebertragung.getUebertragung() instanceof Nachricht)
                    {
                        DefaultListModel aktuellesModel=listmodels.get(((Uebertragung) uebertragungObjekt).getZiel());
                        aktuellesModel.addElement(uebertragung.getUebertragung());

                        //TODO:
                        // Ich muss hier noch wissen, von wem die Nachricht kommt.
                        // Bitte im Protokoll noch hinzufügen.
                        /*if(((Nachricht) uebertragung.getUebertragung()).getSender() != user)
                        {
                            createRecievedMessage(((Nachricht) uebertragung.getUebertragung()).getNachricht());
                        }
                        else
                        {
                            createSentMessage(((Nachricht) uebertragung.getUebertragung()).getNachricht());
                        }*/
                    }

                    break;

                //Schließen der ListModels wenn Verbindung durchtrennt
                case 3:
                    sendeNachrichtAnServer(new Uebertragung(0,null));
                    break;

                //Hinzufügen eines neu erstellten Chatrooms in die ClientGui
                case 4:
                    chatrooms.add((Chatroom) uebertragung.getUebertragung());

                default:
                    //TODO: Hier bitte noch iwie den User bannen oder so, da er sich nicht an das Protokoll hält.
                    break;
            }
        }
    }

    //Sende Nachricht an einen Chatroom (Benötigen ID des aktuell benutzen Chatrooms)
    public void sendeNachrichtVonChatroom(Chatroom c) //Methode für ChatroomGUI
    {
        if(txtFieldChat.getText() != null)
        {
            ListModel model = c.getChatmodel();
            int ziel=c.getId();
            sendeNachrichtAnServer(new Uebertragung(2, ziel, new Nachricht(txtFieldChat.getText(), LocalDateTime.now())));
        }
    }


    //Chatrooms
    public void erstelleChatroom(String name, int teilnehmerAnzahl, String passwort)
    {
        Chatroom chat = new Chatroom(name, teilnehmerAnzahl);
        if(passwort != null)
        {
            chat.setPasswort(passwort);
            try
            {
                DefaultListModel neuesListModel = new DefaultListModel();
                chat.setChatmodel(neuesListModel);
                Uebertragung neuerchatroom = new Uebertragung(4, chat);
                listmodels.add(neuesListModel);
                outToServer.writeObject(neuerchatroom);
                outToServer.flush();
                chatrooms.add(chat);
                createRoom(chat);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //Listmodel wechseln in Gui
    //TODO: Bin mir nicht sicher, was ich hier machen soll :D bzw. wofür die Methode ist^^
    public void switchModel()
    {

    }

    //Chatrooms Ende

    public void sendeNachrichtAnServer(Uebertragung uebertragung)
    {
        (new ClientWritingThread(uebertragung, this)).run();
    }

    //Methode wird aufgerufen, wenn man auf den Button "Erstellen" einer neuen Gruppe drückt.
    //Sie erzeugt eine neue Gruppe in der Sidebar Gruppen.
    //Ein Neues Gruppenobjekt wird angelegt.
    private void createRoom(Chatroom c)
    {
        FileChooser fc = new FileChooser();
        File img = fc.showOpenDialog(friendList.getScene().getWindow());

        ImageView i = new ImageView("file:" + img.getAbsolutePath());
        i.setFitWidth(50);
        i.setFitHeight(50);

        Pane p = new Pane();
        p.setMinSize(350, 50);
        p.setPrefSize(350, 50);
        p.setMaxSize(350, 50);
        p.setVisible(true);

        Button b = new Button();
        b.setMinSize(350, 50);
        b.setPrefSize(350, 50);
        b.setMaxSize(350, 50);
        b.getStyleClass().add("Room");
        b.getStyleClass().add("Font");
        b.setAlignment(Pos.CENTER_LEFT);
        b.setText(c.getName());
        b.setLayoutX(50);
        b.setLayoutY(0);

        p.getChildren().add(i);
        p.getChildren().add(b);

        b.setOnMouseClicked(e -> {
            openChatroom(e.getSource());
        });

        c.setContainer(new VBox());
        c.getContainer().setLayoutX(0);
        c.getContainer().setLayoutY(0);
        if(getActiveChatroom() != null)
        {
            getActiveChatroom().getScrollPane().setVisible(false);
        }
        c.setScrollPane(new ScrollPane(c.getContainer()));
        c.getScrollPane().setVisible(true);
        c.getScrollPane().setPrefSize(850, 600);
        c.getScrollPane().setLayoutX(0);
        c.getScrollPane().setLayoutY(0);
        c.getScrollPane().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        c.getScrollPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        c.getScrollPane().getStyleClass().add("Remove-Border");

        //Muss man so machen, da der Inhalt der VBox, also dem "Container" immer neu gesetzt wird.
        c.getContainer().heightProperty().addListener(observable -> c.getScrollPane().setVvalue(1D));

        paneChat.getChildren().add(c.getScrollPane());
        vBoxRoom.getChildren().add(p);
        toggleNewRoom(1200, false);
        toggleList(0, roomList);
        txtFieldRoomPw.setText("");
        txtFieldRoomName.setText("");
        moveActivePane(btnRoom);
        movePaneChat(350);
    }

    //Chatraum wird anhand von der angeklickten Gruppe (oder des Freundes) erstellt.
    private void openChatroom(Object sender)
    {
        for(Chatroom c : chatrooms)
        {
            //Wüsste gerade nicht, wie ich die ID von dem Room, den ich angeklick habe bekommen sollte.
            if(c.getName().equals(((Button)sender).getText()))
            {
                c.getScrollPane().setVisible(true);
            }
            else
                c.getScrollPane().setVisible(false);
        }
    }

    //Die Methode erstellt eine Instanz einer erhaltenen Nachricht, die von einen Anderen Nutzer, nicht man selbst, versendet wurde
    private void createRecievedMessage(String msg) //Theoretisch brauche ich auch noch Sender, also der User und auch das Sendedatum + Zeit
    {
        Pane p = new Pane();
        //TODO: Auf User-Klasse warten...
        //        ImageView i = new ImageView("file:" + ((File)User.img).getAbsolutePath());
        //        i.setFitWidth(50);
        //        i.setFitHeight(50);
        //        i.setX(5);
        //        i.setY(5);

        Rectangle tmp = new Rectangle();
        tmp.setWidth(50);
        tmp.setHeight(50);
        tmp.setX(5);
        tmp.setY(5);
        tmp.setFill(Color.BLACK);
        tmp.getStyleClass().add("ImageDropShadow");

        Text t = new Text(msg);
        t.setWrappingWidth(250);
        t.setX(40);
        t.setY(40);
        t.setFill(Color.WHITE);

        Pane txtPane = new Pane();
        txtPane.setPrefWidth(300);
        txtPane.setLayoutX(25);
        txtPane.setLayoutY(25);
        txtPane.getStyleClass().add("MessageRecieved");
        txtPane.getStyleClass().add("DropShadow");
        txtPane.getChildren().add(t);
        txtPane.setPadding(new Insets(0, 0, 10, 0));

        p.getChildren().add(txtPane);
        p.getChildren().add(tmp);

        getActiveChatroom().getContainer().getChildren().add(p);
    }

    //Die Methode erstellt eine Instanz einer erhaltenen Nachricht, die von einen Anderen Nutzer, nicht man selbst, versendet wurde
    private void createSentMessage(String msg) //Theoretisch brauche ich auch noch Sender, also der User und auch das Sendedatum + Zeit
    {
        Pane p = new Pane();
        //TODO: Auf User-Klasse warten...
        //        ImageView i = new ImageView("file:" + ((File)User.img).getAbsolutePath());
        //        i.setFitWidth(50);
        //        i.setFitHeight(50);
        //        i.setX(5);
        //        i.setY(5);

        Rectangle tmp = new Rectangle();
        tmp.setWidth(50);
        tmp.setHeight(50);
        tmp.setX(795);
        tmp.setY(5);
        tmp.setFill(Color.BLACK);
        tmp.getStyleClass().add("ImageDropShadow");

        Text t = new Text(msg);
        t.setWrappingWidth(250);
        t.setX(15);
        t.setY(40);
        t.setFill(Color.BLACK);

        Pane txtPane = new Pane();
        txtPane.setPrefWidth(300);
        txtPane.setLayoutX(525);
        txtPane.setLayoutY(25);
        txtPane.getStyleClass().add("MessageSent");
        txtPane.getStyleClass().add("DropShadow");
        txtPane.getChildren().add(t);
        txtPane.setPadding(new Insets(0, 0, 15, 0));

        p.getChildren().add(txtPane);
        p.getChildren().add(tmp);

        getActiveChatroom().getContainer().getChildren().add(p);
    }

    //TODO: Noch nicht in gebrauch, da keine User Klasse vorhanden ist und die Freundesliste noch nicht erstellt ist.
    private void addFriend(String name, boolean status, String text, File img)
    {

        Pane p = new Pane();
        p.setMinSize(350, 50);
        p.setPrefSize(350, 50);
        p.setMaxSize(350, 50);
        p.setVisible(true);

        Button b = new Button();
        b.setMinSize(295, 50);
        b.setPrefSize(295, 50);
        b.setMaxSize(295, 50);
        b.getStyleClass().add("Room");
        b.getStyleClass().add("Font");
        b.setText(name);
        b.setLayoutX(50);
        b.setLayoutY(0);
        b.setTextAlignment(TextAlignment.LEFT);

        Rectangle online = new Rectangle();
        online.setHeight(50);
        online.setWidth(5);
        online.setLayoutX(345);
        online.setLayoutY(0);
        online.setFill(Color.GREEN);
        p.getChildren().add(b);
        p.getChildren().add(online);

    }

    public Chatroom getActiveChatroom()
    {
        Chatroom chatroom = null;

        for(Chatroom c : chatrooms)
        {
            if(c.getScrollPane() == null)
            {
                return null;
            }
            else
            {
                if(c.getScrollPane().isVisible())
                    chatroom = c;
            }
        }

        return chatroom;
    }

    //Animationen und Übergänge
    private void movePaneChat(double x)
    {
        Timeline t = new Timeline();
        t.getKeyFrames().add(new KeyFrame(Duration.millis(150),
                new KeyValue(paneChat.layoutXProperty(), x, Interpolator.EASE_BOTH)));
        t.play();
    }

    private void hideLists()
    {
        toggleList(-350, roomList);
        toggleList(0, friendList);
        roomList.setVisible(false);
        friendList.setVisible(true);
    }

    private void switchList(VBox show, VBox hide)
    {
        Timeline t = new Timeline();
        t.getKeyFrames().add(new KeyFrame(Duration.millis(150),
                new KeyValue((hide.layoutXProperty()), -hide.getWidth(), Interpolator.EASE_BOTH)));
        t.play();

        t.setOnFinished(e -> {
            hide.setVisible(false);
            show.setVisible(true);
            Timeline t2 = new Timeline();
            t2.getKeyFrames().add(new KeyFrame(Duration.millis(150),
                    new KeyValue((show.layoutXProperty()), 0, Interpolator.EASE_BOTH)));
            t2.play();
        });
    }

    private void toggleNewRoom(double x, boolean visible)
    {
        Timeline t = new Timeline();
        t.getKeyFrames().add(new KeyFrame(Duration.millis(150),
                new KeyValue(addRoom.layoutXProperty(), x, Interpolator.EASE_IN),
                new KeyValue(addRoomBackground.layoutXProperty(), x, Interpolator.EASE_IN)));
        t.play();

        t.setOnFinished(e -> {
            addRoom.setVisible(visible);
            addRoomBackground.setVisible(visible);
        });
    }

    private void toggleSettings(double opacity, boolean visible)
    {
        Timeline t = new Timeline();
        t.getKeyFrames().add(new KeyFrame(Duration.millis(300),
                new KeyValue(paneSettings.opacityProperty(), opacity, Interpolator.EASE_BOTH)));
        t.play();

        t.setOnFinished(e -> paneSettings.setVisible(visible));
    }

    private void moveActivePane(Button sender)
    {
        Timeline t = new Timeline();
        t.getKeyFrames().add(new KeyFrame(Duration.millis(150),
                new KeyValue(paneActive.prefWidthProperty(), sender.getWidth(), Interpolator.EASE_BOTH),
                new KeyValue(paneActive.layoutXProperty(), sender.getLayoutX(), Interpolator.EASE_BOTH)));
        t.play();
    }

    private void toggleList(double x, VBox list)
    {
        list.setVisible(true);

        Timeline t = new Timeline();
        t.getKeyFrames().add(new KeyFrame(Duration.millis(150),
                new KeyValue((list.layoutXProperty()), x, Interpolator.EASE_BOTH)));
        t.play();

        t.setOnFinished(e -> {
            if(list.getLayoutX() == -list.getWidth())
            {
                list.setVisible(false);
            }
        });
    }

    private void rotateArrow(double angle)
    {
        Timeline t = new Timeline();
        t.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                new KeyValue(returnArrow.rotateProperty(), angle, Interpolator.EASE_BOTH)));
        t.play();
    }
    // Getter
    public Socket getClientSocket()
    {
        return clientSocket;
    }

    public ObjectOutputStream getOutToServer()
    {
        return outToServer;
    }

    public ObjectInputStream getInFromServer()
    {
        return inFromServer;
    }

    public void setClientSocket(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    public void setOutToServer(ObjectOutputStream outToServer)
    {
        this.outToServer = outToServer;
    }

    public void setInFromServer(ObjectInputStream inFromServer)
    {
        this.inFromServer = inFromServer;
    }

    public ClientReadingThread getClientReadingThread()
    {
        return clientReadingThread;
    }

    public void setClientReadingThread(ClientReadingThread clientReadingThread)
    {
        this.clientReadingThread = clientReadingThread;
    }

    public DefaultListModel<Nachricht> getListModel()
    {
        return this.listModel;
    }

    public void setListModel(DefaultListModel aktuellesModel)
    {
        this.listModel=aktuellesModel;
    }

    public ArrayList<Chatroom> getChatrooms()
    {
        return chatrooms;
    }

    public void setChatrooms(ArrayList<Chatroom> chatrooms)
    {
        this.chatrooms = chatrooms;
    }

    public ArrayList<DefaultListModel> getListmodels()
    {
        return listmodels;
    }

    public void setListmodels(ArrayList<DefaultListModel> listmodels)
    {
        this.listmodels = listmodels;
    }
}
