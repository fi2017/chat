package chatFPAUebung.threads;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import chatFPAUebung.gui.client.ClientControl;

public class ClientReadingThread extends Thread
{
	// Attribute
	private ClientControl control;

	// Main
	public ClientReadingThread(ClientControl control)
	{
		this.control = control;

		this.setName("ClientReadingThread");
	}

	// Run
	public void run()
	{
		while (!this.isInterrupted() && this.isAlive())
		{
			try
			{
				Object input = getControl().getInFromServer().readObject();
				getControl().empfangeNachrichtVonServer(input);
			} catch (EOFException e)
			{
				this.interrupt();
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
	public ClientControl getControl()
	{
		return this.control;
	}
}
