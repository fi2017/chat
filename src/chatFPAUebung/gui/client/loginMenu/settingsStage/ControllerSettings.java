package chatFPAUebung.gui.client.loginMenu.settingsStage;

import chatFPAUebung.gui.client.loginMenu.startScene.Controller;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
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
import java.util.Locale;
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

    public ComboBox themeComboBox;

    public double offsetx;
    public double offsety;

    // Label für Sprachen
    public Label labelNeustart;
    public Button btnLangDE;
    public Button btnLangEN;
    public Label labelTheme;
    public Label labelUI;
    public Label labelGeneral;
    public Label labelLang;

    public ResourceBundle bundle;
    public Locale locale;

    Controller mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        themeInitialize();
        themeComboBox.getSelectionModel().selectFirst();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../startScene/sample.fxml"));
        mainController = loader.getController();

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

            Path neu1 = Paths.get(theme.getDateipfadLogin());
            Path neu2 = Paths.get(theme.getDateipfadClient());
            neu1 = neu1.toAbsolutePath();
            neu2 = neu2.toAbsolutePath();

            Path original1 = Paths.get("src/chatFPAUebung/gui/client/loginMenu/stylesheet.css");
            Path original2 = Paths.get("src/chatFPAUebung/gui/client/clientMain/Style.css");

            try
            {
                Files.delete(Paths.get("src/chatFPAUebung/gui/client/loginMenu/stylesheet.css"));
                Files.copy(neu1, original1);

                Files.delete(Paths.get("src/chatFPAUebung/gui/client/clientMain/Style.css"));
                Files.copy(neu2, original2);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        themeComboBox.setOnAction(e -> {
            labelNeustart.setVisible(true);
        });

        btnLangDE.setOnAction(e -> {
            loadLang("de");
            mainController.loadLang("de");
        });

        btnLangEN.setOnAction(e -> {
            loadLang("en");
            mainController.loadLang("en");
        });
    }

    private void themeInitialize()
    {
        themeComboBox.getItems().add(new Theme("Dark Theme (Standard)", "src/chatFPAUebung/gui/client/loginMenu/themes/darktheme/stylesheet.css", "src/chatFPAUebung/gui/client/loginMenu/themes/darktheme/Style.css"));
        themeComboBox.getItems().add(new Theme("Light Theme", "src/chatFPAUebung/gui/client/loginMenu/themes/lighttheme/stylesheet.css", "src/chatFPAUebung/gui/client/loginMenu/themes/lighttheme/Style.css"));
    }

    public void loadLang(String lang)
    {
        locale = new Locale(lang);

        bundle = ResourceBundle.getBundle("chatFPAUebung/gui/client/loginMenu/startScene/lang", locale);

        labelNeustart.setText(bundle.getString("labelNeustart"));
        btnLangDE.setText(bundle.getString("btnLangDE"));
        btnLangEN.setText(bundle.getString("btnLangEN"));
        labelTheme.setText(bundle.getString("labelTheme"));
        labelUI.setText(bundle.getString("labelUI"));
        labelGeneral.setText(bundle.getString("labelGeneral"));
        labelLang.setText(bundle.getString("labelLang"));
    }
}
