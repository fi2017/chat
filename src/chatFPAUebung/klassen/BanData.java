package klassen;
/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */


import java.io.Serializable;

//data needed for banning or unbanning a user form all or just one coatroom/s
public class BanData implements Serializable
{
	private int chatRoomId = -1; //-1==ban for all ChatRooms
	private String username;

	public BanData(String username)
	{
		this.username = username;
	}

	public BanData(int chatRoomId, String username)
	{
		this.chatRoomId = chatRoomId;
		this.username = username;
	}

	public int getChatRoomId()
	{
		return chatRoomId;
	}

	public String getUsername()
	{
		return username;
	}
}
