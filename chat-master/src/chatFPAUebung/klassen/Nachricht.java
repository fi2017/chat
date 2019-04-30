package chatFPAUebung.klassen;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Nachricht implements Serializable
{
	// Attribute
	private static final long serialVersionUID = 1L;

	private String nachricht;
	private LocalDateTime sendezeitpunkt;

	// Konstruktor
	public Nachricht(String nachricht, LocalDateTime sendezeitpunkt)
	{
		this.nachricht = nachricht;
		this.sendezeitpunkt = sendezeitpunkt;
	}

	// Methoden
	public String toString()
	{
		return "(" + getSendezeitpunkt().format(DateTimeFormatter.ofPattern("HH.mm.ss")) + ") " + getNachricht();
	}

	// Getter
	public String getNachricht()
	{
		return nachricht;
	}

	public LocalDateTime getSendezeitpunkt()
	{
		return sendezeitpunkt;
	}
}
