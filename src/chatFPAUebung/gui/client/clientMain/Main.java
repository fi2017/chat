package chatFPAUebung.gui.client.clientMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("clientGUI.fxml"));
        Scene s = new Scene(root);
        s.getStylesheets().add("http://fonts.googleapis.com/css?family=Gafata");
        primaryStage.setTitle("Chat");
        System.setProperty("prism.lcdtext", "false");
        primaryStage.setScene(s);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
