package chatFPAUebung.threads;

import java.io.IOException;

import chatFPAUebung.gui.client.ClientControl;
import chatFPAUebung.klassen.Uebertragung;

public class ClientWritingThread extends Thread
{
	// Attribute
	private Uebertragung uebertragung;
	private ClientControl control;

	// Konstruktor
	public ClientWritingThread(Uebertragung uebertragung, ClientControl control)
	{
		this.uebertragung = uebertragung;
		this.control = control;

		this.setName("ClientWritingThread");
	}

	// Run
	public void run()
	{
		try
		{
			getControl().getOutToServer().writeObject(getUebertragung());
			getControl().getOutToServer().flush();
			getControl().getOutToServer().reset();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// Getter
	public Uebertragung getUebertragung()
	{
		return uebertragung;
	}

	public ClientControl getControl()
	{
		return control;
	}
}
