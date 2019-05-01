package chatFPAUebung.fxPopup;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

class Info extends FxPopup
{
	private Label lblInfo = new Label("Info");
	private Button btnOk = new Button("OK");

	Info()
	{
		title = "Info";

		centerPane.getChildren().add(lblInfo);
		centerPane.setPadding(new Insets(10));
		borderPane.setCenter(centerPane);
		borderPane.setBottom(buttonBar);

		btnOk.setOnAction(event->close());

		buttonBar.getChildren().add(btnOk);
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
}
