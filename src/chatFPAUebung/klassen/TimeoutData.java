package klassen;
/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */


import java.io.Serializable;

public class TimeoutData implements Serializable
{
	private int min;
	private String user;

	public TimeoutData(int min, String user)
	{
		this.min = min;
		this.user = user;
	}

	public int getMin()
	{
		return min;
	}

	public String getUser()
	{
		return user;
	}
}
