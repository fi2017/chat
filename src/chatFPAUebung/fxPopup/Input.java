package chatFPAUebung.fxPopup;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Input extends FxPopup
{
	private Label lblInfo = new Label("Input:");
	private Button btnOk = new Button("OK");
	private Button btnCancel = new Button("Cancel");
	private TextField input = new TextField();

	Input()
	{
		title = "Input";

		centerPane.getChildren().add(lblInfo);
		centerPane.getChildren().add(input);
		input.setOnAction(e->btnOk.requestFocus());

		centerPane.setPadding(new Insets(10));
		borderPane.setCenter(centerPane);
		borderPane.setBottom(buttonBar);

		btnOk.setOnAction(event -> close());
		btnCancel.setOnAction(event -> close());

		buttonBar.getChildren().add(btnOk);
		buttonBar.getChildren().add(btnCancel);
		buttonBar.setPadding(new Insets(20));

		scene = new Scene(borderPane);
	}

	public Button getBtnOk()
	{
		return btnOk;
	}

	public TextField getInput()
	{
		return input;
	}

	public void setInfo(String info)
	{
		lblInfo.setText(info);
	}

	public Button getBtnCancel()
	{
		return btnCancel;
	}
}
