package chatFPAUebung.ddosProtection;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class SecuritySafe
{
	// Finals
	private static final int pingsPerSecond = 7; // Sets how many pings per seconds are allowed from a user

	// Attributes
	private ArrayList<LocalDateTime> pings; // Shows the sending time of all messages from the user to the server

	// Constructor
	public SecuritySafe()
	{
		this.pings = new ArrayList<LocalDateTime>();
	}

	// Methods
	public boolean addNewPing(LocalDateTime messageSendingTime) // Adds a new ping whenever the user sends a message to
																// the server
	{
		if (getPings().size() == pingsPerSecond) // If there are already enough pings, the new ones can overwrite the
													// old ones, because they are not needed anymore
		{
			getPings().remove(0);
		}

		getPings().add(messageSendingTime);

		if (!checkTooManyPings()) // Checks if the user pinged the server too often in 1s
		{
			if (!checkEvenlyPings()) // Checks if the pings from the user got the exact same time difference
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

	private boolean checkTooManyPings() // Checks if the user pinged the server too often in 1s
	{
		if (getPings().size() == pingsPerSecond) // If there aren't enough pings the user can't be banned anyways
		{
			LocalDateTime newestPing = getPings().get(pingsPerSecond - 1);
			LocalDateTime oldestPing = getPings().get(0);

			if (ChronoUnit.MILLIS.between(oldestPing, newestPing) < 1000)
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

	private boolean checkEvenlyPings() // Checks if the pings from the user got the exact same time difference
	{
		if (getPings().size() == pingsPerSecond)
		{
			boolean isEvenly = true;
			long differenceFirstTwo = getDifferenceFromTimesInMS(getPings().get(0), getPings().get(1));

			for (int i = 1; i < getPings().size() - 1 && isEvenly; i++)
			{
				if (getDifferenceFromTimesInMS(getPings().get(i), getPings().get(i + 1)) != differenceFirstTwo)
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

	private long getDifferenceFromTimesInMS(LocalDateTime ping1, LocalDateTime ping2) // Returns the difference between
																						// 2 LocalDateTimes in MS
	{
		return ChronoUnit.MILLIS.between(ping1, ping2);
	}

	// Getter
	public ArrayList<LocalDateTime> getPings()
	{
		return pings;
	}
}