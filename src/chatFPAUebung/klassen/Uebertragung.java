package chatFPAUebung.klassen;

import java.io.Serializable;

public class Uebertragung implements Serializable
{
	// Attribute
	private static final long serialVersionUID = 1L;
	private int zweck;
	private Object uebertragung;

	// Konstruktor
	public Uebertragung(int zweck, Object uebertragung)
	{
		this.zweck = zweck;
		this.uebertragung = uebertragung;
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
}
