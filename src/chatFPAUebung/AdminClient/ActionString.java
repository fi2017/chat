package chatFPAUebung.AdminClient;

/*
	this class is for displaying the possible actions a Admin can take to influence the server or the users

	it is used in the AdminClint\Controller.java in a ComboBox
 */
public class ActionString
{
	private int action;
	private String display;

	ActionString(int action, String display)
	{
		this.action = action;
		this.display = display;
	}

	int getAction()
	{
		return action;
	}

	@Override
	public String toString()
	{
		return display;
	}
}
