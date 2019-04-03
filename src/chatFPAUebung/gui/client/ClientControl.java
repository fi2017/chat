package chatFPAUebung.gui.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.activation.ActivationGroup_Stub;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.*;

import chatFPAUebung.klassen.Chatroom;
import chatFPAUebung.klassen.Nachricht;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.threads.ClientReadingThread;
import chatFPAUebung.threads.ClientWritingThread;

public class ClientControl
{
	// Attribute
	private ClientGui gui;
	private DefaultListModel<Nachricht> listModel;

	private Socket clientSocket;
	private ObjectOutputStream outToServer;
	private ObjectInputStream inFromServer;

	private ClientReadingThread clientReadingThread;

	private ArrayList<Chatroom> chatrooms = new ArrayList<Chatroom>();
	private ArrayList<DefaultListModel> listmodels = new ArrayList<DefaultListModel>();

	// Konstruktor
	public ClientControl()
	{
		this.gui = new ClientGui();
		this.listModel = new DefaultListModel<Nachricht>();

		setzeListener();
		erstelleVerbindung();

		getGui().setVisible(true);
	}

	// Main
	public static void main(String[] args)
	{
		new ClientControl();
	}

	// Methoden
	public void setzeListener()
	{
		getGui().getList().setModel(getListModel());
		getGui().getBtnSenden().addActionListener(e -> sendeNachricht());
	}

	public void erstelleVerbindung()
	{
		try
		{
			setClientSocket(new Socket("localhost", 8008));
			setOutToServer(new ObjectOutputStream(getClientSocket().getOutputStream()));
			setInFromServer(new ObjectInputStream(getClientSocket().getInputStream()));

			setClientReadingThread(new ClientReadingThread(this));
			getClientReadingThread().start();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		sendeNachrichtAnServer(new Uebertragung(1, null));
	}

	public void empfangeNachrichtVonServer(Object uebertragungObjekt)
	{
		if(uebertragungObjekt instanceof Uebertragung)
		{
			Uebertragung uebertragung = (Uebertragung) uebertragungObjekt;

			switch(((Uebertragung) uebertragungObjekt).getZweck())
			{
				case 1:
					if(uebertragung.getUebertragung() instanceof Nachricht[])
					{
						DefaultListModel aktuellesModel=listmodels.get(((Uebertragung) uebertragungObjekt).getZiel());

						if(((Nachricht[]) uebertragung.getUebertragung()).length != 0)
						{
							aktuellesModel.clear();

							for(Nachricht aktNachricht : (Nachricht[]) uebertragung.getUebertragung())
							{
								aktuellesModel.addElement(aktNachricht);
							}

							zeigeNeuesteNachricht();
						}
					}

					break;

				case 2:
					if(uebertragung.getUebertragung() instanceof Nachricht)
					{
						DefaultListModel aktuellesModel=listmodels.get(((Uebertragung) uebertragungObjekt).getZiel());
						aktuellesModel.addElement((Nachricht) uebertragung.getUebertragung());

						zeigeNeuesteNachricht();
					}

					break;

				case 3:
					sendeNachrichtAnServer(new Uebertragung(0, null));
					break;

				case 4:
					chatrooms.add((Chatroom) uebertragung.getUebertragung());

				default:
					//
					break;
			}
		}
	}

	public void zeigeNeuesteNachricht()
	{
		getGui().getList().ensureIndexIsVisible(getListModel().getSize());

		// Benoetigt um einen Anzeige-Bug zu fixen
		getGui().getList().setSelectedIndex(0);
		getGui().getList().setSelectedIndex(getListModel().getSize() - 1);
		getGui().getList().clearSelection();
	}

	public void sendeNachricht()
	{
		if(getGui().getTextFieldNachricht().getText().trim().length() != 0)
		{
			getGui().getLblFehlermeldung().setText("");
			sendeNachrichtAnServer(new Uebertragung(2, new Nachricht(getGui().getTextFieldNachricht().getText(), LocalDateTime.now())));
		}
		else
		{
			getGui().getLblFehlermeldung().setText("Sie muessen einen Text eingeben!");
		}
	}

	public void sendeNachrichtVonChatroom(ClientGui gui) //Methode für ChatroomGUI
	{
		if(getGui().getTextFieldNachricht().getText()!=null)
		{
			ListModel model = getGui().getList().getModel();
			int ziel=listmodels.indexOf(model);
			gui.getLblFehlermeldung().setText("");
			sendeNachrichtAnServer(new Uebertragung(2, ziel, new Nachricht(gui.getTextFieldNachricht().getText(), LocalDateTime.now())));
		}
	}


	//Chatrooms
	public void erstelleChatroom(String name, int teilnehmerAnzahl, String passwort)
	{
		Chatroom chat = new Chatroom(name, teilnehmerAnzahl);
		if(passwort != null)
		{
			chat.setPasswort(passwort);
		}
		try
		{
			DefaultListModel neuesListModel = new DefaultListModel();
			chat.setChatmodel(neuesListModel);
			Uebertragung neuerchatroom = new Uebertragung(4, chat);
			listmodels.add(neuesListModel);
			outToServer.writeObject(neuerchatroom);
			outToServer.flush();
		}
		catch(Exception e)
		{

		}


	}

	//Listmodel wechseln in Gui
	public void switchModel()
	{

	}


	//Chatrooms Ende

	public void sendeNachrichtAnServer(Uebertragung uebertragung)
	{
		(new ClientWritingThread(uebertragung, this)).run();
	}

	// Getter
	public ClientGui getGui()
	{
		return this.gui;
	}

	public Socket getClientSocket()
	{
		return clientSocket;
	}

	public ObjectOutputStream getOutToServer()
	{
		return outToServer;
	}

	public ObjectInputStream getInFromServer()
	{
		return inFromServer;
	}

	public void setClientSocket(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}

	public void setOutToServer(ObjectOutputStream outToServer)
	{
		this.outToServer = outToServer;
	}

	public void setInFromServer(ObjectInputStream inFromServer)
	{
		this.inFromServer = inFromServer;
	}

	public ClientReadingThread getClientReadingThread()
	{
		return clientReadingThread;
	}

	public void setClientReadingThread(ClientReadingThread clientReadingThread)
	{
		this.clientReadingThread = clientReadingThread;
	}

	public DefaultListModel<Nachricht> getListModel()
	{
		return this.listModel;
	}

	public void setListModel(DefaultListModel aktuellesModel)
	{
		this.listModel=aktuellesModel;
	}

	public ArrayList<Chatroom> getChatrooms()
	{
		return chatrooms;
	}

	public void setChatrooms(ArrayList<Chatroom> chatrooms)
	{
		this.chatrooms = chatrooms;
	}

	public ArrayList<DefaultListModel> getListmodels()
	{
		return listmodels;
	}

	public void setListmodels(ArrayList<DefaultListModel> listmodels)
	{
		this.listmodels = listmodels;
	}
}
