package chatFPAUebung.interfaces;

/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */

import chatFPAUebung.klassen.AdminProxy;
import chatFPAUebung.klassen.TimeoutData;

import java.time.Instant;

public interface ServerRemoteControl
{
	void shutdownServerNow();

	void removeAdmin(AdminProxy adminProxy);

	void refreshUser(AdminProxy admin);

	void setTimeout(TimeoutData data);

	void addAdmin(AdminProxy admin);

	//can be changed if then changed in AdminProxy
	void addMessage(String message);
}
