package chatFPAUebung.gui.client;

import javafx.animation.*;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
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

    private double xOffset;
    private double yOffset;


    //TODO:
    //      - Allgemein Nachrichten versenden
    //      - Suchfunktion der für friendList & roomList
    //      - Eibauen der Chaträume
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
        btnClose.setOnAction(e -> ((Stage)btnClose.getScene().getWindow()).close());

        btnMin.setOnAction(e -> ((Stage)btnMin.getScene().getWindow()).setIconified(true));
        //Settingsmenü wieder zurück in die Hauptanwendung
        btnSettingsReturn.setOnMouseEntered(e -> rotateArrow(returnArrow.getRotate() + 360));
        btnSettingsReturn.setOnAction(e -> toggleSettings(0, false));

        //AddRoom Sidebar Raum erstellen
        btnAddRoomCreate.setOnAction(e -> createRoom(txtFieldRoomName.getText(), txtFieldRoomPw.getText()));
        txtFieldRoomName.setOnAction(e -> createRoom(txtFieldRoomName.getText(), txtFieldRoomPw.getText()));
        txtFieldRoomPw.setOnAction(e -> createRoom(txtFieldRoomName.getText(), txtFieldRoomPw.getText()));

        //Chatroom fenster
        //TODO: Evtl. Schauen welcher Chat grade geöffnet ist, und dementsprechend die friendlist bzw. die roomlist öffnen. (Generics mit Wildcards)
        paneChat.setOnMouseClicked(e -> {
            if(paneAddRoomName.isVisible())
            {
                toggleNewRoom(1200, false);
                toggleList(0, friendList);
                moveActivePane(btnFriends);
                movePaneChat(350.0);
            }
        });
    }



    //Methode wird aufgerufen, wenn man auf den Button "Erstellen" einer neuen Gruppe drückt.
    //Sie erzeugt eine neue Gruppe in der Sidebar Gruppen.
    //Ein Neues Gruppenobjekt wird angelegt.
    //TODO: Einbauen einer DisplayRoom Klasse, die für die einzelnen Rooms zuständig ist und den Namen, ID und ?Teilnehmerliste? speichert.
    private void createRoom(String name, String pw)
    {
        if(!name.equals("") && !pw.equals(""))
        {
            //Aktuell geht nur JPG & PNG
            //JPEG geht nicht.
            //Alle anderen Formate wurden noch nicht getestet
            //TODO: Mehrere Bildformate Testen!
            // Abfangen von Fehlern, wenn die Ausgewählte Datei kein Bild ist.
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
            b.setText(name);
            b.setLayoutX(50);
            b.setLayoutY(0);


            p.getChildren().add(i);
            p.getChildren().add(b);

            p.setOnMouseClicked(e -> {
                openChatroom();
            });

            vBoxRoom.getChildren().add(p);
            toggleNewRoom(1200, false);
            toggleList(0, roomList);
            txtFieldRoomPw.setText("");
            txtFieldRoomName.setText("");
            moveActivePane(btnRoom);
            movePaneChat(350);
        }
    }

    //Chatraum wird anhand von der angeklickten Gruppe (oder des Freundes) erstellt.
    //TODO: implementierung der Funktion und evtl. einer Hilfsklasse.
    private void openChatroom()
    {

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
}