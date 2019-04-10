package chatFPAUebung.klassen;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Uebertragung implements Serializable
{
	// Attribute
	private static final long serialVersionUID = 1L;
	private int zweck;
	private int ziel;
	private Object uebertragung;
	private LocalDateTime uebertragungszeitpunkt;

	/*
	*
	*	Zwecke:
	*	1: Bisher
	* */


	// Konstruktor (Ziel == Chatroom welcher die Nachrichte erhalten soll)
	public Uebertragung(int zweck, int ziel, Object uebertragung)
	{
		this.zweck = zweck;
		this.ziel = ziel;
		this.uebertragung = uebertragung;
	}

	public Uebertragung(int zweck, Object uebertragung)
	{
		this.zweck = zweck;
		this.uebertragung = uebertragung;
		this.uebertragungszeitpunkt = LocalDateTime.now();
	}

	public Uebertragung(Uebertragung uebertragungCopy)
	{
		this.zweck = uebertragungCopy.getZweck();
		this.uebertragung = uebertragungCopy.getUebertragung();
		this.uebertragungszeitpunkt = LocalDateTime.now();
	}

	// Getter
	public int getZweck()
	{
		return zweck;
	}

	public int getZiel() { return ziel; }

	public Object getUebertragung()
	{
		return uebertragung;
	}

	public LocalDateTime getUebertragungszeitpunkt()
	{
		return this.uebertragungszeitpunkt;
	}
}
