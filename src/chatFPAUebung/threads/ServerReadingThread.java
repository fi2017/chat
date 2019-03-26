package chatFPAUebung.threads;

import java.io.IOException;
import java.net.SocketException;

import chatFPAUebung.gui.server.ServerControl;
import chatFPAUebung.klassen.ClientProxy;

public class ServerReadingThread extends Thread
{
	// Attribute
	private ServerControl control;
	private ClientProxy client;

	// Main
	public ServerReadingThread(ServerControl control, ClientProxy client)
	{
		this.control = control;
		this.client = client;

		this.setName("ServerReadingThread");
	}

	// Run
	public void run()
	{
		while (!this.isInterrupted() && this.isAlive())
		{
			try
			{
				Object input = getClient().getInFromClient().readObject();
				getControl().empfangeNachrichtVonClient(input, getClient());
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			} catch (SocketException e)
			{
				this.interrupt();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	// Getter
	public ServerControl getControl()
	{
		return this.control;
	}

	public ClientProxy getClient()
	{
		return this.client;
	}
}
