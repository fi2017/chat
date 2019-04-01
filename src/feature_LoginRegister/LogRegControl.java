package feature_LoginRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

import chatFPAUebung.gui.client.ClientControl;
import chatFPAUebung.klassen.Nachricht;
import chatFPAUebung.klassen.Uebertragung;
import chatFPAUebung.threads.ClientReadingThread;
import chatFPAUebung.threads.ClientWritingThread;

public class LogRegControl
{
	private LogRegGui gui;
	private Socket clientSocket;
	private ObjectOutputStream outToServer;
	private ObjectInputStream inFromServer;
	private LogRegReadingThread ReadingThread;
	
	public static void main(String[] args)
	{
		new LogRegControl(new LogRegGui());
	}
	
	public LogRegControl(LogRegGui g)
	{
		setGui(g);
		g.setVisible(true);
		addActionListenersToGuiObjects();
	}
	


	public void erstelleVerbindung()
	{
		try
		{
			setClientSocket(new Socket("localhost", 8008));
			setOutToServer(new ObjectOutputStream(getClientSocket().getOutputStream()));
			setInFromServer(new ObjectInputStream(getClientSocket().getInputStream()));

			setReadingThread(new LogRegReadingThread(this));
			getReadingThread().start();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		sendeNachrichtAnServer(new Uebertragung(1, null));
	}
	public void empfangeNachrichtVonServer(Object uebertragungObjekt)
	{
		if (uebertragungObjekt instanceof Uebertragung)
		{
			Uebertragung uebertragung = (Uebertragung) uebertragungObjekt;

			switch (((Uebertragung) uebertragungObjekt).getZweck())
			{
			case 1:
				
				break;

			case 2:
				
				break;

			case 3:
				
			default:
				//
				break;
			}
		}
	}

	public void sendeNachrichtAnServer(Uebertragung uebertragung)
	{
		(new ClientWritingThread(uebertragung, getOutToServer())).run();
	}	
	
	private void addActionListenersToGuiObjects()
	{
		getGui().getBtnLogin().addActionListener(e->loginUser());
		getGui().getBtnReg().addActionListener(e->registerUser());
	}

	private void loginUser()
	{
		//wenn login erfolgreich
		ClientControl control= new ClientControl();
		control.setClientSocket(clientSocket);
		control.setInFromServer(inFromServer);
		control.setOutToServer(outToServer);
		control.setClientReadingThread(new ClientReadingThread(control));
	}

	private void registerUser()
	{
		if(checkPasswordWith2nd() || !checkEmptyReg())
		{
			sendeNachrichtAnServer(new Uebertragung(2, new LogRegNachricht(70,getGui().getTextFieldUnameReg().getText(),getGui().getTextFieldPwReg().getText())));
		}
		else
		{
			// Später in der Gui als Label Anzeige einfügen
			System.out.println("Passwörter stimmen nicht überein!");
		}
	}
	

	private boolean checkEmptyLog()
	{
		boolean returnValue=false;
		
		if(getGui().getTextFieldPwLog().getText().trim().length() != 0 || getGui().getTextFieldUnameLog().getText().trim().length() != 0)
		{
			returnValue = true;
		}
		
		return returnValue;
	}
	private boolean checkEmptyReg()
	{
		boolean returnValue=false;
		
		if(getGui().getTextFieldPwReg().getText().trim().length() != 0 || getGui().getTextFieldUnameReg().getText().trim().length() != 0)
		{
			returnValue = true;
		}
		
		return returnValue;
	}

	private void schickeNachricht()
	{
		//Code um an Server zu schicken
	}

	private boolean checkPasswordWith2nd()
	{
		boolean returnValue=false;
		
		if(getGui().getTextFieldPwReg().equals(getGui().getTextFieldPwRepeat()))
		{
			returnValue = true;
		}
		
		return returnValue;
	}

	//GETTER SETTER
	
	
	public LogRegGui getGui()
	{
		return gui;
	}

	public void setGui(LogRegGui gui)
	{
		this.gui = gui;
	}

	public ObjectOutputStream getOutToServer()
	{
		return outToServer;
	}

	public void setOutToServer(ObjectOutputStream outToServer)
	{
		this.outToServer = outToServer;
	}

	public ObjectInputStream getInFromServer()
	{
		return inFromServer;
	}

	public void setInFromServer(ObjectInputStream inFromServer)
	{
		this.inFromServer = inFromServer;
	}

	public Socket getClientSocket()
	{
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}

	public LogRegReadingThread getReadingThread()
	{
		return ReadingThread;
	}

	public void setReadingThread(LogRegReadingThread readingThread)
	{
		ReadingThread = readingThread;
	}

}
