package chatFPAUebung.gui.client.loginMenu.settingsStage;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerSettings implements Initializable {

    public Button closeButton;
    public Button minimizeButton;
    public Button generalSettings;
    public Button uiSettings;
    public Button confirmButton;
    public FlowPane menuBar;
    public Pane generalPane;
    public Pane uiPane;
    public Label labelNeustart;

    public ComboBox themeComboBox;

    public double offsetx;
    public double offsety;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        themeInitialize();

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

        generalSettings.setOnAction(e -> {
            Timeline timeline1 = new Timeline();
            timeline1.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(0.3),
                            new KeyValue(uiPane.translateXProperty(), 610, Interpolator.EASE_BOTH)
                            ));
            timeline1.setOnFinished(e1 -> {
                Timeline timeline2 = new Timeline();
                timeline2.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(0.3),
                                new KeyValue(generalPane.translateXProperty(), 0, Interpolator.EASE_BOTH)
                        ));
                timeline2.play();
            });
            timeline1.play();
        });

        uiSettings.setOnAction(e -> {
            Timeline timeline1 = new Timeline();
            timeline1.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(0.3),
                            new KeyValue(generalPane.translateXProperty(), 610, Interpolator.EASE_BOTH)
                    ));
            timeline1.setOnFinished(e1 -> {
                Timeline timeline2 = new Timeline();
                timeline2.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(0.3),
                                new KeyValue(uiPane.translateXProperty(), 0, Interpolator.EASE_BOTH)
                        ));
                timeline2.play();
            });
            timeline1.play();
        });

        confirmButton.setOnAction(e -> {
            Theme theme = (Theme)themeComboBox.getValue();

            Path neu = Paths.get(theme.getDateiname());
            neu = neu.toAbsolutePath();
            System.out.println(neu);
            Path original = Paths.get("src/chatFPAUebung/gui/client/loginMenu/stylesheet.css");

            try
            {
                Files.delete(Paths.get("src/chatFPAUebung/gui/client/loginMenu/stylesheet.css"));
                Files.copy(neu, original);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        themeComboBox.setOnAction(e -> {
            labelNeustart.setVisible(true);
        });
    }

    private void themeInitialize()
    {
        themeComboBox.getItems().add(new Theme("Dark Theme (Standard)", "src/chatFPAUebung/gui/client/loginMenu/themes/darktheme/stylesheet.css"));
        themeComboBox.getItems().add(new Theme("Light Theme", "src/chatFPAUebung/gui/client/loginMenu/themes/lighttheme/stylesheet.css"));
    }
}
