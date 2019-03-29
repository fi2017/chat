package Chatrooms;

import java.util.ArrayList;

public class Chatroom
{
    String name;
    int id;
    ArrayList<Client> teilnehmer = new ArrayList<Client>();

    public void hinzufuegen(Client c)
    {
        teilnehmer.add(c);
    }

    public void entfernen(Client c)
    {
        teilnehmer.remove(c);
    }



    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public ArrayList<Client> getTeilnehmer()
    {
        return teilnehmer;
    }

    public void setTeilnehmer(ArrayList<Client> teilnehmer)
    {
        this.teilnehmer = teilnehmer;
    }
}
