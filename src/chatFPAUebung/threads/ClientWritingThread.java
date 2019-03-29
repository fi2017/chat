package chatFPAUebung.threads;

import java.io.IOException;
import java.io.ObjectOutputStream;

import chatFPAUebung.gui.client.ClientControl;
import chatFPAUebung.klassen.Uebertragung;

public class ClientWritingThread extends Thread
{
	// Attribute
	private Uebertragung uebertragung;
	private ObjectOutputStream out;

	// Konstruktor
	public ClientWritingThread(Uebertragung uebertragung,ObjectOutputStream o)
	{
		this.uebertragung = uebertragung;
		this.out = o;

		this.setName("ClientSitedWritingThread");
	}

	// Run
	public void run()
	{
		try
		{
			out.writeObject(getUebertragung());
			out.flush();
			out.reset();
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

}
