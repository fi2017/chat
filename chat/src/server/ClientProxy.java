package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProxy
{
	private Server server;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Thread listenThread;

	public ClientProxy(Socket socket, Server server) throws IOException
	{
		this.socket = socket;
		this.server = server;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true); // Autoflush == true

		listenThread = new Thread(() -> hoere());
		listenThread.start();
	}

	private void hoere()
	{
		server.setStatus("Listenthread gestartet für " + this);

		while (!listenThread.isInterrupted())
		{
			String nachricht;
			try
			{
				Thread.sleep(100);
				nachricht = in.readLine(); // Problem: blockiert, bis eine Zeile ankommt
				server.verarbeiteNachricht(nachricht, this);
			}
			catch (IOException e)
			{
				server.setStatus("I/O Fehler in " + this + ". Listen Thread wird gestoppt.");
				listenThread.interrupt();
			}
			catch (InterruptedException e)
			{
				server.setStatus("Listen Thread von " + this + " wird gestoppt.");
				listenThread.interrupt();
			}
		}
	}

	public void sende(String nachricht)
	{
		out.println(nachricht);
	}

	public void beende()
	{
		try
		{
			listenThread.interrupt();
			// TODO: Nachricht an Client schicken, dass Server beendet wird
			in.close();
			out.close();
			socket.close();
		}
		catch (IOException e)
		{
			server.setStatus("I/O-Fehler beim beenden von " + this);
			e.printStackTrace();
		}

		server.setStatus(this + " wurde beendet");
	}
}
