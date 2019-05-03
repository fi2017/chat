package feature_LoginRegister;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

public class User implements Serializable
{

	private String username;
	private int id;
	private String password;
	private int globalRollenNummer;
	private LocalDate banned = LocalDate.now();
	private boolean online=false;
	private boolean neu = false;
	private File profilbild;

	private Instant timeout = null;

	public User()
	{

	}

	public User(String uname, String pw, int id, int grnummer)
	{
		setUsername(uname);
		setPassword(pw);
		setId(id);
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

	public int getGlobalRollenNummer()
	{
		return globalRollenNummer;
	}

	public void setGlobalRollenNummer(int globalRollenNummer)
	{
		this.globalRollenNummer = globalRollenNummer;
	}

	public boolean isOnline()
	{
		return online;
	}

	public void setOnline(boolean online)
	{
		this.online = online;
	}

	public LocalDate isBanned()
	{
		return banned;
	}

	public void setBanned(LocalDate date)
	{
		this.banned = date;
	}

	public boolean isNeu()
	{
		return neu;
	}

	public void setNeu(boolean neu)
	{
		this.neu = neu;
	}

	public String getStatus()
	{
		if(online)
		{
			if(isInTimeout())
			{
				return "timeout";
			}
			return "online";
		}
		else
		{
			return "offline";
		}
	}

	public boolean isInTimeout()
	{

		if(timeout==null)
		{
			return false;
		}
		else
		{
			if(timeout.compareTo(Instant.now())<=0)
			{
				timeout = null;
				return false;
			}
		}
		return true;
	}

	public File getProfilbild()
	{
		return profilbild;
	}

	public void setProfilbild(File profilbild)
	{
		this.profilbild = profilbild;
	}
}
