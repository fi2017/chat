package feature_LoginRegister;

import java.util.ArrayList;

import chatFPAUebung.gui.server.ServerControl;
import chatFPAUebung.klassen.ClientProxy;
import chatFPAUebung.klassen.Nachricht;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.threads.ServerListenThread;
import chatFPAUebung.threads.ServerReadingThread;
import chatFPAUebung.threads.ServerWritingThread;

public class LogRegServerControl extends Thread
{
	//nimmt Connections an (ServerListenThread) und überprüft Logindaten in der Datenbank
	//Wenn Daten übereinstimmen, wird der Client an die ServerControl übergeben
	//Ansonsten wird die Verbindung nach einer Fehlermeldung an den Client wieder geschlossen (vllt. nach x Versuchen?)
	//Registrierung läuft analog ab
	
	private ServerControl control;
	private ServerReadingThread readingThread;
	private ServerListenThread listenThread;
	private ArrayList<ClientProxy> clients;
	
	
	
	public LogRegServerControl(ServerControl control)
	{
		
		this.control = control;
		setListenThread(new ServerListenThread(this));
		getListenThread().start();
		
	}
	
	public void run()
	{
		
	}
	public void empfangeClient(ClientProxy neuerClient)
	{
		getClients().add(neuerClient);
	}
	
	public void empfangeNachrichtVonClient(Object uebertragungObjekt, ClientProxy client)
	{
		if (uebertragungObjekt instanceof Uebertragung)
		{
			Uebertragung uebertragung = (Uebertragung) uebertragungObjekt;

			switch (((Uebertragung) uebertragungObjekt).getZweck())
			{
				
			case 10: 				

				break;

			case 11:
				

				break;

				

			default: sendeNachrichtAnClient(new Uebertragung(8,"Uebertragungsfehler"),client);
				//
				break;
			}
		}
	}

	public void sendeNachrichtAnClient(Uebertragung uebertragung, ClientProxy client)
	{
		(new ServerWritingThread(uebertragung, client)).start();
	}

	
	//Getter / Setter
	public ServerControl getControl()
	{
		return control;
	}
	public void setControl(ServerControl control)
	{
		this.control = control;
	}
	public ServerReadingThread getReadingThread()
	{
		return readingThread;
	}
	public void setReadingThread(ServerReadingThread readingThread)
	{
		this.readingThread = readingThread;
	}
	public ServerListenThread getListenThread()
	{
		return listenThread;
	}
	public void setListenThread(ServerListenThread listenThread)
	{
		this.listenThread = listenThread;
	}

	public ArrayList<ClientProxy> getClients()
	{
		return clients;
	}

	public void setClients(ArrayList<ClientProxy> clients)
	{
		this.clients = clients;
	}
	
	
	
	 
}
