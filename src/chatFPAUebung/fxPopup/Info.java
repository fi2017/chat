package chatFPAUebung.fxPopup;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Info extends FxPopup
{
	private Label lblInfo = new Label("Info");
	private Button btnOk = new Button("OK");

	public Info()
	{
		super();
		title = "Info";

		addContent(lblInfo);

		btnOk.setOnAction(event->close());

		addButton(btnOk);
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
