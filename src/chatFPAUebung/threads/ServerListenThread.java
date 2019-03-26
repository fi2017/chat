package chatFPAUebung.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import chatFPAUebung.gui.server.ServerControl;
import chatFPAUebung.klassen.ClientProxy;

public class ServerListenThread extends Thread
{
	// Attribute
	private ServerControl control;

	// Konstruktor
	public ServerListenThread(ServerControl control)
	{
		this.control = control;

		this.setName("ServerListenThread");
	}

	// Run
	public void run()
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(8008);
			serverSocket.setSoTimeout(1000);
			while (!this.isInterrupted() && this.isAlive())
			{
				try
				{
					Socket clientSocket = serverSocket.accept();
					getControl().empfangeClient(new ClientProxy(getControl(), clientSocket));

					serverSocket.close();
					serverSocket = new ServerSocket(8008);
					serverSocket.setSoTimeout(1000);
				} catch (SocketTimeoutException e)
				{
					// Happens every 1000ms for interrupted-check
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			serverSocket.close();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// Getter
	public ServerControl getControl()
	{
		return this.control;
	}
}
