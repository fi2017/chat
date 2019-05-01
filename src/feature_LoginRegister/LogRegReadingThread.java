package feature_LoginRegister;

import chatFPAUebung.gui.client.loginMenu.startScene.Controller;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

public class LogRegReadingThread extends Thread
{

	// Attribute
		private Controller control;

		// Main
		public LogRegReadingThread(Controller control)
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
				} catch (EOFException | SocketException e)
				{
					this.interrupt();
				} catch (ClassNotFoundException | IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		// Getter
		public Controller getControl()
		{
			return this.control;
		}


}
