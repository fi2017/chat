package chatFPAUebung.gui.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.*;

import chatFPAUebung.fileHandler.FileHandlerBans;
import chatFPAUebung.interfaces.ServerRemoteControl;
import chatFPAUebung.klassen.*;
import chatFPAUebung.threads.ServerListenThread;
import chatFPAUebung.threads.ServerWritingThread;
import feature_LoginRegister.LogRegServerControl;
import feature_LoginRegister.User;

public class ServerControl implements ServerRemoteControl
{
	//private static final String dbURL = "jdbc:mariadb://172.16.5.55:3306/fi2017_chatdb_grp1?user=fi2017javaprojekt&password=fi2017";
	private static final String dbURL = "jdbc:mariadb://localhost:3306/fi2017_chatdb?user=root&password=";


	// Attribute
	private ServerGui gui;
	private LogRegServerControl loginServer = null;

	private Connection con;
	private PreparedStatement preparedStmt;
	@SuppressWarnings("FieldCanBeLocal")
	private String myDriver = "org.mariadb.jdbc.Driver";
	private ArrayList<ClientProxy> clients;
	private ArrayList<User> userList;
	private ServerListenThread serverListenThread;

	private ArrayList<Nachricht> nachrichten;
	private ArrayList<Chatroom> chatrooms = new ArrayList<Chatroom>();

	private ArrayList<Ban> bans;

	// Konstruktor
	public ServerControl()
	{
		this.clients = new ArrayList<>();
		this.userList = new ArrayList<>();
		this.gui = new ServerGui();
		try
		{
			Class.forName(myDriver);
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.nachrichten = new ArrayList<>();
		this.bans = new ArrayList<>(Arrays.asList((new FileHandlerBans()).readBans()));

		setzeListener();
		getGui().setVisible(true);
		//createTestenvironment();
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

			setLoginServer(new LogRegServerControl(this, clients));
			getLoginServer().start();
			readDatabase();
		}
		else
		{
			getGui().getLblFehlermeldung().setText("Der Server laeuft bereits!");
		}
	}

	//Datenbank zugriff mit User erstellen

	private void readDatabase()
	{
		try
		{
			con = DriverManager.getConnection(dbURL);
			//String for database connection
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select u.* from user u");
			while (rs.next())
			{
				mkUser(rs);
			}
			con.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Fehler beim Verbinden mit der Datenbank / Datenbank Fehler");
		}
	}

	private void mkUser(ResultSet rs)
	{
		User user = new User();
		try
		{
			user.setId(rs.getInt(1));
			user.setUsername(rs.getString(2));
			user.setPassword(rs.getString(3));
			user.setGlobalRollenNummer(rs.getInt(4));
			user.setBanned(rs.getDate(5).toLocalDate());
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		getUserList().add(user);
	}

	public void writeToDatabase() throws SQLException
	{
		try
		{
			con = DriverManager.getConnection(dbURL);
			String query = "INSERT INTO user (Username, Password, Role, AccountStatus) " +
					"VALUES (?,?,?,?)";
			preparedStmt = con.prepareStatement(query);

			for (User u : getUserList())
			{
				if (u.isNeu())
				{
					preparedStmt.setString(1, u.getUsername());
					preparedStmt.setString(2, u.getPassword());
					preparedStmt.setInt(3, u.getGlobalRollenNummer());
					preparedStmt.setDate(4, java.sql.Date.valueOf(u.isBanned()));
					preparedStmt.executeUpdate();
				}
			}


		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{

			if (preparedStmt != null)
			{
				preparedStmt.close();
			}

			if (con != null)
			{
				con.close();
			}
		}
	}

	public void stoppeServer()
	{
		if (getLoginServer() != null)
		{
			System.err.println("Der Server wurde gestoppt!");
			try
			{
				writeToDatabase();
			} catch (SQLException e1)
			{
				System.out.println("Error beim Schreiben in Datenbank");
				e1.printStackTrace();
			}
			System.out.println("Datenbank wurde beschrieben!");
			getGui().getLblFehlermeldung().setText("");

			getLoginServer().getListenThread().interrupt();
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
			getLoginServer().setListenThread(null);
			setLoginServer(null);

		}
		else
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
				}
				else
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
		}
		else
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
						if(!client.getUser().isInTimeout())
						{
							sendeNachrichtAnClient(new Uebertragung(1, getNachrichten().toArray(new Nachricht[0])), client);
						}

						break;

					case 2:
						if (uebertragung.getUebertragung() instanceof Nachricht)
						{
							getNachrichten().add((Nachricht) uebertragung.getUebertragung());
							broadcasteNachricht((Nachricht) uebertragung.getUebertragung(),uebertragung.getZiel);
						}

						break;

					case 3:
						sendeNachrichtAnClient(new Uebertragung(0, null), client);

						//TODO: Hier fehlt case 4: indem der erstellte Chatroom einer liste hinzugefügt wird.
						// Diese Liste wird einen neuen Benutzer beim einloggen dann gegeben, damit der den Chatrooms auch beitreten kann
						// Oder der Client sendet seine Eingabe (Name vom Chatroom) an den Server, der dann alles erledigt und nur den einen
						// zurück gibt.
						break;

						//Chatroom hinzufügen
					case 4:
						chatrooms.add((Chatroom) ((Uebertragung) uebertragungObjekt).getUebertragung());
						for(ClientProxy aktClient : getClients())
						{
							sendeNachrichtAnClient((Uebertragung) uebertragungObjekt, aktClient);
						}
						break;

					default:
						//
						break;
				}
			}
			else
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

	public void broadcasteNachricht(Nachricht nachricht, int ziel)
	{
		for (ClientProxy aktClient : chatrooms[ziel].getTeilnehmer())
		{
			sendeNachrichtAnClient(new Uebertragung(2, ziel, nachricht), aktClient);
		}
	}

	public void sendeNachrichtAnClient(Uebertragung uebertragung, ClientProxy client)
	{
		new ServerWritingThread(uebertragung, client, this).start();
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

	@Override
	public void shutdownServerNow()
	{

	}

	@Override
	public void removeAdmin(AdminProxy adminProxy)
	{
		loginServer.getAdminProxies().remove(adminProxy);
	}

	@Override
	public void refreshUser(AdminProxy admin)
	{
		ArrayList<UserDisplay> userDisplays = new ArrayList<>();
		for (User value : userList)
		{
			userDisplays.add(new UserDisplay(value.getUsername(),value.getStatus()));
		}
	}

	@Override
	public void setTimeout(TimeoutData data)
	{

	}

	@Override
	public void addAdmin(AdminProxy admin)
	{

	}

	@Override
	public void addMessage(String message)
	{

	}
}
