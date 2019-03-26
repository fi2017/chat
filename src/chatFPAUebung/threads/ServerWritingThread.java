package chatFPAUebung.threads;

import java.io.IOException;

import chatFPAUebung.klassen.ClientProxy;
import chatFPAUebung.klassen.Uebertragung;

public class ServerWritingThread extends Thread
{
	// Attribute
	private Uebertragung uebertragung;
	private ClientProxy client;

	// Konstruktor
	public ServerWritingThread(Uebertragung uebertragung, ClientProxy client)
	{
		this.uebertragung = uebertragung;
		this.client = client;

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
			e.printStackTrace();
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
}
