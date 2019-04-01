package klassen;

import java.io.Serializable;

/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */

public class UserDisplay implements Serializable
{
	private String status,username;

	public UserDisplay(String status, String username)
	{
		this.status = status;
		this.username = username;
	}

	@Override
	public String toString()
	{
		if(status.equals(""))
		{
			return username;
		}
		else
		{
			return username+" ("+status+")";
		}
	}

	public String getUsername()
	{
		return username;
	}
}
