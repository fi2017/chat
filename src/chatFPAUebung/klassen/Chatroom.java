package chatFPAUebung.klassen;

import javax.swing.*;
import java.util.ArrayList;

public class Chatroom
{
    private String name;
    private static int idNew = 0;
    private int id;
    private int maxTeilnehmer;
    private String passwort;
    private DefaultListModel Chatmodel;

    ArrayList<ClientProxy> teilnehmer = new ArrayList<ClientProxy>();
    ArrayList<String> blacklist = new ArrayList<String>();   //Methode zum blacklisten

    public void hinzufuegen(ClientProxy c)
    {
        teilnehmer.add(c);
    }

    public void entfernen(ClientProxy c)
    {
        teilnehmer.remove(c);
    }

    public Chatroom(String name, int maxTeilnehmer)
    {
        this.id = idNew;
        idNew ++;
        this.name = name;
        this.maxTeilnehmer = maxTeilnehmer;
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

    public ArrayList<ClientProxy> getTeilnehmer()
    {
        return teilnehmer;
    }

    public void setTeilnehmer(ArrayList<ClientProxy> teilnehmer)
    {
        this.teilnehmer = teilnehmer;
    }

    public int getMaxTeilnehmer() {
        return maxTeilnehmer;
    }

    public void setMaxTeilnehmer(int maxTeilnehmer) {
        this.maxTeilnehmer = maxTeilnehmer;
    }

    public String getPasswort() { return passwort; }

    public void setPasswort(String passwort) { this.passwort = passwort; }

    public ArrayList<String> getBlacklist() { return blacklist; }

    public void setBlacklist(ArrayList<String> blacklist) { this.blacklist = blacklist; }

    public DefaultListModel getChatmodel()
    {
        return Chatmodel;
    }

    public void setChatmodel(DefaultListModel chatmodel)
    {
        Chatmodel = chatmodel;
    }
}
