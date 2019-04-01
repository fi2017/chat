package feature_LoginRegister;

public class LogRegNachricht
{
	String username;
	String password;
	
	public LogRegNachricht( String uname, String pass)
	{

		setUsername(uname);
		setPassword(pass);
	}

	

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}
