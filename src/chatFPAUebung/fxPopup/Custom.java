package chatFPAUebung.fxPopup;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class Custom extends FxPopup
{
	public Custom()
	{
		super();
		title = "Custom";
	}

	public void addContent(Node node)
	{
		super.addContent(node);
	}

	public void addButton(Button button)
	{
		super.addButton(button);
	}
}
