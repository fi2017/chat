package chatFPAUebung.threads;

import java.io.IOException;

import chatFPAUebung.gui.server.ServerControl;
import chatFPAUebung.klassen.ClientProxy;
import chatFPAUebung.klassen.Uebertragung;

public class ServerWritingThread extends Thread
{
	// Attribute
	private Uebertragung uebertragung;
	private ClientProxy client;
	private ServerControl control;

	// Konstruktor
	public ServerWritingThread(Uebertragung uebertragung, ClientProxy client, ServerControl control)
	{
		this.uebertragung = uebertragung;
		this.client = client;
		this.control = control;

		this.setName("ServerWritingThread");
	}

	// Methoden
	public void run()
	{
		try
		{
			getClient().getOutToClient().writeObject(getUebertragung());
			getClient().getOutToClient().flush();
			getClient().getOutToClient().reset();
		} catch (IOException e)
		{
			getControl().removeUser(getClient());
		}
	}

	// Getter
	public Uebertragung getUebertragung()
	{
		return uebertragung;
	}

	public ClientProxy getClient()
	{
		return client;
	}

	public ServerControl getControl()
	{
		return this.control;
	}
}
