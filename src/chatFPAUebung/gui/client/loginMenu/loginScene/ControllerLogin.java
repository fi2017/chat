package chatFPAUebung.gui.client.loginMenu.loginScene;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {
    public FlowPane menuBar;
    public Button closeButton;
    public Button minimizeButton;
    public Button loginButton;
    public Button backButton;

    public GridPane gridContainerLogin;

    public double offsetx;
    public double offsety;

    public void initialize(URL location, ResourceBundle resources) {

        closeButton.setOnAction(e -> {
            ((Stage)closeButton.getScene().getWindow()).close();
        });

        minimizeButton.setOnAction(e -> {
            ((Stage)closeButton.getScene().getWindow()).setIconified(true);
        });

        menuBar.setOnMousePressed(e -> {
            offsetx = e.getSceneX();
            offsety = e.getSceneY();
        });

        menuBar.setOnMouseDragged(e -> {
            ((Stage)menuBar.getScene().getWindow()).setX(e.getScreenX() - offsetx);
            ((Stage)menuBar.getScene().getWindow()).setY(e.getScreenY() - offsety);
        });

        backButton.setOnAction(e -> {
            try
            {
                Parent rootMain = FXMLLoader.load(getClass().getResource("../startScene/sample.fxml"));
                Scene mainScene = loginButton.getScene();

                rootMain.translateYProperty().set(-400);
                Pane parentContainer = (Pane)mainScene.getRoot();
                parentContainer.getChildren().add(rootMain);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(rootMain.translateYProperty(), 0, Interpolator.EASE_OUT);
                KeyValue kv2 = new KeyValue(gridContainerLogin.translateYProperty(), 400, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
                KeyFrame kf2 = new KeyFrame(Duration.seconds(2), kv2);
                timeline.getKeyFrames().add(kf);
                timeline.getKeyFrames().add(kf2);
                timeline.setOnFinished(e1 -> {
                    parentContainer.getChildren().remove(gridContainerLogin);
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
    }
}
