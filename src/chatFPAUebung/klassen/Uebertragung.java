package chatFPAUebung.klassen;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Uebertragung implements Serializable
{
	// Attribute
	private static final long serialVersionUID = 1L;
	private int zweck;
	private Object uebertragung;
	private LocalDateTime uebertragungszeitpunkt;

	// Konstruktor
	public Uebertragung(int zweck, Object uebertragung)
	{
		this.zweck = zweck;
		this.uebertragung = uebertragung;
		this.uebertragungszeitpunkt = LocalDateTime.now();
	}

	// Getter
	public int getZweck()
	{
		return zweck;
	}

	public Object getUebertragung()
	{
		return uebertragung;
	}

	public LocalDateTime getUebertragungszeitpunkt()
	{
		return this.uebertragungszeitpunkt;
	}
}
