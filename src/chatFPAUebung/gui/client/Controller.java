package chatFPAUebung.gui.client;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{

    public Button btnClose;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnClose.setOnAction(e -> {
            ((Stage)btnClose.getScene().getWindow()).close();
        });
    }
}
