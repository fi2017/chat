package chatFPAUebung.fxPopup;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Confirm extends FxPopup
{
	private Label lblInfo = new Label("Confirm");
	private Button btnOk = new Button("OK");
	private Button btnCancel = new Button("Cancel");

	Confirm()
	{
		title = "Confirm";

		centerPane.getChildren().add(lblInfo);
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

	public void setInfo(String info)
	{
		lblInfo.setText(info);
	}

	public Button getBtnCancel()
	{
		return btnCancel;
	}
}
