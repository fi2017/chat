package chatFPAUebung.fxPopup;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Confirm extends FxPopup
{
	private Label lblInfo = new Label("Confirm");
	private Button btnOk = new Button("OK");
	private Button btnCancel = new Button("Cancel");

	public Confirm()
	{
		super();
		title = "Confirm";
		addContent(lblInfo);

		btnOk.setOnAction(event -> close());
		btnCancel.setOnAction(event -> close());

		addButton(btnOk);
		addButton(btnCancel);


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
