package chatFPAUebung.gui.client;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    public FlowPane MenuBar;
    public Button btnClose;
    public Button btnMin;
    public Button btnMax;
    public Button btnSettings;
    public Button btnFriends;
    public Button btnRooms;
    public Button btnAddRoom;
    public VBox SideBar;
    public Image settingsImg;
    public Pane settingsPane;


    private double xOffset;
    private double yOffset;

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

        btnSettings.setOnAction(e -> {
            if(SideBar.getMaxWidth() == 50)
            {
                SideBar.setMaxWidth(300);
                SideBar.setPrefWidth(300);
                RotateTransition rt = new RotateTransition(Duration.millis(300), settingsPane);
                rt.setFromAngle(0.0);
                rt.setToAngle(90.0);

                ScaleTransition st = new ScaleTransition((Duration.millis(300)), SideBar);
                st.setByX(250);

                ParallelTransition pt = new ParallelTransition();
                pt.getChildren().addAll(pt, rt);
                pt.play();
            }
            else
            {
                SideBar.setMaxWidth(50);
                SideBar.setPrefWidth(50);
                RotateTransition rt = new RotateTransition(Duration.millis(300), settingsPane);
                rt.setFromAngle(90.0);
                rt.setToAngle(0.0);
                rt.play();
            }

        });
    }
}
