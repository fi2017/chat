package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Control
{
	Gui gui;
	Server server;

	public static void main(String[] args)
	{
		new Control();
	}

	public Control()
	{
		gui = new Gui();
		gui.setVisible(true);

		gui.addStartListener(e -> starteServer());
		gui.addStopListener(e -> stoppeServer());

	}

	public void setStatus(String status)
	{
		gui.setStatus(status);
	}

	private void starteServer()
	{
		if (server != null)
		{
			gui.setStatus("Es läuft bereits ein server auf Port " + server.getPortnummer());
		} 
		else
		{
			try
			{
				int portnummer = gui.getPortnummer();
				server = new Server(portnummer, this);
				server.starteServer();
			}
			catch (NumberFormatException e)
			{
				gui.setStatus("Bitte eine gültige Portnummer eingeben.");
			}
		}
	}

	private void stoppeServer()
	{
		if (server != null)
		{
			server.stoppeServer();
			server = null;
		}
	}
}
