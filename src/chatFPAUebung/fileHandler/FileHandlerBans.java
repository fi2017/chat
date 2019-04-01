package chatFPAUebung.fileHandler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import chatFPAUebung.klassen.Ban;

public class FileHandlerBans
{
	// Attributes
	private static final String path = "../ressources/bans.ser";

	// Constructor
	public FileHandlerBans()
	{
		//
	}

	public boolean writeBans(Ban[] arrayBans)
	{
		try (FileOutputStream fileOut = new FileOutputStream(path);
				ObjectOutputStream out = new ObjectOutputStream(fileOut))
		{
			out.writeObject(arrayBans);
			return true;
		} catch (IOException e)
		{
			System.err.println("Fehler beim Schreiben der Bans!");

			e.printStackTrace();
			return false;
		}
	}

	public Ban[] readBans()
	{
		try (FileInputStream fileIn = new FileInputStream(path); ObjectInputStream in = new ObjectInputStream(fileIn))
		{
			Ban[] arrayBans = (Ban[]) in.readObject();
			return arrayBans;
		} catch (Exception e)
		{
			System.err.println("Fehler beim Lesen der Bans!");

			e.printStackTrace();
			return new Ban[0];
		}
	}
}
