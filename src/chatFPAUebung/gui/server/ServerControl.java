package chatFPAUebung.gui.server;

import java.io.IOException;
import java.util.ArrayList;

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

	// Konstruktor
	public ServerControl()
	{
		this.clients = new ArrayList<ClientProxy>();
		this.gui = new ServerGui();

		this.nachrichten = new ArrayList<Nachricht>();

		setzeListener();
		getGui().setVisible(true);
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
			getGui().getLblFehlermeldung().setText("");

			setLoginServer(new LogRegServerControl(this));
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
		getClients().add(neuerClient);
	}

	public void empfangeNachrichtVonClient(Object uebertragungObjekt, ClientProxy client)
	{
		if (uebertragungObjekt instanceof Uebertragung)
		{
			Uebertragung uebertragung = (Uebertragung) uebertragungObjekt;

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
		(new ServerWritingThread(uebertragung, client)).start();
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
