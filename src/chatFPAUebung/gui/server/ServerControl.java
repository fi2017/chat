package chatFPAUebung.gui.server;

import java.io.IOException;
import java.util.ArrayList;

import chatFPAUebung.klassen.Ban;
import chatFPAUebung.klassen.ClientProxy;
import chatFPAUebung.klassen.Nachricht;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.threads.ServerListenThread;
import chatFPAUebung.threads.ServerWritingThread;

public class ServerControl
{
	// Attribute
	private ServerGui gui;

	private ArrayList<ClientProxy> clients;
	private ServerListenThread serverListenThread;

	private ArrayList<Nachricht> nachrichten;

	private ArrayList<Ban> bans;

	// Konstruktor
	public ServerControl()
	{
		this.clients = new ArrayList<ClientProxy>();
		this.gui = new ServerGui();

		this.nachrichten = new ArrayList<Nachricht>();
		this.bans = new ArrayList<Ban>(); // TODO: Needs to be saved in a file later (Not right now as we ban ourselves
											// for test purposes

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
		if (getServerListenThread() == null)
		{
			getGui().getLblFehlermeldung().setText("");

			setServerListenThread(new ServerListenThread(this));
			getServerListenThread().start();
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
		boolean isBanned = false;

		for (int i = 0; i < getBans().size() && !isBanned; i++)
		{
			if (neuerClient.getClientSocket().getInetAddress().equals(getBans().get(i).getInternetAdress()))
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
			System.err.println("Neuen User zur Liste hinzugefügt!");
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

				// TODO: SEND MESSAGE TO USER THAT HE IS BANNED AND DISPLAY IT
			}
		}
	}

	public void removeUser(ClientProxy client)
	{
		try
		{
			client.getServerReadingThread().interrupt();
			client.getInFromClient().close();
			client.getOutToClient().close();
			client.getClientSocket().close();

			getClients().remove(client);

		} catch (IOException e)
		{
			e.printStackTrace();
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
}
