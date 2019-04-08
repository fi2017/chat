package chatFPAUebung.threads;

import java.io.IOException;
import java.time.LocalDateTime;

import chatFPAUebung.gui.server.ServerControl;
import chatFPAUebung.klassen.Nachricht;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.klassen.ClientProxy;

public class ServerMaintenanceThread extends Thread
{

	private int seconds;
	private Uebertragung uebertragung;
	private ServerControl control;
	
	public ServerMaintenanceThread(int seconds, ServerControl control)
	{
		this.seconds = seconds;
		this.control = control;
		
		this.setName("ServerMaintananceThread");
	}



	public void run()
	{
		try
		{			
			control.broadcasteNachricht(new Nachricht("Serverwartung! Herunterfahren in "+seconds/1000+" Sekunden.", LocalDateTime.now()));
			Thread.sleep(seconds);

			
			
			
			
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ServerControl getControl()
	{
		return control;
	}
}
