package chatFPAUebung.gui.client.loginMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.setProperty("prism.lcdtext", "false");
        Parent root = FXMLLoader.load(getClass().getResource("startScene/sample.fxml"));
        primaryStage.setTitle("FI2017 Chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOpacity(0.98);
        primaryStage.getScene().getStylesheets().add("https://fonts.googleapis.com/css?family=Questrial");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
