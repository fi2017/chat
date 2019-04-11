package sample.startScene;

import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.sql.Time;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        closeButton.setOnAction(e -> {
            ((Stage)closeButton.getScene().getWindow()).close();
        });

        minimizeButton.setOnAction(e -> {
            ((Stage)closeButton.getScene().getWindow()).setIconified(true);
        });

        /*
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
        */
        loginButton.setOnAction(e -> {
            try
            {
                Parent rootLogin = FXMLLoader.load(getClass().getResource("../loginScene/login.fxml"));
                Scene loginScene = new Scene(rootLogin);

                rootLogin.translateYProperty().set(400);
                parentContainer.getChildren().add(rootLogin);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(rootLogin.translateYProperty(), 0, Interpolator.EASE_OUT);
                KeyValue kv2 = new KeyValue(gridContainer.translateYProperty(), -400, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
                KeyFrame kf2 = new KeyFrame(Duration.seconds(2), kv2);
                timeline.getKeyFrames().add(kf);
                timeline.getKeyFrames().add(kf2);
                timeline.setOnFinished(e1 -> {
                    parentContainer.getChildren().remove(gridContainer);
                });
                timeline.play();
/*
                mainStage = (Stage)((Node)e.getSource()).getScene().getWindow();
                mainStage.setScene(loginScene);
                mainStage.show();
*/
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        });

        registerButton.setOnAction(e -> {
            try
            {
                Parent rootLogin = FXMLLoader.load(getClass().getResource("../registerScene/register.fxml"));
                Scene loginScene = new Scene(rootLogin);

                rootLogin.translateYProperty().set(400);
                parentContainer.getChildren().add(rootLogin);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(rootLogin.translateYProperty(), 0, Interpolator.EASE_OUT);
                KeyValue kv2 = new KeyValue(gridContainer.translateYProperty(), -400, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
                KeyFrame kf2 = new KeyFrame(Duration.seconds(2), kv2);
                timeline.getKeyFrames().add(kf);
                timeline.getKeyFrames().add(kf2);
                timeline.setOnFinished(e1 -> {
                    parentContainer.getChildren().remove(gridContainer);
                });
                timeline.play();
/*
                mainStage = (Stage)((Node)e.getSource()).getScene().getWindow();
                mainStage.setScene(loginScene);
                mainStage.show();
*/
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
}
