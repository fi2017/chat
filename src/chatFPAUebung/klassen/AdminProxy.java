package chatFPAUebung.klassen;

/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */

import chatFPAUebung.interfaces.ServerRemoteControl;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class AdminProxy extends Thread
{
	private final Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private ServerRemoteControl controller;

	public AdminProxy(Socket socket, ObjectInputStream in, ObjectOutputStream out, ServerRemoteControl controller)
	{
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.controller = controller;
	}

	@Override
	public void run()
	{
		Uebertragung temp;

		while (!isInterrupted())
		{
			try
			{
				temp = (Uebertragung) in.readObject();

				switch (temp.getZweck())
				{
					case 0:
						break;
					case 1:
						controller.refreshUser(this);
						break;
					case 2:
						controller.setTimeout((TimeoutData) temp.getUebertragung());
						break;
					case 6:
						controller.shutdownServerNow();
						break;
				}
			}
			catch (InterruptedIOException | SocketException e)
			{
				interrupt();
			}
			catch (IOException | ClassNotFoundException e)
			{
				interrupt();
				e.printStackTrace();
			}
		}

		Platform.runLater(() -> controller.addMessage("Connection to Admin Lost: " + socket.getLocalSocketAddress()));
		controller.removeAdmin(this);

		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	ObjectOutputStream getOut()
	{
		return out;
	}
}
