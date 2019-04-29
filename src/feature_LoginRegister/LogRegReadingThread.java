package feature_LoginRegister;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import chatFPAUebung.gui.client.clientMain.ClientControl;
import chatFPAUebung.threads.ClientReadingThread;

public class LogRegReadingThread extends Thread
{

	// Attribute
		private LogRegControl control;

		// Main
		public LogRegReadingThread(LogRegControl control)
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
				} catch (EOFException e)
				{
					this.interrupt();
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
		public LogRegControl getControl()
		{
			return this.control;
		}
	
 
}
