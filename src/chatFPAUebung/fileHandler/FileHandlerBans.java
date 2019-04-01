package chatFPAUebung.fileHandler;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import chatFPAUebung.klassen.Ban;

public class FileHandlerBans
{
	// Attributes
	private final Path path = Paths.get("src/chatFPAUebung/ressources", "bans.ser");

	// Constructor
	public FileHandlerBans()
	{
		//
	}

	public boolean writeBans(Ban[] arrayBans)
	{
		try (FileOutputStream fileOut = new FileOutputStream(path.toFile());
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
		try (FileInputStream fileIn = new FileInputStream(path.toFile());
				ObjectInputStream in = new ObjectInputStream(fileIn))
		{
			Ban[] arrayBans = (Ban[]) in.readObject();
			return arrayBans;
		} catch (EOFException e)
		{
			System.err.print("Die ausgelesene Datei war leer!");
			return new Ban[0];
		} catch (Exception e)
		{
			System.err.println("Fehler beim Lesen der Bans!");

			e.printStackTrace();
			return new Ban[0];
		}
	}
}
