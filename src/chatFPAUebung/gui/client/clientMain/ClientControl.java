package chatFPAUebung.gui.client.clientMain;


import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.rmi.activation.ActivationGroup_Stub;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;

import chatFPAUebung.klassen.Chatroom;
import chatFPAUebung.klassen.Nachricht;
import chatFPAUebung.klassen.NachrichtBild;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.threads.ClientReadingThread;
import chatFPAUebung.threads.ClientWritingThread;
import feature_LoginRegister.User;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.css.Rect;

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
    public Button btnAttatchment;
    public AnchorPane paneBackground;

    private double xOffset;
    private double yOffset;
    private User user;
    private DefaultListModel<Nachricht> listModel;
    private Socket clientSocket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private ClientReadingThread clientReadingThread;
    private ArrayList<Chatroom> chatrooms = new ArrayList<>();
    private ArrayList<DefaultListModel> listmodels = new ArrayList<>();


    int i = 0;

    //TODO:
    //      - Allgemein Nachrichten versenden
    //      - Bilder versenden!
    //      - Settingsmenü noch nicht vorhanden (Marcel)
    //              - Themes
    //              - Language
    //              - Username & Password ändern
    //              - Mute

    //Anlegen der events der einzelnen Komponenten der GUI
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //Anwendung in "startform" geben
        vBoxRoom.setSpacing(5);
        this.listModel = new DefaultListModel<>();
        listmodels.add(listModel);
        toggleNewRoom(1200, false);
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
        btnClose.setOnAction(e -> {
        	try
        	{
        		sendeNachrichtAnServer(new Uebertragung(6,this.user));
        		Thread.currentThread().join();
        		Thread.currentThread().wait(100);
        	}
        	catch(Exception e1)
        	{
        		System.out.println("Kann keine Nachricht vor schliessen an Server schicken");
        	}
        	((Stage)btnClose.getScene().getWindow()).close();
        });

        btnMin.setOnAction(e -> ((Stage)btnMin.getScene().getWindow()).setIconified(true));
        //Settingsmenü wieder zurück in die Hauptanwendung
        btnSettingsReturn.setOnMouseEntered(e -> rotateArrow(returnArrow.getRotate() + 360));
        btnSettingsReturn.setOnAction(e -> toggleSettings(0, false));

        //AddRoom-Sidebar(rechts) -> neuen Raum erstellen
        btnAddRoomCreate.setOnAction(e -> erstelleChatroom(txtFieldRoomName.getText(), 15, txtFieldRoomPw.getText()));
        txtFieldRoomName.setOnAction(e -> erstelleChatroom(txtFieldRoomName.getText(), 15, txtFieldRoomPw.getText()));
        txtFieldRoomPw.setOnAction(e -> erstelleChatroom(txtFieldRoomName.getText(), 15, txtFieldRoomPw.getText()));

        //Chatroom hinzufügen
        btnSearchRoom.setOnAction(e -> {

        });

        //Im Chatroom drinnen (Beim anzeigen, der Nachrichten)
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

            sendeNachricht(getActiveChatroom().getId());
            txtFieldChat.setText("");
        });

        btnAttatchment.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("Image Files (*.png)", "*.png");
            FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("Image Files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter gif = new FileChooser.ExtensionFilter("Image Files (*.gif)", "*.gif");
            fc.getExtensionFilters().add(png);
            fc.getExtensionFilters().add(jpg);
            fc.getExtensionFilters().add(gif);
            File img = fc.showOpenDialog(friendList.getScene().getWindow());

            //TODO: Bild muss erst an den Server gesendet werden. -> Chatroom / Joshua muss sich drum kümmern.
            // theoretisch sowas wie:
            //          sendeNachrichtAnServer(new Uebertragung(6, getActiveChatroom(), new Nachricht(img, LocalDateTime.now()), user)); Oder whatever
            createSentImage(img, user);
            sendeNachrichtAnServer(new Uebertragung(2, i, new NachrichtBild(img), user));
        });

        //TODO: Temp;
        createTestUser();
    }

    private void createTestUser()
    {
        Platform.runLater(() -> {
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("Image Files (*.png)", "*.png");
            FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("Image Files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter gif = new FileChooser.ExtensionFilter("Image Files (*.gif)", "*.gif");
            fc.getExtensionFilters().add(png);
            fc.getExtensionFilters().add(jpg);
            fc.getExtensionFilters().add(gif);
            File img = fc.showOpenDialog(friendList.getScene().getWindow());
            user = new User("Peter", "123", 0, 0);
            user.setProfilbild(img);
        });

    }

    public void erstelleVerbindung(Socket clientSocket, ObjectInputStream is, ObjectOutputStream os)
    {
        try
        {
            setClientSocket(clientSocket);
            setOutToServer(os);
            setInFromServer(is);

            setClientReadingThread(new ClientReadingThread(this));
            getClientReadingThread().start();
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
                        }
                    }
                    break;

                //Empfangen von neuen Nachrichten
                case 2:
                    if(uebertragung.getUebertragung() instanceof NachrichtBild)
                    {
                        if(uebertragung.getSender() != user)
                        {
//                            createRecievedImage((NachrichtBild));
                        }
                    }else if(uebertragung.getUebertragung() instanceof Nachricht)
                    {
                        if(uebertragung.getSender() != user)
                        {
                            createRecievedMessage(((Nachricht) uebertragung.getUebertragung()).getNachricht(), uebertragung.getSender(), getChatroomById(uebertragung.getZiel()));
                        }
                        else
                        {
                            createSentMessage(((Nachricht) uebertragung.getUebertragung()).getNachricht(), uebertragung.getSender());
                        }
                    }


                    break;

                //Schließen der ListModels wenn Verbindung durchtrennt
                case 3:
                    sendeNachrichtAnServer(new Uebertragung(0,null));
                    break;

                //Hinzufügen eines neu erstellten Chatrooms in die ClientGui
                case 4:
                    chatrooms.add((Chatroom) uebertragung.getUebertragung());
                    beitrittChatroom(((Chatroom) uebertragung.getUebertragung()).getId());
                    Platform.runLater(() -> createAllChatrooms(chatrooms));
                    break;
                case 5:
                    Platform.runLater(() -> createAllChatrooms((ArrayList<Chatroom>)uebertragung.getUebertragung()));
                    break;

                default:
                    //TODO: Hier bitte noch iwie den User bannen oder so, da er sich nicht an das Protokoll hält.
                    break;
            }
        }
    }

    //Sende Nachricht an einen Chatroom (Benötigen ID des aktuell benutzen Chatrooms)
    public void sendeNachricht(int i) //Methode für ChatroomGUI
    {
        if(txtFieldChat.getText() != null)
        {
            //2 normale senden der Nachrichten
            System.out.println(user.getUsername());
            System.out.println(user);
            sendeNachrichtAnServer(new Uebertragung(2, i, new Nachricht(txtFieldChat.getText(), LocalDateTime.now()), user));
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
                FileChooser fc = new FileChooser();
                FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("Image Files (*.png)", "*.png");
                FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("Image Files (*.jpg)", "*.jpg");
                FileChooser.ExtensionFilter gif = new FileChooser.ExtensionFilter("Image Files (*.gif)", "*.gif");
                fc.getExtensionFilters().add(png);
                fc.getExtensionFilters().add(jpg);
                fc.getExtensionFilters().add(gif);
                File img = fc.showOpenDialog(friendList.getScene().getWindow());
                chat.setImage(img);
                DefaultListModel neuesListModel = new DefaultListModel();
                chat.setChatmodel(neuesListModel);
                Uebertragung neuerchatroom = new Uebertragung(4, chat);
                listmodels.add(neuesListModel);
                outToServer.writeObject(neuerchatroom);
                outToServer.flush();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }



    //Chatrooms Ende

    public void sendeNachrichtAnServer(Uebertragung uebertragung)
    {
        (new ClientWritingThread(uebertragung, getOutToServer())).run();
    }

    public void createAllChatrooms(ArrayList<Chatroom> chat)
    {
        this.chatrooms = chat;
        vBoxRoom.getChildren().clear();

        for(Chatroom c : chatrooms)
        {
            createRoom(c);
        }
    }

    public void createAllMessages(ArrayList<Uebertragung> uebertragungen)
    {
        for(Uebertragung u : uebertragungen)
        {
            if(u.getSender() != user)
            {
                createRecievedMessage(((Nachricht)u.getUebertragung()).getNachricht(), u.getSender(), getChatroomById(u.getZiel()));
            }
            else
            {
                createSentMessage(((Nachricht)u.getUebertragung()).getNachricht(), u.getSender());
            }
        }
    }

    //Methode wird aufgerufen, wenn man auf den Button "Erstellen" einer neuen Gruppe drückt.
    //Sie erzeugt eine neue Gruppe in der Sidebar Gruppen.
    //Ein Neues Gruppenobjekt wird angelegt.
    private void createRoom(Chatroom c)
    {
        ImageView i = new ImageView("file:" + c.getImage().getAbsolutePath());
        i.setFitWidth(50);
        i.setFitHeight(50);
        i.setSmooth(true);
        i.setPreserveRatio(false);
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

//        p.getChildren().add(i);
        p.getChildren().add(b);

        b.setOnMouseClicked(e -> {
            beitrittChatroom(c.getId());
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
                c.getContainer().setVisible(true);
            }
            else
            {
                c.getScrollPane().setVisible(false);
                c.getContainer().setVisible(true);
            }


            System.out.println(c.getName() + " " + c.getId() + " " + c.getScrollPane().isVisible());
        }
    }

    public void beitrittChatroom(int chatroomID)
    {
        try
        {
            Uebertragung beitrittsversuch = new Uebertragung(5, chatroomID,null);
            outToServer.writeObject(beitrittsversuch);
            outToServer.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void verlasseChatroom(int chatroomID)
    {
        try
        {
            Uebertragung leave = new Uebertragung(7, chatroomID,null);
            outToServer.writeObject(leave);
            outToServer.flush();
            System.err.println("In funktion verlasseChatroom");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    //Die Methode erstellt eine Instanz einer erhaltenen Nachricht, die von einen Anderen Nutzer, nicht man selbst, versendet wurde
    private void createRecievedMessage(String msg, User sender, Chatroom c) //Theoretisch brauche ich auch noch Sender, also der User und auch das Sendedatum + Zeit
    {

        Pane p = new Pane();
        //TODO: Auf User-Klasse warten...
/*
        ImageView profilbild = new ImageView("file:" + sender.getProfilbild().getAbsolutePath());
        profilbild.setFitWidth(50);
        profilbild.setFitHeight(50);
        profilbild.setX(5);
        profilbild.setY(5);
        profilbild.setSmooth(true);
        profilbild.setPreserveRatio(false);
*/

/*        Label name = new Label(sender.getUsername());
        name.setLayoutX(60);
        name.setLayoutY(25);
        name.setFont(new Font(17));
        name.setTextFill(Color.WHITE);*/

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
        //p.getChildren().add(profilbild);
        //p.getChildren().add(name);

        Platform.runLater(() -> getChatroomById(c.getId()).getContainer().getChildren().add(p));
    }

    //Die Methode erstellt eine Instanz einer erhaltenen Nachricht, die von einen Anderen Nutzer, nicht man selbst, versendet wurde
    private void createSentMessage(String msg, User sender) //Theoretisch brauche ich auch noch Sender, also der User und auch das Sendedatum + Zeit
    {
        Pane p = new Pane();
        //TODO: Auf User-Klasse warten...
        ImageView profilbild = new ImageView("file:" + sender.getProfilbild().getAbsolutePath());
        profilbild.setFitWidth(50);
        profilbild.setFitHeight(50);
        profilbild.setX(745);
        profilbild.setY(35);
        profilbild.setSmooth(true);
        profilbild.setPreserveRatio(false);

        Label name = new Label(sender.getUsername());
        name.setLayoutX(505);
        name.setLayoutY(60);
        name.setFont(new Font(17));
        name.setTextFill(Color.BLACK);

        Text t = new Text(msg);
        t.setWrappingWidth(250);
        t.setX(15);
        t.setY(40);
        t.setFill(Color.BLACK);

        Pane txtPane = new Pane();
        txtPane.setPrefWidth(275);
        txtPane.setLayoutX(500);
        txtPane.setLayoutY(60);
        txtPane.getStyleClass().add("MessageSent");
        txtPane.getStyleClass().add("DropShadow");
        txtPane.getChildren().add(t);
        txtPane.setPadding(new Insets(0, 0, 15, 0));

        p.getChildren().add(txtPane);
        p.getChildren().add(profilbild);
        p.getChildren().add(name);

        Platform.runLater(() -> getActiveChatroom().getContainer().getChildren().add(p));
    }

    private void createSentImage(File imgFile, User sender) //Theoretisch brauche ich auch noch Sender, also der User und auch das Sendedatum + Zeit
    {
        Pane p = new Pane();
        //TODO: Auf User-Klasse warten...
        ImageView profilbild = new ImageView("file:" + sender.getProfilbild().getAbsolutePath());
        profilbild.setFitWidth(50);
        profilbild.setFitHeight(50);
        profilbild.setX(745);
        profilbild.setY(35);
        profilbild.setSmooth(true);
        profilbild.setPreserveRatio(false);

        Label name = new Label(sender.getUsername());
        name.setLayoutX(505);
        name.setLayoutY(60);
        name.setFont(new Font(17));
        name.setTextFill(Color.BLACK);


        ImageView img = new ImageView("file:" + imgFile.getAbsolutePath());
        img.setFitWidth(225);
        img.setFitHeight(225);
        img.setSmooth(true);
        img.setPreserveRatio(true);
        img.setOnMouseClicked(e -> {
            Pane bigImagePane = new Pane();
            ImageView bigImg = new ImageView("file:"+ imgFile.getAbsolutePath());
            bigImg.setSmooth(true);
            bigImg.setPreserveRatio(true);
            bigImg.setFitWidth(1200);
            bigImg.setFitHeight(800);

            bigImagePane.getChildren().add(bigImg);

            bigImagePane.setOnMouseClicked(event -> {
                paneBackground.getChildren().remove(bigImagePane);
            });

            paneBackground.getChildren().add(bigImagePane);
        });

        Pane imgPane = new Pane();
        imgPane.setPrefWidth(275);
        imgPane.setLayoutX(500);
        imgPane.setLayoutY(25);
        imgPane.getStyleClass().add("MessageSent");
        imgPane.getStyleClass().add("DropShadow");
        imgPane.getChildren().add(img);
        imgPane.setPadding(new Insets(0, 0, 15, 0));

        p.getChildren().add(imgPane);
        p.getChildren().add(profilbild);
        p.getChildren().add(name);
        Platform.runLater(() -> getActiveChatroom().getContainer().getChildren().add(p));
    }

    private void createRecievedImage(File imgFile, User sender) //Theoretisch brauche ich auch noch Sender, also der User und auch das Sendedatum + Zeit
    {
        Pane p = new Pane();
        //TODO: Auf User-Klasse warten...
        ImageView profilbild = new ImageView("file:" + sender.getProfilbild().getAbsolutePath());
        profilbild.setFitWidth(50);
        profilbild.setFitHeight(50);
        profilbild.setX(5);
        profilbild.setY(5);
        profilbild.setSmooth(true);
        profilbild.setPreserveRatio(false);

        Label name = new Label(sender.getUsername());
        name.setLayoutX(60);
        name.setLayoutY(25);
        name.setFont(new Font(17));
        name.setTextFill(Color.WHITE);


        ImageView img = new ImageView("file:" + imgFile.getAbsolutePath());
        img.setFitWidth(225);
        img.setFitHeight(225);
        img.setX(35);
        img.setY(25);
        img.setSmooth(true);
        img.setPreserveRatio(true);
        img.setOnMouseClicked(e -> {
            Pane bigImagePane = new Pane();
            ImageView bigImg = new ImageView("file:"+ imgFile.getAbsolutePath());
            bigImg.setSmooth(true);
            bigImg.setPreserveRatio(true);
            bigImg.setFitWidth(1200);
            bigImg.setFitHeight(800);

            bigImagePane.getChildren().add(bigImg);

            bigImagePane.setOnMouseClicked(event -> {
                paneBackground.getChildren().remove(bigImagePane);
            });

            paneBackground.getChildren().add(bigImagePane);
        });

        Pane imgPane = new Pane();
        imgPane.setPrefWidth(275);
        imgPane.setLayoutX(25);
        imgPane.setLayoutY(25);
        imgPane.getStyleClass().add("MessageRecieved");
        imgPane.getStyleClass().add("DropShadow");
        imgPane.getChildren().add(img);
        imgPane.setPadding(new Insets(0, 0, 10, 0));

        p.getChildren().add(imgPane);
        p.getChildren().add(profilbild);
        p.getChildren().add(name);

        Platform.runLater(() -> getActiveChatroom().getContainer().getChildren().add(p));
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

    public Chatroom getChatroomById(int id)
    {
        Chatroom chat = null;

        for(Chatroom c : chatrooms)
        {
            if(c.getId() == id)
            {
                chat = c;
            }
        }

        return chat;
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


    // Getter und Setter
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

    public void setUser(User user)
    {
        this.user = user;
    }

    public User getUser()
    {
        return this.user;
    }
}
