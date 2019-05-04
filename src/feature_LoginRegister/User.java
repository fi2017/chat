package feature_LoginRegister;

import javax.swing.*;
import java.io.File;
import java.sql.Date;

public class User
{

	private String username;
	private int id;
	private String password;
	private int globalRollenNummer;
	private Date banned = new Date(0);
	private boolean online=false;
	private boolean neu = false;
	private File image;

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

	public Date isBanned()
	{
		return banned;
	}

	public void setBanned(Date date)
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


	public File getImage()
	{
		return image;
	}

	public void setImage(File image)
	{
		this.image = image;
	}
}
