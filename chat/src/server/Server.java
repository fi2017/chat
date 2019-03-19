package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

class Server implements Runnable
{
	private int portnummer;
	private Thread thread;
	private Control control;
	private ArrayList<ClientProxy> clientliste;

	public Server(int portnummer, Control c)
	{
		this.portnummer = portnummer;
		this.control = c;
		clientliste = new ArrayList<>();
	}

	public void starteServer()
	{
		thread = new Thread(this);
		thread.start();
	}

	public void stoppeServer()
	{
		thread.interrupt();
	}

	public void run()
	{
		ServerSocket serversocket;

		try
		{
			serversocket = new ServerSocket(portnummer);
			serversocket.setSoTimeout(100);
			control.setStatus("Server gestartet auf Port: " + portnummer);

			while (!thread.isInterrupted())
			{
				try
				{
					Socket socket = serversocket.accept();

					// Abbruch bei Sockettimeout

					clientliste.add(new ClientProxy(socket, this));
					control.setStatus("Verbindung akzeptiert: " + socket);
				}
				catch (SocketTimeoutException e)
				{
				}
			}

			serversocket.close();
			control.setStatus("Serversocket geschlossen");

			for (ClientProxy c : clientliste)
			{
				c.beende();
			}

			control.setStatus("Alle Clientproxies beendet");

		}
		catch (IOException e)
		{
			control.setStatus("Genereller I/O Fehler");
			e.printStackTrace();
		}
	}

	public void verarbeiteNachricht(String nachricht, ClientProxy absender)
	{
		// TODO: Nachricht nach Protokoll auswerten und weiterverarbeiten

		// Vorläufig: Als Satusnachricht an Control und an alle ClientProxies schicken
		control.setStatus(absender + ": " + nachricht);

		for (ClientProxy c : clientliste)
		{
			c.sende(absender + ": " + nachricht);
		}
	}

	public void setStatus(String status)
	{
		control.setStatus(status);
	}

	public int getPortnummer()
	{
		return portnummer;
	}
}