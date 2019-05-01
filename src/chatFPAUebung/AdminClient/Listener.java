package chatFPAUebung.AdminClient;

/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */

import chatFPAUebung.klassen.ChatRoomDisplay;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.klassen.UserDisplay;
import javafx.application.Platform;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.ArrayList;

class Listener implements Runnable
{
	private final Controller controller;
	private final ObjectInputStream in;

	Listener(ObjectInputStream in, Controller controller)
	{
		this.in = in;
		this.controller = controller;
	}

	@Override
	public void run()
	{
		try
		{
			Object temp;
			while ((temp = in.readObject()) != null)
			{
				Uebertragung message = (Uebertragung) temp;
				switch (message.getZweck())
				{
					case 1:
						//noinspection unchecked
						Platform.runLater(() -> controller.refreshUser((ArrayList<UserDisplay>) message.getUebertragung()));
						break;
					case 2:
						//noinspection unchecked
						Platform.runLater(() -> controller.refreshChatRooms((ArrayList<ChatRoomDisplay>) message.getUebertragung()));
						break;
				}
			}
		}
		catch (EOFException | InterruptedIOException e)
		{
			//interrupted
		}
		catch (SocketException e)
		{
			e.printStackTrace();
			Platform.runLater(controller::connectionLost);
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
