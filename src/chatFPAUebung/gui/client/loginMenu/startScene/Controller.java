package chatFPAUebung.gui.client.loginMenu.startScene;

import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.threads.ClientWritingThread;
import feature_LoginRegister.LogRegNachricht;
import feature_LoginRegister.LogRegReadingThread;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    // Main-Fenster Elemente
    public Button closeButton;
    public Button minimizeButton;
    public Button loginButton;
    public Button registerButton;
    public Button settingsButton;
    public FlowPane menuBar;
    public Pane parentContainer;
    public GridPane gridContainer;

    public double offsetx;
    public double offsety;

    // Login-Fenster Elemente
    public GridPane gridContainerLogin;
    public javafx.scene.control.TextField textFieldUsernameLogin;
    public javafx.scene.control.TextField textFieldPasswordLogin;
    public Button backButtonLogin;
    public Button loginButtonLogin;

    // Register-Fenster Elemente
    public GridPane gridContainerRegister;
    public javafx.scene.control.TextField textFieldUsernameRegister;
    public javafx.scene.control.TextField textFieldPasswordRegister;
    public Button backButtonRegister;
    public Button registerButtonRegister;

    // Richards Zeug
    private Socket clientSocket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private LogRegReadingThread ReadingThread;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        erstelleVerbindung();

        loginButton.setOnAction(e -> {

            gridContainerLogin.setLayoutY(400);
            gridContainerRegister.setLayoutY(400);

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(gridContainerLogin.translateYProperty(), -400, Interpolator.EASE_BOTH);
            KeyValue kv2 = new KeyValue(gridContainer.translateYProperty(), -400, Interpolator.EASE_BOTH);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            KeyFrame kf2 = new KeyFrame(Duration.seconds(1), kv2);
            timeline.getKeyFrames().add(kf);
            timeline.getKeyFrames().add(kf2);
            timeline.play();
        });

        registerButton.setOnAction(e -> {

            gridContainerRegister.setLayoutY(400);
            gridContainerLogin.setLayoutY(400);

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(gridContainerRegister.translateYProperty(), -400, Interpolator.EASE_BOTH);
            KeyValue kv2 = new KeyValue(gridContainer.translateYProperty(), -400, Interpolator.EASE_BOTH);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            KeyFrame kf2 = new KeyFrame(Duration.seconds(1), kv2);
            timeline.getKeyFrames().add(kf);
            timeline.getKeyFrames().add(kf2);
            timeline.play();
        });

        backButtonLogin.setOnAction(e -> {
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(gridContainer.translateYProperty(), 0, Interpolator.EASE_OUT);
            KeyValue kv2 = new KeyValue(gridContainerLogin.translateYProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            KeyFrame kf2 = new KeyFrame(Duration.seconds(2), kv2);
            timeline.getKeyFrames().add(kf);
            timeline.getKeyFrames().add(kf2);
            timeline.play();

        });

        backButtonRegister.setOnAction(e -> {
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(gridContainer.translateYProperty(), 0, Interpolator.EASE_OUT);
            KeyValue kv2 = new KeyValue(gridContainerRegister.translateYProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            KeyFrame kf2 = new KeyFrame(Duration.seconds(2), kv2);
            timeline.getKeyFrames().add(kf);
            timeline.getKeyFrames().add(kf2);
            timeline.play();

        });

        loginButtonLogin.setOnAction(e -> {
            if(!checkEmptyLog())
            {
                sendeNachrichtAnServer(new Uebertragung(10, new LogRegNachricht(textFieldUsernameLogin.getText(),textFieldPasswordLogin.getText())));
                System.out.println("LogRegControl: sendenNachricht() aus Methode loginUser()");
            }
        });

        registerButtonRegister.setOnAction(e -> {
            if(!checkEmptyReg())
            {
                sendeNachrichtAnServer(new Uebertragung(11, new LogRegNachricht(textFieldUsernameRegister.getText(),textFieldPasswordRegister.getText())));
            }
            else
            {
                // Sp�ter in der Gui als Label Anzeige einf�gen
                System.out.println("Passw�rter stimmen nicht �berein!");
            }
        });

        closeButton.setOnAction(e -> {
            ((Stage)closeButton.getScene().getWindow()).close();
        });

        minimizeButton.setOnAction(e -> {
            ((Stage)closeButton.getScene().getWindow()).setIconified(true);
        });

        settingsButton.setOnAction(e -> {
            try
            {
                Stage stageSettings = new Stage();
                Parent rootSettings = FXMLLoader.load(getClass().getResource("../settingsStage/settings.fxml"));
                stageSettings.setScene(new Scene(rootSettings));
                stageSettings.setResizable(false);
                stageSettings.initStyle(StageStyle.UNDECORATED);
                stageSettings.setOpacity(0.98);
                stageSettings.getScene().getStylesheets().add("https://fonts.googleapis.com/css?family=Questrial");
                stageSettings.show();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        });

        menuBar.setOnMousePressed(e -> {
            offsetx = e.getSceneX();
            offsety = e.getSceneY();
        });

        menuBar.setOnMouseDragged(e -> {
            ((Stage)menuBar.getScene().getWindow()).setX(e.getScreenX() - offsetx);
            ((Stage)menuBar.getScene().getWindow()).setY(e.getScreenY() - offsety);
        });
    }

    public void erstelleVerbindung()
    {
        try
        {
            setClientSocket(new Socket("localhost", 8008));
            System.out.println("Socket erstellt");
            setOutToServer(new ObjectOutputStream(getClientSocket().getOutputStream()));
            setInFromServer(new ObjectInputStream(getClientSocket().getInputStream()));

            setReadingThread(new LogRegReadingThread(this));
            getReadingThread().start();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        sendeNachrichtAnServer(new Uebertragung(1, null));
    }

    public void empfangeNachrichtVonServer(Object uebertragungObjekt)
    {
        if (uebertragungObjekt instanceof Uebertragung)
        {
            Uebertragung uebertragung = (Uebertragung) uebertragungObjekt;

            switch (((Uebertragung) uebertragungObjekt).getZweck())
            {
                case 1://wenn login erfolgreich
/*					ClientControl control= new ClientControl(this);
					control.setClientSocket(clientSocket);
					control.setInFromServer(inFromServer);
					control.setOutToServer(outToServer);
					control.setClientReadingThread(new ClientReadingThread(control));
					control.getClientReadingThread().start();*/

                    ReadingThread.interrupt();
                    ReadingThread = null;

                    Platform.runLater( () -> {

                        ((Stage) closeButton.getScene().getWindow()).close();
                        try
                        {
                            Stage primaryStage = new Stage();
                            Parent root = FXMLLoader.load(getClass().getResource("../../clientMain/clientGUI.fxml"));
                            Scene s = new Scene(root);
                            s.getStylesheets().add("http://fonts.googleapis.com/css?family=Gafata");
                            primaryStage.setTitle("Chat");
                            System.setProperty("prism.lcdtext", "false");
                            primaryStage.setScene(s);
                            primaryStage.initStyle(StageStyle.TRANSPARENT);
                            primaryStage.show();

                        }
                        catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    });




                    break;

                case 2: //wenn login nicht erfolgreich - PW falsch
                    //getGui().getLblErrormsg().setText("Passwort falsch");
                    break;

                case 3:  //wenn login nicht erfolgreich  - User falsch/unbekannt
                    //getGui().getLblErrormsg().setText("Username nicht bekannt");
                    break;
                case 4: //wenn login nicht erfolgreich - User schon eingeloggt
                    //getGui().getLblErrormsg().setText("User ist schon eingeloggt");
                    break;

                case 5: //wenn login nicht erfolgreich - User gebannt
                    //getGui().getLblErrormsg().setText("Der Banhammer hat gesprochen!");
                    break;
                case 6: // wenn register  erfolgreich
                    //getGui().getLblErrormsg().setText("Registrierung erfolgreich");
                    break;
                case 7: //wenn register nicht erfolgreich - Username schon benutzt
                    //getGui().getLblErrormsg().setText("Username schon vergeben");
                    break;
                case 8: //wenn bei der �bertragung was schiefgeht
                    //getGui().getLblErrormsg().setText("�bertragungsfehler");
                default:
                    //getGui().getLblErrormsg().setText("Protokollfehler");
                    break;
            }
        }
    }

    public void sendeNachrichtAnServer(Uebertragung uebertragung)
    {
        (new ClientWritingThread(uebertragung, getOutToServer())).run();
    }

    private boolean checkEmptyLog()
    {
        boolean returnValue=false;

        if(textFieldPasswordLogin.getText().trim().length() == 0 || textFieldUsernameLogin.getText().trim().length() == 0)
        {
            returnValue = true;
            System.out.println("Login Textfelder leer");
        }

        return returnValue;
    }
    private boolean checkEmptyReg()
    {
        boolean returnValue=false;

        if(textFieldPasswordRegister.getText().trim().length() == 0 || textFieldUsernameRegister.getText().trim().length() == 0)
        {
            returnValue = true;
        }

        return returnValue;
    }

/*
    private boolean checkPasswordWith2nd()
    {
        boolean returnValue=false;

        if(getGui().getTextFieldPwReg().getText().equals(getGui().getTextFieldPwRepeat().getText()))
        {
            returnValue = true;
        }

        return returnValue;
    }
*/

    public ObjectOutputStream getOutToServer()
    {
        return outToServer;
    }

    public void setOutToServer(ObjectOutputStream outToServer)
    {
        this.outToServer = outToServer;
    }

    public ObjectInputStream getInFromServer()
    {
        return inFromServer;
    }

    public void setInFromServer(ObjectInputStream inFromServer)
    {
        this.inFromServer = inFromServer;
    }

    public Socket getClientSocket()
    {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    public LogRegReadingThread getReadingThread()
    {
        return ReadingThread;
    }

    public void setReadingThread(LogRegReadingThread readingThread)
    {
        ReadingThread = readingThread;
    }
}
