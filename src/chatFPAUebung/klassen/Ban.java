package chatFPAUebung.klassen;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;

public class Ban implements Serializable
{
	// Attributes
	private static final long serialVersionUID = 1L;

	private LocalDateTime banTime;
	private LocalDateTime unbanTime;
	private InetAddress internetAdress;
	private String banReason;

	// Constructor
	public Ban(LocalDateTime banTime, LocalDateTime unbanTime, InetAddress internetAdress, String banReason)
	{
		this.banTime = banTime;
		this.unbanTime = unbanTime;
		this.internetAdress = internetAdress;
		this.banReason = banReason;
	}

	// Methods
	public boolean checkIfStillBanned()
	{
		if (LocalDateTime.now().isBefore(getUnbanTime()))
		{
			return true;
		} else
		{
			return false;
		}
	}

	// Getter
	public LocalDateTime getBanTime()
	{
		return banTime;
	}

	public LocalDateTime getUnbanTime()
	{
		return unbanTime;
	}

	public InetAddress getInternetAdress()
	{
		return internetAdress;
	}

	public String getBanReason()
	{
		return banReason;
	}
}
