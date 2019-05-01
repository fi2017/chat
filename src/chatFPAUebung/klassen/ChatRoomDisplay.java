package chatFPAUebung.klassen;

/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */

import java.io.Serializable;

public class ChatRoomDisplay implements Serializable
{
	private int chatRoomId;
	private String chatName;

	public ChatRoomDisplay(int chatRoomId, String chatName)
	{
		this.chatRoomId = chatRoomId;
		this.chatName = chatName;
	}

	public int getChatRoomId()
	{
		return chatRoomId;
	}

	@Override
	public String toString()
	{
		return chatRoomId+": "+chatName;
	}
}
