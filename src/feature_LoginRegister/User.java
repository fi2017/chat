package feature_LoginRegister;

public class User
{

	private String username;
	private int id;
	private String password;
	private int battletag;
	private int globalRollenNummer;
	
	public User()
	{
		
	}
	
	public User(String uname, String pw, int id, int btag,int grnummer)
	{
		setUsername(uname);
		setPassword(pw);
		setId(id);
		setBattletag(btag);
		setGlobalRollenNummer(grnummer);
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public int getBattletag()
	{
		return battletag;
	}

	public void setBattletag(int battletag)
	{
		this.battletag = battletag;
	}

	public int getGlobalRollenNummer()
	{
		return globalRollenNummer;
	}

	public void setGlobalRollenNummer(int globalRollenNummer)
	{
		this.globalRollenNummer = globalRollenNummer;
	}
	
	
}
