package chatFPAUebung.klassen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chatFPAUebung.ddosProtection.Security;
import chatFPAUebung.gui.server.ServerControl;
import chatFPAUebung.threads.ServerReadingThread;

public class ClientProxy
{
	// Attribute
	private Socket clientSocket;
	private ObjectOutputStream outToClient;
	private ObjectInputStream inFromClient;

	private ServerReadingThread serverReadingThread;

	private Security clientSecurity;

	public ClientProxy(ServerControl control, Socket clientSocket)
	{
		try
		{
			this.clientSocket = clientSocket;
			this.outToClient = new ObjectOutputStream(getClientSocket().getOutputStream());
			this.inFromClient = new ObjectInputStream(getClientSocket().getInputStream());
			this.clientSecurity = new Security(this);

			this.serverReadingThread = new ServerReadingThread(control, this);
			getServerReadingThread().start();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Socket getClientSocket()
	{
		return clientSocket;
	}

	public ObjectOutputStream getOutToClient()
	{
		return outToClient;
	}

	public ObjectInputStream getInFromClient()
	{
		return inFromClient;
	}

	public ServerReadingThread getServerReadingThread()
	{
		return this.serverReadingThread;
	}

	public void setClientSocket(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}

	public void setOutToClient(ObjectOutputStream outToClient)
	{
		this.outToClient = outToClient;
	}

	public void setInFromClient(ObjectInputStream inFromClient)
	{
		this.inFromClient = inFromClient;
	}

	public Security getClientSecurity()
	{
		return this.clientSecurity;
	}
}
