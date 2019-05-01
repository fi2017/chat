package chatFPAUebung.interfaces;

/*
	Autor: Philipp Hägerich

	This work ist made for the Chat-Project from the FI2017 from the BSZ
 */

public interface BlackListInterface
{
	void addUserToBlackList(String username);
	void removeUserFromBlackList(String username);
	boolean canUserJoint(String username);
}
