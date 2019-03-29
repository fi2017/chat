package chatFPAUebung.ddosProtection;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class SecuritySafe
{
	// Finals
	private static final int pingsPerSecond = 7;

	// Attributes
	private ArrayList<LocalDateTime> pings;

	// Constructor
	public SecuritySafe()
	{
		this.pings = new ArrayList<LocalDateTime>();
	}

	// Methods
	public boolean addNewPing(LocalDateTime messageSendingTime)
	{
		if (getPings().size() == pingsPerSecond)
		{
			getPings().remove(0);
		}

		getPings().add(messageSendingTime);

		if (checkTooManyPings())
		{
			if (checkEvenlyPings())
			{
				return false;
			} else
			{
				return true;
			}
		} else
		{
			return true;
		}
	}

	public boolean checkTooManyPings() // Checks if the user pinged the server too often in 1s
	{
		if (getPings().size() == pingsPerSecond)
		{
			LocalDateTime newestPing = getPings().get(pingsPerSecond - 1);
			LocalDateTime oldestPing = getPings().get(0);

			if (ChronoUnit.MILLIS.between(newestPing, oldestPing) < 1000)
			{
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}
	}

	public boolean checkEvenlyPings() // Checks if the pings from the user got the exact same time difference
	{
		if (getPings().size() == pingsPerSecond)
		{
			boolean isEvenly = true;
			long differenceFirstTwo = getDifferenceFromTimes(getPings().get(0), getPings().get(1));

			for (int i = 1; i < getPings().size() - 1 && isEvenly; i++)
			{
				if (getDifferenceFromTimes(getPings().get(i), getPings().get(i + 1)) != differenceFirstTwo)
				{
					isEvenly = false;
				}
			}

			return isEvenly;
		} else
		{
			return false;
		}
	}

	private long getDifferenceFromTimes(LocalDateTime ping1, LocalDateTime ping2)
	{
		return ChronoUnit.MILLIS.between(ping1, ping2);
	}

	// Getter
	public ArrayList<LocalDateTime> getPings()
	{
		return pings;
	}
}