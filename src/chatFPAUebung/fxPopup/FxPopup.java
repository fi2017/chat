package chatFPAUebung.fxPopup;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@SuppressWarnings("WeakerAccess")
public abstract class FxPopup
{
	protected Stage stage = new Stage();
	protected VBox centerPane = new VBox();
	protected Scene scene = null;
	protected FlowPane buttonBar = new FlowPane();
	protected BorderPane borderPane = new BorderPane();
	protected String title;
	
	

	public FxPopup()
	{
		centerPane.setPadding(new Insets(10));
		buttonBar.setPadding(new Insets(20));

		borderPane.setCenter(centerPane);
		borderPane.setBottom(buttonBar);
	}

	public void show()
	{
		if(scene==null)
		{
			scene = new Scene(borderPane);
		}

		stage.setScene(scene);
		stage.setTitle(title);
		stage.show();
	}

	protected void addContent(Node node)
	{
		centerPane.getChildren().add(node);
	}

	protected void addButton(Button button)
	{
		buttonBar.getChildren().add(button);
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
