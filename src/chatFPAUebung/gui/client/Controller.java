package chatFPAUebung.gui.client;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{


    public FlowPane MenuBar;
    public Button btnClose;
    public Button btnMin;
    public Button btnMax;

    private double xOffset;
    private double yOffset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnClose.setOnAction(e -> {
            ((Stage)btnClose.getScene().getWindow()).close();
        });

        btnMax.setOnAction(e -> {
            if(!((Stage) btnMax.getScene().getWindow()).isMaximized())
            {
                ((Stage)btnMax.getScene().getWindow()).setMaximized(true);
            }
            else
            {
                ((Stage)btnMax.getScene().getWindow()).setMaximized(false);
            }
        });

        btnMin.setOnAction(e -> {
            ((Stage)btnMin.getScene().getWindow()).setIconified(true);
        });

        MenuBar.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        MenuBar.setOnMouseDragged(e ->{
            ((Stage)MenuBar.getScene().getWindow()).setX(e.getScreenX() - xOffset);
            ((Stage)MenuBar.getScene().getWindow()).setY(e.getScreenY() - yOffset);
        });
    }
}
