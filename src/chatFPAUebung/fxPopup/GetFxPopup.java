package chatFPAUebung.fxPopup;

public abstract class GetFxPopup
{
	public static Info INFO()
	{
		return new Info();
	}

	public static Input INPUT()
	{
		return new Input();
	}

	public static Confirm CONFIRM()
	{
		return new Confirm();
	}
}
