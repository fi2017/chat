package chatFPAUebung.gui.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import chatFPAUebung.fileHandler.FileHandlerBans;
import chatFPAUebung.klassen.Ban;
import chatFPAUebung.klassen.ClientProxy;
import chatFPAUebung.klassen.Nachricht;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.threads.ServerListenThread;
import chatFPAUebung.threads.ServerWritingThread;
import feature_LoginRegister.LogRegServerControl;
import feature_LoginRegister.User;

public class ServerControl
{
	// Attribute
	private ServerGui gui;
	private LogRegServerControl loginServer;

	private ArrayList<ClientProxy> clients;
	private ArrayList<User> userList;
	private ServerListenThread serverListenThread;

	private ArrayList<Nachricht> nachrichten;

	private ArrayList<Ban> bans;

	// Konstruktor
	public ServerControl()
	{
		this.clients = new ArrayList<ClientProxy>();
		this.gui = new ServerGui();

		this.nachrichten = new ArrayList<Nachricht>();
		this.bans = new ArrayList<Ban>(Arrays.asList((new FileHandlerBans()).readBans()));

		setzeListener();
		getGui().setVisible(true);
		createTestenvironment();
	}

	// Main
	public static void main(String[] args)
	{
		new ServerControl();
	}

	// Methoden
	public void setzeListener()
	{
		getGui().getBtnStart().addActionListener(e -> starteServer());
		getGui().getBtnStop().addActionListener(e -> stoppeServer());
	}

public void starteServer()
	{
		if (getLoginServer() == null)
		{
			System.err.println("Der Server wurde auf Port 8008 gestartet!");
			getGui().getLblFehlermeldung().setText("");

			setLoginServer(new LogRegServerControl(this,clients));
			getLoginServer().start();
		} else
		{
			getGui().getLblFehlermeldung().setText("Der Server laeuft bereits!");
		}
	}

	public void stoppeServer()
	{
		if (getServerListenThread() != null)
		{
			System.err.println("Der Server wurde gestoppt!");
			getGui().getLblFehlermeldung().setText("");

			getServerListenThread().interrupt();
			for (ClientProxy aktClient : getClients())
			{
				try
				{
					aktClient.getServerReadingThread().interrupt();
					aktClient.getInFromClient().close();
					aktClient.getOutToClient().close();
					aktClient.getClientSocket().close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			getClients().clear();
			setServerListenThread(null);

		} else
		{
			getGui().getLblFehlermeldung().setText("Der Server laeuft noch nicht!");
		}
	}

	public void empfangeClient(ClientProxy neuerClient)
	{
		boolean isBanned = false;

		for (int i = 0; i < getBans().size() && !isBanned; i++)
		{
			if (neuerClient.getClientSocket().getInetAddress().equals(getBans().get(i).getInternetAddress()))
			{
				if (!getBans().get(i).checkIfStillBanned())
				{
					getBans().remove(i);
					i--;
				} else
				{
					try
					{
						isBanned = true;
						neuerClient.getServerReadingThread().interrupt();
						neuerClient.getInFromClient().close();
						neuerClient.getOutToClient().close();
						neuerClient.getClientSocket().close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		if (!isBanned)
		{
			getClients().add(neuerClient);
			System.out.println("Neuen User zur Liste hinzugefügt!");
		} else
		{
			System.err.println("Gebannter User wurde gekillt");
		}
	}

	public void empfangeNachrichtVonClient(Object uebertragungObjekt, ClientProxy client)
	{
		if (uebertragungObjekt instanceof Uebertragung)
		{
			Uebertragung uebertragung = new Uebertragung((Uebertragung) uebertragungObjekt);
			Ban newBan = client.getClientSecurity().addNewPing(uebertragung.getUebertragungszeitpunkt());

			if (newBan == null)
			{
				System.out.println("Habe Nachricht vom User erhalten!");
				switch (((Uebertragung) uebertragungObjekt).getZweck())
				{
				case 1:
					sendeNachrichtAnClient(new Uebertragung(1, getNachrichten().toArray(new Nachricht[0])), client);

					break;

				case 2:
					if (uebertragung.getUebertragung() instanceof Nachricht)
					{
						getNachrichten().add((Nachricht) uebertragung.getUebertragung());
						broadcasteNachricht((Nachricht) uebertragung.getUebertragung());
					}

					break;

				case 3:
					sendeNachrichtAnClient(new Uebertragung(0, null), client);

				default:
					//
					break;
				}
			} else
			{
				System.out.println("Habe User gebannt!");
				getBans().add(newBan);
				removeUser(client);

				(new FileHandlerBans()).writeBans(getBans().toArray(new Ban[0]));

				// TODO: SEND MESSAGE TO USER THAT HE IS BANNED AND DISPLAY IT
			}
		}
	}

	public void removeUser(ClientProxy client)
	{
		System.err.println("\nDer Client mit der IP " + client.getClientSocket().getInetAddress() + " wurde entfernt!");

		try
		{
			client.getServerReadingThread().interrupt();
			client.getInFromClient().close();
			client.getOutToClient().close();
			client.getClientSocket().close();

			getClients().remove(client);

		} catch (IOException e)
		{
			getClients().remove(client);
		}
	}

	public void broadcasteNachricht(Nachricht nachricht)
	{
		for (ClientProxy aktClient : getClients())
		{
			sendeNachrichtAnClient(new Uebertragung(2, nachricht), aktClient);
		}
	}

	public void sendeNachrichtAnClient(Uebertragung uebertragung, ClientProxy client)
	{
		(new ServerWritingThread(uebertragung, client, this)).start();
	}
	
	public void createTestenvironment()
	{
		this.userList = new ArrayList<User>();
		User u = new User("Richard","123",1,0);
		u.setBanned(true);
		getUserList().add(u);
		getUserList().add(new User("Lukas","1234",2,1));
		getUserList().add(new User("Joshua","1235",3,1));
		getUserList().add(new User("Reis","1236",4,1));
	}
	
	//Datenbankzugriff
	public void connectToDatabase()
	{
		//connection
		
		
		readUserFromDatabase();
	}
	

	private void readUserFromDatabase()
	{
		
	}

	// Getter
	public ServerGui getGui()
	{
		return this.gui;
	}

	public ArrayList<Nachricht> getNachrichten()
	{
		return this.nachrichten;
	}

	public ArrayList<ClientProxy> getClients()
	{
		return clients;
	}

	public ArrayList<Ban> getBans()
	{
		return bans;
	}

	public ServerListenThread getServerListenThread()
	{
		return serverListenThread;
	}

	public void setGui(ServerGui gui)
	{
		this.gui = gui;
	}

	public void setClients(ArrayList<ClientProxy> clients)
	{
		this.clients = clients;
	}

	public void setServerListenThread(ServerListenThread serverListenThread)
	{
		this.serverListenThread = serverListenThread;
	}

	public LogRegServerControl getLoginServer()
	{
		return loginServer;
	}

	public void setLoginServer(LogRegServerControl login)
	{
		this.loginServer = login;
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
