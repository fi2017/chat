package chatFPAUebung.ddosProtection;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class SecurityUnsafe
{
	// Final
	private static int flagsNeededForBan = 4; // How many flags a user needs to get to be banned
	private static int flagsNeededInSeconds = 10; // In what time the flags need to be collected to be banned

	private static int sendingTimeDifferenceDeviation = 100; // How many MS the time difference between 2 messages can
																// be from the avg. to still be eventually from a bot

	// Attributes
	private ArrayList<LocalDateTime> pings; // All messages from the user to the server
	private ArrayList<LocalDateTime> flags; // All times when the user was detected to be a bot eventually

	private int avgSendingTimeDifferenceMS; // What the avg. time between two messages from the user is in the last X
											// seconds ago

	// Constructor
	public SecurityUnsafe()
	{
		this.pings = new ArrayList<LocalDateTime>();
		this.flags = new ArrayList<LocalDateTime>();
	}

	// Methods
	public boolean addNewPing(LocalDateTime messageSendingTime)
	{
		// Adds a new ping whenever the user sends a message to the server
		getPings().add(messageSendingTime);

		if (checkNewFlag()) // Checks if a new flag is needed
		{
			if (checkFlagsForBan()) // Checks if enough flags are collected to ban the client
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

	private boolean checkNewFlag()
	{
		calculateNewAvgSendingTime();

		// Checks if a flag is possible
		if (getAvgSendingTimeDifferenceMS() != 0)
		{
			long sendingTimeDifferenceMS = ChronoUnit.MILLIS.between(getPings().get(getPings().size() - 1),
					getPings().get(getPings().size() - 2));

			// Checks if the difference of time between the message is around the avg. time
			// between messages
			if ((sendingTimeDifferenceMS + sendingTimeDifferenceDeviation >= getAvgSendingTimeDifferenceMS()
					&& sendingTimeDifferenceMS <= getAvgSendingTimeDifferenceMS())
					|| (sendingTimeDifferenceMS - sendingTimeDifferenceDeviation <= getAvgSendingTimeDifferenceMS()
							&& sendingTimeDifferenceMS >= getAvgSendingTimeDifferenceMS())) // IF ACT. DIF + iS > AVG
																							// AND
																							// ACT. DIF - IS < AVG
			{
				getFlags().add(LocalDateTime.now()); // Adds a new Flag for the current time

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

	private void calculateNewAvgSendingTime()
	{
		int lastPingInListFromXsecondsOrLessAgo = 0;

		// Gets the last ping that can be used for the check
		for (int i = 0; i < getPings().size() && lastPingInListFromXsecondsOrLessAgo == 0; i++)
		{
			if (ChronoUnit.SECONDS.between(getPings().get(i), LocalDateTime.now()) <= flagsNeededInSeconds)
			{
				lastPingInListFromXsecondsOrLessAgo = i;
			}
		}

		if (lastPingInListFromXsecondsOrLessAgo != 0)
		{
			int secondsAll = 0;
			int countedTimes = 0;

			// Calculates the avg. sending time between messages
			for (int i = lastPingInListFromXsecondsOrLessAgo; i < getPings().size() - 1; i++)
			{
				secondsAll += ChronoUnit.MILLIS.between(getPings().get(i), getPings().get(i + 1));
				countedTimes++;
			}

			// It only replaces the avg. sending time if there were enough messages
			if (countedTimes > 1)
			{
				setAvgSendingTimeDifferenceMS(secondsAll / countedTimes);
			} else
			{
				setAvgSendingTimeDifferenceMS(0);
			}
		} else
		{
			setAvgSendingTimeDifferenceMS(0);
		}
	}

	private boolean checkFlagsForBan()
	{
		// Checks if there are enough flags at the moment
		if (getFlags().size() >= flagsNeededForBan)
		{
			LocalDateTime earliestNeededFlag = getFlags().get(getFlags().size() - flagsNeededForBan - 1);
			LocalDateTime lastNeededFlag = getFlags().get(getFlags().size() - 1);

			// Checks if the flags were in a short enough period
			if (getDifferenceFromTimesInMS(earliestNeededFlag, lastNeededFlag) < flagsNeededInSeconds)
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

	// Returns the difference between 2 LocalDateTimes in MS
	private long getDifferenceFromTimesInMS(LocalDateTime ping1, LocalDateTime ping2)
	{
		return ChronoUnit.MILLIS.between(ping1, ping2);
	}

	// Getter
	public ArrayList<LocalDateTime> getPings()
	{
		return this.pings;
	}

	public ArrayList<LocalDateTime> getFlags()
	{
		return this.flags;
	}

	public int getAvgSendingTimeDifferenceMS()
	{
		return this.avgSendingTimeDifferenceMS;
	}

	public void setAvgSendingTimeDifferenceMS(int avgSendingTimeDifferenceMS)
	{
		this.avgSendingTimeDifferenceMS = avgSendingTimeDifferenceMS;
	}
}