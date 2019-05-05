package chatFPAUebung.gui.client.clientMain;


import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.activation.ActivationGroup_Stub;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;

import chatFPAUebung.fxPopup.Custom;
import chatFPAUebung.klassen.Chatroom;
import chatFPAUebung.klassen.Nachricht;
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
import java.util.Locale;
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
    private DefaultListModel<Nachricht> listModel;
    private Socket clientSocket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private ClientReadingThread clientReadingThread;
    private ArrayList<Chatroom> chatrooms = new ArrayList<Chatroom>();
    private ArrayList<DefaultListModel> listmodels = new ArrayList<DefaultListModel>();


    private int i;
    private User sender;
    private User marcel, julian, sebastian, katrin;
    private ArrayList<User> friends = new ArrayList<>();

    private Locale locale;
    private ResourceBundle bundle;

    //Anlegen der events und der einzelnen Komponenten der GUI
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadLang(readLang());

        //Anwendung in "startform" geben
        vBoxRoom.setSpacing(5);
        friendList.setSpacing(5);
        this.listModel = new DefaultListModel<Nachricht>();
        listmodels.add(listModel);
        toggleNewRoom(1200, false);
        erstelleVerbindung();
        hideLists();
        initializeUser();
        addFriends(friends);


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

            if(!txtFieldChat.getText().equals(""))
            {
                createSentMessage(txtFieldChat.getText(), sender, getActiveChatroom());
                Platform.runLater(() -> checkMessages(getActiveChatroom()));
            }
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
            createSentImage(img, sender, getActiveChatroom());

        });

        btnSearchFriend.setOnAction(e -> {
            if(searchFieldFriend.getText().equals("Katrin"))
            {
                addFriend(katrin);
            }
            searchFieldFriend.setText("");
        });

        searchFieldFriend.setOnAction(e -> {
            if(searchFieldFriend.getText().equals("Katrin"))
            {
                addFriend(katrin);
            }
            searchFieldFriend.setText("");
        });
    }

    private void checkMessages(Chatroom c)
    {
        if(c.getName().equals("Katrin"))
        {

        }
        else if(c.getName().equals("Test"))
        {
            if(c.getContainer().getChildren().size() == 0)
            {
                Platform.runLater(() -> createRecievedMessage("Hii", marcel, getActiveChatroom()));
            }
            else if(c.getContainer().getChildren().size() == 2)
            {
                Platform.runLater(() -> createRecievedMessage("Ach, ganz gut^^", marcel, getActiveChatroom()));
            }
            else if(c.getContainer().getChildren().size() == 4)
            {
                Platform.runLater(() -> createRecievedMessage("Nice :D", marcel, getActiveChatroom()));
            }
        }
    }

    private void openChatroomById(int id)
    {
        for(Chatroom c : chatrooms)
        {
            if(c.getId() == id)
                c.getScrollPane().setVisible(true);
            else
                c.getScrollPane().setVisible(false);
        }
    }

    private void initializeUser()
    {
        sender = new User();
        sender.setUsername("Michael");
        sender.setId(0);
        sender.setImage(new File("src/chatFPAUebung/gui/client/clientMain/images/michael.png"));

        marcel = new User();
        marcel.setUsername("Marcel");
        marcel.setId(1);
        marcel.setImage(new File("src/chatFPAUebung/gui/client/clientMain/images/marcel.png"));
        marcel.setOnline(true);

        julian = new User();
        julian.setUsername("Julian");
        julian.setId(2);
        julian.setImage(new File("src/chatFPAUebung/gui/client/clientMain/images/wolf.png"));
        julian.setOnline(true);

        sebastian = new User();
        sebastian.setUsername("Sebastian");
        sebastian.setId(2);
        sebastian.setImage(new File("src/chatFPAUebung/gui/client/clientMain/images/mark.png"));
        sebastian.setOnline(false);

        katrin = new User();
        katrin.setUsername("Katrin");
        katrin.setId(2);
        katrin.setImage(new File("src/chatFPAUebung/gui/client/clientMain/images/stremme.png"));
        katrin.setOnline(true);

        friends.add(marcel);
        friends.add(sebastian);
        friends.add(julian);
    }

    private void addFriends(ArrayList<User> friends)
    {
        for (User u : friends)
        {
            addFriend(u);
        }
    }

    //Methode wird aufgerufen, wenn man auf den Button "Erstellen" einer neuen Gruppe drückt.
    //Sie erzeugt eine neue Gruppe in der Sidebar Gruppen.
    //Ein Neues Gruppenobjekt wird angelegt.
    private void createRoom(Chatroom c)
    {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("Image Files (*.png)", "*.png");
        FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("Image Files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter gif = new FileChooser.ExtensionFilter("Image Files (*.gif)", "*.gif");
        fc.getExtensionFilters().add(png);
        fc.getExtensionFilters().add(jpg);
        fc.getExtensionFilters().add(gif);
        File img = fc.showOpenDialog(friendList.getScene().getWindow());
        c.setImage(img);

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
        b.getStyleClass().add("Font");
        b.getStyleClass().add("Room");
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
        c.getScrollPane().setLayoutX(25);
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
    private void createRecievedMessage(String msg, User user, Chatroom chatroom) //Theoretisch brauche ich auch noch Sender, also der User und auch das Sendedatum + Zeit
    {
        Pane p = new Pane();
        ImageView i = new ImageView("file:" + user.getImage().getAbsolutePath());
        i.setFitWidth(50);
        i.setFitHeight(50);
        i.setX(5);
        i.setY(5);
        i.setSmooth(true);
        i.setPreserveRatio(false);

        Label name = new Label(user.getUsername());
        name.setLayoutX(60);
        name.setLayoutY(25);
        name.setFont(new Font(17));
        name.setTextFill(Color.WHITE);

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
        p.getChildren().add(i);
        p.getChildren().add(name);


        Platform.runLater(() -> chatroom.getContainer().getChildren().add(p));
    }

    //Die Methode erstellt eine Instanz einer erhaltenen Nachricht, die von einen Anderen Nutzer, nicht man selbst, versendet wurde
    private void createSentMessage(String msg, User user, Chatroom chatroom)
    {
        Pane p = new Pane();

        ImageView i = new ImageView("file:" + user.getImage().getAbsolutePath());
        i.setFitWidth(50);
        i.setFitHeight(50);
        i.setX(745);
        i.setY(5);
        i.setSmooth(true);
        i.setPreserveRatio(false);

        Label name = new Label(user.getUsername());
        name.setLayoutX(505);
        name.setLayoutY(25);
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
        txtPane.setLayoutY(25);
        txtPane.getStyleClass().add("MessageSent");
        txtPane.getStyleClass().add("DropShadow");
        txtPane.getChildren().add(t);
        txtPane.setPadding(new Insets(0, 0, 15, 0));

        p.getChildren().add(txtPane);
        p.getChildren().add(i);
        p.getChildren().add(name);

        Platform.runLater(() -> chatroom.getContainer().getChildren().add(p));
    }

    private void createRecievedImage(File imgFile, User user, Chatroom chatroom)
    {
        Pane p = new Pane();
        ImageView i = new ImageView("file:" + user.getImage().getAbsolutePath());
        i.setFitWidth(50);
        i.setFitHeight(50);
        i.setX(5);
        i.setY(5);
        i.setSmooth(true);
        i.setPreserveRatio(false);

        Label name = new Label(user.getUsername());
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
            Custom popup = new Custom();

            Pane bigImagePane = new Pane();
            ImageView bigImg = new ImageView("file:"+ imgFile.getAbsolutePath());
            bigImg.setSmooth(true);
            bigImg.setPreserveRatio(true);

            bigImg.setFitWidth(1200);
            bigImg.setFitHeight(800);

            bigImagePane.getChildren().add(bigImg);

            bigImagePane.setOnMouseClicked(event -> {
                popup.close();
            });

            popup.addContent(bigImagePane);
            popup.show();
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
        p.getChildren().add(i);
        p.getChildren().add(name);

        Platform.runLater(() -> chatroom.getContainer().getChildren().add(p));
    }

    private void createSentImage(File imgFile, User user, Chatroom chatroom) //Theoretisch brauche ich auch noch Sender, also der User und auch das Sendedatum + Zeit
    {
        Pane p = new Pane();

        ImageView i = new ImageView("file:" + user.getImage().getAbsolutePath());
        i.setFitWidth(50);
        i.setFitHeight(50);
        i.setX(745);
        i.setY(5);
        i.setSmooth(true);
        i.setPreserveRatio(false);

        Label name = new Label(user.getUsername());
        name.setLayoutX(505);
        name.setLayoutY(25);
        name.setFont(new Font(17));
        name.setTextFill(Color.BLACK);

        ImageView img = new ImageView("file:" + imgFile.getAbsolutePath());
        img.setFitWidth(225);
        img.setFitHeight(225);
        img.setY(25);
        img.setSmooth(true);
        img.setPreserveRatio(true);

        img.setOnMouseClicked(e -> {
            Custom popup = new Custom();

            Pane bigImagePane = new Pane();
            ImageView bigImg = new ImageView("file:"+ imgFile.getAbsolutePath());
            bigImg.setSmooth(true);
            bigImg.setPreserveRatio(true);

            bigImg.setFitWidth(1200);
            bigImg.setFitHeight(800);

            bigImagePane.getChildren().add(bigImg);

            bigImagePane.setOnMouseClicked(event -> {
                popup.close();
            });

            popup.addContent(bigImagePane);
            popup.show();
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
        p.getChildren().add(i);
        p.getChildren().add(name);

        Platform.runLater(() -> chatroom.getContainer().getChildren().add(p));
    }

    private void addFriend(User user)
    {
        Pane p = new Pane();
        p.setMinSize(350, 50);
        p.setPrefSize(350, 50);
        p.setMaxSize(350, 50);
        p.setVisible(true);

        ImageView i = new ImageView("file:" + user.getImage().getAbsolutePath());
        i.setFitWidth(50);
        i.setFitHeight(50);
        i.setSmooth(true);
        i.setPreserveRatio(false);

        Button b = new Button();
        b.setMinSize(295, 50);
        b.setPrefSize(295, 50);
        b.setMaxSize(295, 50);
        b.getStyleClass().add("Font");
        b.getStyleClass().add("Room");
        b.setText(user.getUsername());
        b.setLayoutX(50);
        b.setLayoutY(0);
        b.setTextAlignment(TextAlignment.LEFT);

        Rectangle online = new Rectangle();
        online.setHeight(50);
        online.setWidth(5);
        online.setLayoutX(345);
        online.setLayoutY(0);
        if(user.isOnline())
            online.setFill(Color.GREEN);
        else
            online.setFill(Color.RED);

        p.getChildren().add(i);
        p.getChildren().add(b);
        p.getChildren().add(online);

        b.setOnMouseClicked(e -> {
            openChatroom(e.getSource());
        });

        Chatroom c = new Chatroom(user.getUsername(), 2);
        chatrooms.add(c);

        c.setContainer(new VBox());
        c.getContainer().setLayoutX(0);
        c.getContainer().setLayoutY(0);
        if(getActiveChatroom() != null)
        {
            getActiveChatroom().getScrollPane().setVisible(false);
        }
        c.setScrollPane(new ScrollPane(c.getContainer()));
        c.getScrollPane().setVisible(true);
        c.getScrollPane().setPrefSize(800, 600);
        c.getScrollPane().setLayoutX(25);
        c.getScrollPane().setLayoutY(0);
        c.getScrollPane().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        c.getScrollPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        c.getScrollPane().getStyleClass().add("Remove-Border");

        //Muss man so machen, da der Inhalt der VBox, also dem "Container" immer neu gesetzt wird.
        c.getContainer().heightProperty().addListener(observable -> c.getScrollPane().setVvalue(1D));
        paneChat.getChildren().add(c.getScrollPane());
        vBoxRoom.getChildren().add(p);

        createMessages(user);

        Platform.runLater(() -> friendList.getChildren().add(p));
    }

    private void createMessages(User user)
    {
        if(user.getUsername().equals("Julian"))
        {
            createSentMessage("Und wie finden Sie unser Projekt?", sender, getChatroomById(2));
            createRecievedMessage("Deutlich besser als das von der anderen Gruppe!", julian, getChatroomById(2));
            createSentMessage("Ja gut das war ja klar :D Wir haben ja auch Sie als Lehrer^^", sender, getChatroomById(2));
            createRecievedMessage("Ja das stimmt schon, der Herr Mark ist halt einfach nicht so gut wie ich...", julian, getChatroomById(2));
            createSentMessage("Oha, das kommt jetzt aber von Ihnen! Sowas würde ich NIE sagen!", sender, getChatroomById(2));
        }
        else if(user.getUsername().equals("Sebastian"))
        {
            createSentMessage("Und wie ist es so, Java ohne mich zu unterrichten?", sender, getChatroomById(1));
            createRecievedMessage("Schrecklich! Seit dem du weg bist, ist die Gruppe einfach nicht mehr die Selbe... Ich habe keinen mehr, der tatkräftig mitarbeitet, aufmerksahm ist und so nett und hilfsbereit ist wie du.", sebastian, getChatroomById(1));
            createSentMessage("Ach was, Sie schaffen das schon! Sie sind doch ein eins a Lehrer!", sender, getChatroomById(1));
        }
        else if(user.getUsername().equals("Marcel"))
        {
            createRecievedMessage("jo michael?", marcel, getChatroomById(0));
            createSentMessage("yo habs jetzt erst gelesen :D", sender,getChatroomById(0));
            createSentMessage("wars was wichtiges, oder hat's sich erledigt?", sender, getChatroomById(0));
            createRecievedMessage("ist relativ wichtig", marcel, getChatroomById(0));
            createRecievedMessage("es geht um reli", marcel, getChatroomById(0));
            createSentMessage("was is denn?", sender, getChatroomById(0));
            createRecievedMessage("Weißt du denn, welche Blätter wir alles lernen müssen?", marcel, getChatroomById(0));
            createSentMessage("Ja, glaub schon. Des müssten des eine Sekten Blatt sein. Und dann noch den eine vom letzten mal. Mit dem UL oder so.", sender, getChatroomById(0));
            createRecievedMessage("Ah okay, cool", marcel, getChatroomById(0));
            createRecievedMessage("Dann kann ich ja jetzt sorgenfrei lernen!", marcel, getChatroomById(0));

        }
        hideChatrooms();
    }

    private void hideChatrooms()
    {
        for(Chatroom c : chatrooms)
            Platform.runLater(() -> c.getScrollPane().setVisible(false));
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
                    if(uebertragung.getUebertragung() instanceof Nachricht)
                    {
/*
                        DefaultListModel aktuellesModel=listmodels.get(((Uebertragung) uebertragungObjekt).getZiel());
                        aktuellesModel.addElement(uebertragung.getUebertragung());
*/

                        //User user = new User();
                        //user.setUsername("Michael");
                        //user.setId(1);
                        /*
                        if(((Nachricht) uebertragung.getUebertragung()).getSender() != user)
                        {
                            createRecievedMessage(((Nachricht) uebertragung.getUebertragung()).getNachricht());
                        }
                        else
                        {
                            createSentMessage(((Nachricht) uebertragung.getUebertragung()).getNachricht());
                        }
                        */

                        createSentMessage(((Nachricht) uebertragung.getUebertragung()).getNachricht(), sender, getChatroomById(uebertragung.getZiel()));
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
                    break;
            }
        }
    }

    private Chatroom getChatroomById(int ziel)
    {
        Chatroom chat = new Chatroom();
        for(Chatroom c : chatrooms)
        {
            if(c.getId() == ziel)
                chat = c;
        }
        return chat;
    }

    //Sende Nachricht an einen Chatroom (Benötigen ID des aktuell benutzen Chatrooms)
    public void sendeNachricht(int i) //Methode für ChatroomGUI
    {
        if(txtFieldChat.getText() != null)
        {
            //2 normale senden der Nachrichten
            sendeNachrichtAnServer(new Uebertragung(2, i, new Nachricht(txtFieldChat.getText(), LocalDateTime.now())));
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
/*                DefaultListModel neuesListModel = new DefaultListModel();
                chat.setChatmodel(neuesListModel);
                Uebertragung neuerchatroom = new Uebertragung(4, chat);
                listmodels.add(neuesListModel);
                outToServer.writeObject(neuerchatroom);
                outToServer.flush();*/
                chatrooms.add(chat);
                createRoom(chat);
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

    public void loadLang(String lang)
    {
        locale = new Locale(lang);

        bundle = ResourceBundle.getBundle("chatFPAUebung/gui/client/loginMenu/startScene/lang", locale);

        btnFriends.setText(bundle.getString("btnFriends"));
        btnRoom.setText(bundle.getString("btnRoom"));
        btnNewRoom.setText(bundle.getString("btnNewRoom"));
        btnSettings.setText(bundle.getString("btnSettings"));
        labelAddRoomName.setText(bundle.getString("labelAddRoomName"));
        labelAddRoomPw.setText(bundle.getString("labelAddRoomPw"));
        btnAddRoomCreate.setText(bundle.getString("btnAddRoomCreate"));
    }

    public String readLang()
    {
        String zeile = "";

        try
        {
            Path path = Paths.get("src/chatFPAUebung/gui/client/loginMenu/startLang.txt");
            BufferedReader reader = Files.newBufferedReader(path);

            try
            {
                zeile = reader.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                reader.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return zeile;
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
}
