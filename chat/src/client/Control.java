package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Control
{
	private Gui gui;
	private Socket socket;

	private PrintWriter out;
	private BufferedReader in;
	private Thread listenThread;

	public static void main(String[] args)
	{
		new Control();
	}

	Control()
	{
		gui = new Gui();
		gui.setVisible(true);
		gui.addVerbindenListener(e -> verbinde());
		gui.addSendenListener(e -> sende());
	}

	private void verbinde()
	{
		if (socket != null)
		{
			gui.setStatus("Verbindung bereits hergestellt");
		}
		else
		{
			try
			{
				String host = gui.getHost();
				int portnummer = gui.getPortnummer();
	
				socket = new Socket(host, portnummer);
				gui.setStatus("Verbindung hergestellt");
	
				out = new PrintWriter(socket.getOutputStream(), true); // Autoflush == true
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
				listenThread = new Thread(() -> hoere());
				listenThread.start();
			}
			catch (UnknownHostException e)
			{
				gui.setStatus("Host unbekannt: " + e.getMessage());
			}
			catch (IOException e)
			{
				gui.setStatus("Genereller I/O-Fehler: " + e.getMessage());
			} 
			
		}
	}

	private void hoere()
	{
		gui.setStatus("Listenthread gestartet");
	
		while (!listenThread.isInterrupted())
		{
			String nachricht;
			try
			{
				Thread.sleep(100);
				nachricht = in.readLine(); // blockiert, bis eine zeile ankommt
	
				verarbeiteNachricht(nachricht);
			}
			catch (IOException e)
			{
				gui.setStatus("I/O Fehler in " + this + ". Listen Thread wird gestoppt.");
				listenThread.interrupt();
			}
			catch (InterruptedException e)
			{
				gui.setStatus("Listen Thread von " + this + " wird gestoppt.");
				listenThread.interrupt();
			}
		}
	}

	private void sende()
	{
		String nachricht = gui.getNachricht();

		try
		{
			out.println(nachricht);
		}
		catch (NullPointerException e)
		{
			gui.setStatus("Bitte zuerst Verbindung zum Server herstellen");
		}
	}

	private void verarbeiteNachricht(String nachricht)
	{
		// TODO: verarbeite Nachricht nach Protokoll
		// vorläufig: sende als Statusnachricht an GUI
		gui.setStatus(nachricht);
	}
}
