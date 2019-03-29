package feature_LoginRegister;

public class LogRegControl
{
	LogRegGui gui;
	
	public static void main(String[] args)
	{
		new LogRegControl(new LogRegGui());
	}
	
	public LogRegControl(LogRegGui g)
	{
		setGui(g);
		g.setVisible(true);
		addActionListenersToGuiObjects();
	}
	
	

	
	
	
	
	
	private void addActionListenersToGuiObjects()
	{
		getGui().getBtnLogin().addActionListener(e->loginUser());
		getGui().getBtnReg().addActionListener(e->registerUser());
	}

	private void loginUser()
	{
		
	}

	private void registerUser()
	{
		if(checkPasswordWith2nd())
		{
			schickeNachricht(new RegLogNachricht(70,getGui().getTextFieldUnameReg().getText(),getGui().getTextFieldPwReg().getText()));
		}
		else
		{
			// Später in der Gui als Label Anzeige einfügen
			System.out.println("Passwörter stimmen nicht überein!");
		}
	}
	

	private void schickeNachricht(RegLogNachricht register)
	{
		//Code um an Server zu schicken
	}

	private boolean checkPasswordWith2nd()
	{
		boolean returnValue=false;
		
		if(getGui().getTextFieldPwReg().equals(getGui().getTextFieldPwRepeat()))
		{
			returnValue = true;
		}
		
		return returnValue;
	}

	//GETTER SETTER
	
	
	public LogRegGui getGui()
	{
		return gui;
	}

	public void setGui(LogRegGui gui)
	{
		this.gui = gui;
	}
}
