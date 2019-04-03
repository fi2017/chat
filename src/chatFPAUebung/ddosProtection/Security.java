package chatFPAUebung.ddosProtection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import chatFPAUebung.klassen.Ban;
import chatFPAUebung.klassen.ClientProxy;

public class Security
{
	// Finals
	private static final int banTimeObviousBehavior = 1;
	private static final int banTimeStrikingBehavior = 7;

	// Attributes
	private ClientProxy client;
	private SecuritySafe securitySafe; // Indicates Bots 100% accurate
	private SecurityUnsafe securityUnsafe; // Indicates Bots not 100% accurate

	// Constructor
	public Security(ClientProxy client)
	{
		this.client = client;
		this.securitySafe = new SecuritySafe();
		this.securityUnsafe = new SecurityUnsafe();
	}

	// Methods
	public Ban addNewPing(LocalDateTime messageSendingTime)
	{
		if (getSecuritySafe().addNewPing(messageSendingTime))
		{
			if (getSecurityUnsafe().addNewPing(messageSendingTime))
			{
				return null;
			} else
			{
				return new Ban(messageSendingTime, messageSendingTime.plusDays(banTimeStrikingBehavior),
						getClient().getClientSocket().getInetAddress(),
						"Banned for striking behavior till " + messageSendingTime.plusDays(banTimeStrikingBehavior)
								.format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH.mm.ss")));
			}
		} else
		{
			return new Ban(messageSendingTime, messageSendingTime.plusDays(banTimeObviousBehavior),
					getClient().getClientSocket().getInetAddress(),
					"Banned for obvious behavior till " + messageSendingTime.plusDays(banTimeObviousBehavior)
							.format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH.mm.ss")));
		}
	}

	// Getter
	public ClientProxy getClient()
	{
		return client;
	}

	public SecuritySafe getSecuritySafe()
	{
		return securitySafe;
	}

	public SecurityUnsafe getSecurityUnsafe()
	{
		return securityUnsafe;
	}
}
