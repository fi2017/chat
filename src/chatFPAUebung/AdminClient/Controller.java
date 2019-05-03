package chatFPAUebung.AdminClient;

/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */

import chatFPAUebung.fxPopup.Confirm;
import chatFPAUebung.fxPopup.Input;
import chatFPAUebung.klassen.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
	@FXML
	private Button btnConnect;

	@FXML
	private Button btnSend;

	@FXML
	private TextField textFieldIP;

	@FXML
	private TextField textFieldPort;

	@FXML
	private ComboBox<ActionString> listActions;

	@FXML
	private ComboBox<ChatRoomDisplay> listChatRoom;

	@FXML
	private ListView<UserDisplay> listUser;

	@FXML
	private ListView<String> listMessages;

	private ObjectOutputStream out;
	private Thread listenerThread;
	private Thread refreshThread;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		//adding the Options to the ComboBox
		//listActions.getItems().add(new ActionString(1, "Refresh"));
		listActions.getItems().add(new ActionString(2, "Set in Timeout (1 min)"));
		listActions.getItems().add(new ActionString(4, "Ban user form Server"));
		listActions.getItems().add(new ActionString(14, "Whitelist user form Server"));
		listActions.getItems().add(new ActionString(5, "Ban user form ChatRoom"));
		listActions.getItems().add(new ActionString(15, "Whitelist user form ChatRoom"));
		listActions.getItems().add(new ActionString(6, "Shutdown Server"));
		listActions.getItems().add(new ActionString(8, "Ärgere clients"));


		//select the first action
		listActions.getSelectionModel().select(0);

		//set the buttons
		btnConnect.setOnAction((e) -> new Thread(this::connect).start());
		btnSend.setDisable(true);
		btnSend.setOnAction((e) -> send(listActions.getSelectionModel().getSelectedItem().getAction()));
	}

	//sends a action with the needed data to the server
	private void send(int action)
	{
		boolean send = true;
		try
		{
			Object temp = null; //some actions don't need data to be sen with them
			switch (action)
			{
				case 2:
					send = false;
					Input popupInput = new Input();
					popupInput.setInfo("Timeout time (min):");
					popupInput.getBtnOk().setOnAction(e ->
					{
						try
						{
							int min = Integer.parseInt(popupInput.getInput().getText());
							out.writeObject(new Uebertragung(6, new TimeoutData(min, listUser.getSelectionModel().getSelectedItem().getUsername())));
							out.flush();
						} catch (Exception ex)
						{
							addMessage(ex.getMessage());
						}

						popupInput.close();
					});
					popupInput.show();
					break;
				case 4:
					temp = new BanData(listUser.getSelectionModel().getSelectedItem().getUsername());
					break;
				case 5:
					temp = new BanData(listChatRoom.getSelectionModel().getSelectedItem().getChatRoomId(), listUser.getSelectionModel().getSelectedItem().getUsername());
					break;
				case 6:
					send = false;
					configShutdown();
					break;
			}

			if (send)
			{
				out.writeObject(new Uebertragung(action, temp));
				out.flush();
			}
		} catch (SocketException e)
		{
			connectionLost();
		} catch (IOException e)
		{
			//e.printStackTrace(); //for debugging
			addMessage("ERROR: " + e.getMessage()); //to display the ERROR to the user
		}
	}

	private void configShutdown()
	{
		Confirm popup = new Confirm();
		popup.getBtnOk().setOnAction(e ->
		{
			try
			{
				out.writeObject(new Uebertragung(7, null));
				out.flush();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			popup.close();
		});
		popup.setInfo("Are zou sure:");
		popup.show();
	}

	//connects the AdminClient to the server
	private void connect()
	{
		try
		{
			int port = Integer.parseInt(textFieldPort.getText());

			Socket socket = new Socket(textFieldIP.getText(), port);
			out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			Listener listener = new Listener(in, this);
			listenerThread = new Thread(listener);
			listenerThread.start();

			//logs the admin in so that the server will see it as a Admin
			login();

			Platform.runLater(() -> btnSend.setDisable(false));

			refreshThread = new Thread(this::automaticRefresh);
			refreshThread.start();
		} catch (SocketException e)
		{
			Platform.runLater(() ->
			{
				addMessage("Connection failed");
				addMessage("ERROR: " + e.getMessage()); //to display the ERROR to the user
			});

		} catch (Exception e)
		{
			Platform.runLater(() ->
			{
				addMessage("Invalid Input");
				addMessage("ERROR: " + e.getMessage()); //to display the ERROR to the user
			});
		}
	}

	//sends a password
	private void login()
	{
		send(5503789);
	}

	//applies the new UserList to its display list
	void refreshUser(ArrayList<UserDisplay> temp)
	{
		int lastSelection = listUser.getSelectionModel().getSelectedIndex();

		listUser.getItems().clear();
		listUser.getItems().addAll(temp);
		listUser.getSelectionModel().select(lastSelection);
	}

	//adds a debug/error message
	private void addMessage(String temp)
	{
		listMessages.getItems().add(temp);
		listMessages.scrollTo(listUser.getItems().size() - 1);
	}

	void connectionLost()
	{
		refreshThread.interrupt();
		Platform.runLater(() -> addMessage("Connection Lost"));
		btnSend.setDisable(true);
	}

	// ends the Listener for a clean exit
	void exit()
	{
		if (listenerThread != null)
		{
			listenerThread.interrupt();
		}
	}

	//displays the chatRooms that wer send from the server
	void refreshChatRooms(ArrayList<ChatRoomDisplay> uebertragung)
	{
		listChatRoom.getItems().clear();
		listChatRoom.getItems().setAll(uebertragung);
		listChatRoom.getSelectionModel().select(0);
	}

	private void automaticRefresh()
	{
		while (!Thread.currentThread().isInterrupted())
		{
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				//normal exit
			}
			send(1);
		}
	}
}
