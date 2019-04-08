package AdminClient;

/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainAdmin extends Application
{

	private Controller controller;

	//custom exit to be able to close threads an inform server
	@Override
	public void stop() throws Exception
	{
		controller.exit();
		super.stop();
		System.exit(0); // just to be sure its dead
	}

	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("guiAdmin.fxml"));
			Parent root = fxmlLoader.load();
			primaryStage.setTitle("Admin");
			primaryStage.setScene(new Scene(root));

			controller = fxmlLoader.getController();
			
			primaryStage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
