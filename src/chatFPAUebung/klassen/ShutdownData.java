package klassen;

import java.io.Serializable;
import java.time.Instant;

public class ShutdownData implements Serializable
{
	private Instant shutdownTime;
	private String message;

	public ShutdownData(Instant shutdownTime, String message)
	{
		this.shutdownTime = shutdownTime;
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

	public Instant getShutdownTime()
	{
		return shutdownTime;
	}
}
