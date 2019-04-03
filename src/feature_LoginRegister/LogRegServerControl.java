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
	private ArrayList<User> userList;
	
	
	
	public LogRegServerControl(ServerControl control)
	{
		
		this.control = control;
		control.setUserList(getUserList());
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
				
			case 10: //Loginversuch				
					loginUser((LogRegNachricht)uebertragung.getUebertragung());
				break;

			case 11: //Registrierungsversuch
					registerUser((LogRegNachricht)uebertragung.getUebertragung());

				break;

				

			default: sendeNachrichtAnClient(new Uebertragung(8,"Uebertragungsfehler"),client);
				//
				break;
			}
		}
	}

	private boolean registerUser(LogRegNachricht uebertragung)
	{
		//überprüft in der Datenbank zuerst, ob der Nutzername schon vergeben ist
		//wenn er noch nicht vergeben ist, wird ein neuer Nutzer in der Datenbank angelegt
		//sonst wird die Fehlermeldung an den Client zurückgegeben, dass der Name schon vergeben ist 
		return false;
		
		
	}

	private int loginUser(LogRegNachricht uebertragung)
	{
		//überprüft in der Datenbank zuerst, ob der Nutzername und Passwort übereinstimmen
		//gibt Nachrichtenzweck zurück, je nachdem, ob erfolgreich oder fehler
		return 8;
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


	public ArrayList<User> getUserList()
	{
		return userList;
	}


	public void setUserList(ArrayList<User> userList)
	{
		this.userList = userList;
	}
	
	
	
	 
}
