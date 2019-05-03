package feature_LoginRegister;

import chatFPAUebung.gui.server.ServerControl;
import chatFPAUebung.klassen.AdminProxy;
import chatFPAUebung.klassen.ClientProxy;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.threads.ServerListenThread;
import chatFPAUebung.threads.ServerReadingThread;
import chatFPAUebung.threads.ServerWritingThread;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LogRegServerControl extends Thread
{
	//nimmt Connections an (ServerListenThread) und �berpr�ft Logindaten in der Datenbank
	//Wenn Daten �bereinstimmen, wird der Client an die ServerControl �bergeben
	//Ansonsten wird die Verbindung nach einer Fehlermeldung an den Client wieder geschlossen (vllt. nach x Versuchen?)
	//Registrierung l�uft analog ab

	private ServerControl control;
	private ServerReadingThread readingThread;
	private ServerListenThread listenThread;
	private ArrayList<ClientProxy> clients;
	private ArrayList<AdminProxy> adminProxies = new ArrayList<>();

	public LogRegServerControl(ServerControl control, ArrayList<ClientProxy> clients)
	{
		setControl(control);
		setClients(clients);

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
					switch(loginUser((LogRegNachricht)uebertragung.getUebertragung()))
					{
						case 1: sendeNachrichtAnClient(new Uebertragung(1,""),client);
							client.getServerReadingThread().setControl(control);
							client.getServerReadingThread().setLoginControl(null);
							for (User value : control.getUserList())
							{
								if(value.getUsername().equals(((LogRegNachricht) uebertragung.getUebertragung()).getUsername()))
								{
									client.setUser(value);
									break;
								}
							}

							try
							{
								control.refreshChatroom(client);
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}

							break;
						case 2: sendeNachrichtAnClient(new Uebertragung(2,""),client);
							break;
						case 3: sendeNachrichtAnClient(new Uebertragung(3,""),client);
							break;
						case 4: sendeNachrichtAnClient(new Uebertragung(4,""),client);
							break;
						case 5: sendeNachrichtAnClient(new Uebertragung(5,""),client);
							break;
						case 0: sendeNachrichtAnClient(new Uebertragung(8,""),client);


					}
					break;

				case 11: //Registrierungsversuch
					if(!registerUser((LogRegNachricht)uebertragung.getUebertragung()))
					{
						sendeNachrichtAnClient(new Uebertragung(7,""),client);
					}
					else
					{
						sendeNachrichtAnClient(new Uebertragung(6,""),client);
					}

					break;
				case 5503789: //login from admin
					getClients().remove(client);
					AdminProxy tempAdmin = new AdminProxy(client.getClientSocket(),client.getInFromClient(), client.getOutToClient(), control);
					tempAdmin.start();
					adminProxies.add(tempAdmin);
					break;



				default: sendeNachrichtAnClient(new Uebertragung(8,""),client);
					//
					break;
			}
		}
	}

	private boolean registerUser(LogRegNachricht uebertragung)
	{
		boolean userSchonVergeben = false;

		for(User u : control.getUserList())
		{
			if(u.getUsername().equals(uebertragung.username))
			{
				userSchonVergeben=true;
			}
		}
		if(userSchonVergeben==true)
		{
			return false;
		}
		else
		{
			User newUser=new User(uebertragung.getUsername(), uebertragung.getPassword() ,control.getUserList().get(control.getUserList().size()-1).getId()+1,1);
			control.getUserList().add(newUser);
			return true;
		}
		//�berpr�ft in der UserList zuerst, ob der Nutzername schon vergeben ist
		//wenn er noch nicht vergeben ist, wird ein neuer User in der Userlist erstellt
		//sonst wird die Fehlermeldung an den Client zur�ckgegeben, dass der Name schon vergeben ist



	}

	private int loginUser(LogRegNachricht uebertragung)
	{
		//�berpr�ft in der Datenbank zuerst, ob der Nutzername und Passwort �bereinstimmen
		//gibt Nachrichtenzweck zur�ck, je nachdem, ob erfolgreich oder fehler
		int returnValue=0;
		System.out.println(uebertragung.getUsername() + uebertragung.getPassword());
		for(User u : control.getUserList())
		{
			System.out.println(u.getUsername()+u.getPassword()+u.isBanned()+u.isOnline());
			if(u.getUsername().equals(uebertragung.getUsername()))
			{
				if(!u.isBanned().equals(LocalDateTime.now()))
				{
					if(!u.isOnline())
					{
						if(u.getPassword().equals(uebertragung.getPassword()))
						{
							System.out.println("Login erfolgreich");
							returnValue=1;
							u.setOnline(true);
							u.setNeu(true);
							break;
						}
						else
						{
							returnValue=2;
							break;
						}
					}
					else
					{
						returnValue=4;
						u.setOnline(false);
						break;
					}
				}
				else
				{
					System.out.println("gebannt");
					returnValue=5;
					break;
				}
			}
			else
			{
				returnValue=3;
			}
		}
		return returnValue;
	}

	public void sendeNachrichtAnClient(Uebertragung uebertragung, ClientProxy client)
	{
		(new ServerWritingThread(uebertragung, client, getControl())).start();
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


	public ArrayList<AdminProxy> getAdminProxies()
	{
		return adminProxies;
	}
}
