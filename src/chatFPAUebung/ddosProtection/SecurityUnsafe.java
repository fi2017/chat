package chatFPAUebung.ddosProtection;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class SecurityUnsafe
{
	// Finals
	private static int flagsNeededForBan = 4; // Sets how many flags a user needs to get to be banned
	private static int flagsNeededInSeconds = 10; // Sets in what time the above set flags need to be collected in
													// order for the user to be banned

	private static int sendingTimeDifferenceDeviation = 100; // Sets how many MS the time difference between 2 messages
																// is allowed be from the avg. to still receive a flag

	// Attributes
	private ArrayList<LocalDateTime> pings; // Shows the sending time of all messages from the user to the server
	private ArrayList<LocalDateTime> flags; // Shows all times when the user was detected to be a bot eventually

	private int avgSendingTimeDifferenceMS; // Shows what the avg. time between two messages from the user is in the
											// last X seconds ago

	// Constructor
	public SecurityUnsafe()
	{
		this.pings = new ArrayList<LocalDateTime>();
		this.flags = new ArrayList<LocalDateTime>();
	}

	// Methods
	public boolean addNewPing(LocalDateTime messageSendingTime) // Adds a new ping whenever the user sends a message to
																// the server
	{
		getPings().add(messageSendingTime);

		if (checkNewFlag()) // Checks if a new flag is needed
		{
			if (!checkFlagsForBan()) // Checks if enough flags are collected to ban the client
			{
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return true;
		}
	}

	private boolean checkNewFlag() // Checks if a new flag needs to be added and adds it if so
	{
		calculateNewAvgSendingTime();

		if (getAvgSendingTimeDifferenceMS() != 0) // Checks if a flag is possible
		{
			long sendingTimeDifferenceMS = ChronoUnit.MILLIS.between(getPings().get(getPings().size() - 1),
					getPings().get(getPings().size() - 2));

			// Checks if the difference of time between the message from the avg. time is
			// big enough to probably not be from a bot
			if ((sendingTimeDifferenceMS + sendingTimeDifferenceDeviation >= getAvgSendingTimeDifferenceMS()
					&& sendingTimeDifferenceMS <= getAvgSendingTimeDifferenceMS())
					|| (sendingTimeDifferenceMS - sendingTimeDifferenceDeviation <= getAvgSendingTimeDifferenceMS()
							&& sendingTimeDifferenceMS >= getAvgSendingTimeDifferenceMS()))
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

	private void calculateNewAvgSendingTime() // Calculates the avg. sending time within the last X seconds
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

	private boolean checkFlagsForBan() // Checks if the user collected enough flags in order to be banned
	{
		// Checks if there are enough flags at the moment to even be able to get a ban
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

	private long getDifferenceFromTimesInMS(LocalDateTime ping1, LocalDateTime ping2) // Returns the difference between
																						// 2 LocalDateTimes in MS
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