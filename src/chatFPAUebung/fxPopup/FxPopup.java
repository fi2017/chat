package chatFPAUebung.fxPopup;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class FxPopup
{
	protected Stage stage = new Stage();
	protected VBox centerPane = new VBox();
	protected Scene scene;
	protected FlowPane buttonBar = new FlowPane();
	protected BorderPane borderPane = new BorderPane();
	protected String title;

	public void show()
	{
		stage.setScene(scene);
		stage.setTitle(title);
		stage.show();
	}

	public void close()
	{
		stage.close();
	}

	public Stage getStage()
	{
		return stage;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
}
