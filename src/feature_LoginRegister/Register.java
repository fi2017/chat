package feature_LoginRegister;

public class Register
{
	int protocolCase;
	String username;
	String password;
	
	public Register(int protCase, String uname, String pass)
	{
		setProtocolCase(protCase);
		setUsername(uname);
		setPassword(pass);
	}

	public int getProtocolCase()
	{
		return protocolCase;
	}

	public void setProtocolCase(int protocolCase)
	{
		this.protocolCase = protocolCase;
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
