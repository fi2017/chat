package chatFPAUebung.threads;

import java.io.IOException;
import java.net.SocketException;

import chatFPAUebung.gui.server.ServerControl;
import chatFPAUebung.klassen.ClientProxy;
import feature_LoginRegister.LogRegServerControl;

public class ServerReadingThread extends Thread
{
	// Attribute
	private ServerControl control;
	private LogRegServerControl loginControl;
	private ClientProxy client;

	// Main
	public ServerReadingThread(LogRegServerControl loginControl, ClientProxy client)
	{
		this.loginControl = loginControl;
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
				if(loginControl!=null)
				{
					getLoginControl().empfangeNachrichtVonClient(input, getClient());
				}
				if(control!=null)
				{
					getControl().empfangeNachrichtVonClient(input, getClient());
				}
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

	public LogRegServerControl getLoginControl()
	{
		return loginControl;
	}

	public void setLoginControl(LogRegServerControl loginControl)
	{
		this.loginControl = loginControl;
	}
}
